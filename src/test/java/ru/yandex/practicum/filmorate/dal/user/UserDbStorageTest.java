package ru.yandex.practicum.filmorate.dal.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({UserDbStorage.class, UserRowMapper.class})
class UserDbStorageTest {
    private final UserDbStorage storage;

    @Autowired
    public UserDbStorageTest(UserDbStorage userStorage) {
        this.storage = userStorage;
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setLogin("litvitnik");
        user.setName("Виталий Литуновский");
        user.setBirthday(LocalDate.of(1997, 12, 18));
        user.setEmail("litvitnik@yandex.ru");
        storage.create(user);

        Optional<User> userOptional = storage.findById(user.getId());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(foundUser ->
                        assertThat(foundUser)
                                .hasFieldOrPropertyWithValue("id", user.getId())
                                .hasFieldOrPropertyWithValue("email", "litvitnik@yandex.ru")
                                .hasFieldOrPropertyWithValue("login", "litvitnik")
                );
    }

    @Test
    void testCreate() {
        User originalUser = new User();
        originalUser.setLogin("litvitnik");
        originalUser.setName("Виталий Литуновский");
        originalUser.setBirthday(LocalDate.of(1997, 12, 18));
        originalUser.setEmail("litvitnik@yandex.ru");
        storage.create(originalUser);

        assertThat(originalUser)
                .as("Метод create не должен возвращать null")
                .isNotNull();

        assertThat(originalUser.getId())
                .as("Методу create должен быть присвоен ID базой данных")
                .isNotNull();

        Optional<User> userFromDbOptional = storage.findById(originalUser.getId());

        assertThat(userFromDbOptional)
                .as("Сохраненный пользователь должен находиться в базе по своему ID")
                .isPresent();

        User userFromDb = userFromDbOptional.get();

        assertThat(userFromDb.getLogin())
                .as("Логин сохраненного пользователя должен совпадать с оригиналом")
                .isEqualTo(originalUser.getLogin());

        assertThat(userFromDb.getName())
                .as("Имя сохраненного пользователя должно совпадать с оригиналом")
                .isEqualTo(originalUser.getName());

        assertThat(userFromDb.getEmail())
                .as("Email сохраненного пользователя должен совпадать с оригиналом")
                .isEqualTo(originalUser.getEmail());
    }


    @Test
    void testUpdate() {
        User originalUser = new User();
        originalUser.setLogin("litvitnik");
        originalUser.setName("Виталий Литуновский");
        originalUser.setBirthday(LocalDate.of(1997, 12, 18));
        originalUser.setEmail("litvitnik@yandex.ru");
        storage.create(originalUser);

        User updatedInfo = new User();
        updatedInfo.setId(originalUser.getId());
        updatedInfo.setLogin("Burlakov");
        updatedInfo.setName("Сергей Бурлаков");
        updatedInfo.setBirthday(LocalDate.of(2025, 10, 9));
        updatedInfo.setEmail("Burlakov@yandex.ru");
        storage.update(updatedInfo);

        User userFromDb = storage.findById(updatedInfo.getId()).get();

        assertThat(userFromDb)
                .as("Метод update не должен возвращать null")
                .isNotNull();

        assertThat(userFromDb.getName())
                .as("некорректно обновилось имя пользователя")
                .isEqualTo(updatedInfo.getName());

        assertThat(userFromDb.getBirthday())
                .as("некорректно обновилась дата рождения пользователя")
                .isEqualTo(updatedInfo.getBirthday());

        assertThat(userFromDb.getEmail())
                .as("некорректно обновилась почта пользователя")
                .isEqualTo(updatedInfo.getEmail());

    }

    @Test
    void testFindAll() {
        User user1 = new User();
        user1.setLogin("litvitnik");
        user1.setName("Виталий Литуновский");
        user1.setBirthday(LocalDate.of(1997, 12, 18));
        user1.setEmail("litvitnik@yandex.ru");
        storage.create(user1);

        User user2 = new User();
        user2.setLogin("Burlakov");
        user2.setName("Сергей Бурлаков");
        user2.setBirthday(LocalDate.of(1997, 12, 18));
        user2.setEmail("Burlakov@yandex.ru");
        storage.create(user2);

        Collection<User> users = storage.findAll();

        assertThat(users)
                .as("Метод findAll не должен возвращать null")
                .isNotNull();

        assertThat(users.size())
                .as("Метод findAll некорректно возвращает пользователей")
                .isEqualTo(2);

    }

}