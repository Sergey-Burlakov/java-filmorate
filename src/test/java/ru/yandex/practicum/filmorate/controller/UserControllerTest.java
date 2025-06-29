package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController = new UserController();
    User user;

    @BeforeEach
    void serializeUserBefore(){
        user = new User();
        user.setEmail("litunovskiy.vitaliy@yandex.ru");
        user.setLogin("litvitnik");
        user.setName("Виталий Литуновский");
        user.setBirthday(LocalDate.of(1997,12,18));
    }

    @Test
    void testCreateExceptionNullEmai(){
        user.setEmail(" ");
        assertThrows(ResponseStatusException.class, () -> {
            userController.create(user);
        }, "Не отработало исключение при создании пользователя с пустой почтой");
    }

    @Test
    void testCreateExceptionInvalidEmai(){
        user.setEmail("yandex.ru");
        assertThrows(ResponseStatusException.class, () -> {
            userController.create(user);
        }, "Не отработало исключение при создании пользователя с некорректной почтой");
    }

    @Test
    void testCreateExceptionNullLogin(){
        user.setLogin("");
        assertThrows(ResponseStatusException.class, () -> {
            userController.create(user);
        }, "Не отработало исключение при создании пользователя с пустым логином");
    }

    @Test
    void testCreateExceptionWithSpaseInLogin(){
        user.setLogin("Litunovskiy Vitaliy");
        assertThrows(ResponseStatusException.class, () -> {
            userController.create(user);
        }, "Не отработало исключение при создании пользователя с логином, которое содержит пробелы");
    }

    @Test
    void testCreateExceptionNullBirthday(){
        user.setBirthday(null);
        assertThrows(ResponseStatusException.class, () -> {
            userController.create(user);
        }, "Не отработало исключение при создании пользователя с пустым днем рождения");
    }

    @Test
    void testCreateExceptionFutureBirthday(){
        user.setBirthday(LocalDate.of(2077,12,18));
        assertThrows(ResponseStatusException.class, () -> {
            userController.create(user);
        }, "Не отработало исключение при создании пользователя с будущим днем рождения");
    }
}