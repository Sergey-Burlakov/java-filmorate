package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public Collection<User> findAll() {
        log.trace("Поступил запрос на вывод всех пользователей из хранилища");
        return storage.findAll();
    }

    public User create(User user) {
        log.trace("Поступил запрос на добавление пользователя");
        validateUser(user);
        log.debug("Пользователь login = «{}» прошел валидацию при создании", user.getLogin());
        return storage.create(user);
    }

    public User update(User user) {
        log.trace("Поступил запрос на обновление пользователя");
        if (user.getId() == null) {
            log.error("В id объекта передано null значение");
            throw new IllegalArgumentException();
        }

        User oldUser = storage.findById(user.getId());
        User candidateUser = new User(oldUser);

        if (user.getEmail() != null && (!user.getEmail().isBlank())) {
            candidateUser.setEmail(user.getEmail());
            log.debug("UserId = {} изменился параметр Email. Новое значение «{}» старое значение «{}»",
                    user.getId(), user.getEmail(), oldUser.getEmail());
        }
        if (user.getLogin() != null && (!user.getLogin().isBlank())) {
            candidateUser.setLogin(user.getLogin());
            log.debug("UserId = {} изменился параметр Login. Новое значение «{}» старое значение «{}»",
                    user.getId(), user.getLogin(), oldUser.getLogin());
        }
        if (user.getName() != null && (!user.getName().isBlank())) {
            candidateUser.setName(user.getName());
            log.debug("UserId = {} изменился параметр Name. Новое значение «{}» старое значение «{}»",
                    user.getId(), user.getName(), oldUser.getName());
        } else {
            setDefaultNameIfEmpty(candidateUser);
        }
        if (user.getBirthday() != null && (!user.getBirthday().equals(oldUser.getBirthday()))) {
            candidateUser.setBirthday(user.getBirthday());
            log.debug("UserId = {} изменился параметр Birthday. Новое значение «{}» старое значение «{}»",
                    user.getId(), user.getBirthday(), oldUser.getBirthday());
        }

        validateUser(candidateUser);
        log.debug("Пользователь id = {} прошел валидацию при обновлении", candidateUser.getId());

        storage.update(candidateUser);
        log.info("Пользователь Id = {}, Login = {} успешно обновлен", candidateUser.getId(), candidateUser.getLogin());
        return candidateUser;
    }

    public void addFriend(long userId, long friendId) {
        log.trace("Поступил запрос на добавление в друзья");
        if (userId == friendId) {
            String message = "Невозможно добавить в друзья самого себя";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        checkIds(userId, friendId);
        log.debug("ID пользователей прошли проверку");
        storage.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        log.trace("Поступил запрос на удаление из друзей");
        if (userId == friendId) {
            String message = "Невозможно удалить самого себя из друзей";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        checkIds(userId, friendId);
        log.debug("ID пользователей прошли проверку");
        storage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(long id) {
        log.trace("Поступил запрос на получение списка друзей");
        storage.findById(id);
        return storage.getFriends(id);
    }

    public List<User> getMutualFriends(long userId, long otherId) {
        log.trace("Поступил запрос на получение списка общих друзей");
        checkIds(userId, otherId);
        log.debug("ID пользователей прошли проверку");
        if (userId == otherId) {
            String message = "Невозможно просмотреть общих друзей с самим собой";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return storage.getMutualFriends(userId, otherId);
    }

    private void checkIds(long firstId, long secondId) {
        storage.findById(firstId);
        storage.findById(secondId);
    }


    private void validateUser(User user) {
        //TODO попробовать сделать улученную валидацию почты
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            String message = "Имейл должен быть указан";
            log.error(message);
            throw new ValidationException(message);
        }
        if (!user.getEmail().contains("@")) {
            String message = "Неверный формат почты";
            log.error(message);
            throw new ValidationException(message);
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            String message = "Логин не может быть пустым и содержать пробелы";
            log.error(message);
            throw new ValidationException(message);
        }
        if (user.getBirthday() == null) {
            String message = "Дата рождения не может быть пуста";
            log.error(message);
            throw new ValidationException(message);
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            String message = "Дата рождения не может быть в будущем";
            log.error(message);
            throw new ValidationException(message);
        }
    }

    private void setDefaultNameIfEmpty(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя ID = {} было не заполнено, установлено значение по умолчанию " +
                    "Login = Name = {}", user.getId(), user.getLogin());
        }
    }

}
