package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        try {
            validateUser(user);
            log.info("Валидация пользователя при создании" +
                    " Id = {} Login = {} прошла успешно.", user.getLogin(), user.getId());
        } catch (ValidationException e) {
            log.error("Ошибка валидации пользователя при создании {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        user.setId(getNextId());
        setDefaultNameIfEmpty(user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User userWithUpdates) {

        if (userWithUpdates.getId() == null) {
            String message = "id должен быть заполнен";
            log.warn(message);
            throw new ValidationException("id должен быть заполнен");
        }
        if (!users.containsKey(userWithUpdates.getId())) {
            String message = String.format("Пользователь Id = %d не найден\n",userWithUpdates.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }

        User oldUser = users.get(userWithUpdates.getId());
        User candidateUser = new User(oldUser);

        if (userWithUpdates.getEmail() != null && (!userWithUpdates.getEmail().isBlank())){
            candidateUser.setEmail(userWithUpdates.getEmail());
        }
        if (userWithUpdates.getLogin() != null && (!userWithUpdates.getLogin().isBlank())){
            candidateUser.setLogin(userWithUpdates.getLogin());
        }
        if (userWithUpdates.getName() != null && (!userWithUpdates.getName().isBlank())){
            candidateUser.setName(userWithUpdates.getName());
        } else {
            setDefaultNameIfEmpty(candidateUser);
        }
        if (userWithUpdates.getBirthday() != null){
            candidateUser.setBirthday(userWithUpdates.getBirthday());
        }

        try {
            validateUser(candidateUser);
            log.info("Валидация пользователя при обновлении" +
                    " Id = {} Login = {} прошла успешно.", candidateUser.getLogin(), candidateUser.getId());
        } catch (ValidationException e) {
            log.error("Ошибка валидации пользователя при обновлении {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        users.put(candidateUser.getId(),candidateUser);
        log.info("Пользователь Id = {}, Login = {} успешно обновлен", candidateUser.getId(),candidateUser.getLogin());
        return candidateUser;
    }


    private void validateUser(User user) {
        //TODO попробовать сделать улученную валидацию почты
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Имейл должен быть указан");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Неверный формат почты");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday() == null){
            throw new ValidationException("Дата рождения не может быть пуста");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    private void setDefaultNameIfEmpty(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя ID = {} было не заполнено, установлено значение по умолчанию " +
                    "Login = Name = {}", user.getId(), user.getLogin());
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
