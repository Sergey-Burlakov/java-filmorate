package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest {
    private UserService userService;
    private InMemoryUserStorage userStorage;
    private InMemoryFilmStorage filmStorage;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
    }

    @Test
    void testCreateExceptionNullEmai() {
        User user = new User();
        user.setLogin("litvitnik");
        user.setName("Виталий Литуновский");
        user.setBirthday(LocalDate.of(1997, 12, 18));

        user.setEmail(" ");
        assertThrows(ValidationException.class, () -> {
            userService.create(user);
        }, "Не отработало исключение при создании пользователя с пустой почтой");
    }

    @Test
    void testCreateExceptionInvalidEmai() {
        User user = new User();
        user.setLogin("litvitnik");
        user.setName("Виталий Литуновский");
        user.setBirthday(LocalDate.of(1997, 12, 18));

        user.setEmail("yandex.ru");
        assertThrows(ValidationException.class, () -> {
            userService.create(user);
        }, "Не отработало исключение при создании пользователя с некорректной почтой");
    }

    @Test
    void testCreateExceptionNullLogin() {
        User user = new User();
        user.setEmail("litunovskiy.vitaliy@yandex.ru");
        user.setName("Виталий Литуновский");
        user.setBirthday(LocalDate.of(1997, 12, 18));

        user.setLogin("");
        assertThrows(ValidationException.class, () -> {
            userService.create(user);
        }, "Не отработало исключение при создании пользователя с пустым логином");
    }

    @Test
    void testCreateExceptionWithSpaseInLogin() {
        User user = new User();
        user.setEmail("litunovskiy.vitaliy@yandex.ru");
        user.setName("Виталий Литуновский");
        user.setBirthday(LocalDate.of(1997, 12, 18));

        user.setLogin("Litunovskiy Vitaliy");
        assertThrows(ValidationException.class, () -> {
            userService.create(user);
        }, "Не отработало исключение при создании пользователя с логином, которое содержит пробелы");
    }

    @Test
    void testCreateExceptionNullBirthday() {
        User user = new User();
        user.setEmail("litunovskiy.vitaliy@yandex.ru");
        user.setLogin("litvitnik");
        user.setName("Виталий Литуновский");

        user.setBirthday(null);
        assertThrows(ValidationException.class, () -> {
            userService.create(user);
        }, "Не отработало исключение при создании пользователя с пустым днем рождения");
    }

    @Test
    void testCreateExceptionFutureBirthday() {
        User user = new User();
        user.setEmail("litunovskiy.vitaliy@yandex.ru");
        user.setLogin("litvitnik");
        user.setName("Виталий Литуновский");

        user.setBirthday(LocalDate.of(2077, 12, 18));
        assertThrows(ValidationException.class, () -> {
            userService.create(user);
        }, "Не отработало исключение при создании пользователя с будущим днем рождения");
    }

}