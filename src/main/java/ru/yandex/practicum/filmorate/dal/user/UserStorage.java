package ru.yandex.practicum.filmorate.dal.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    Collection<User> findAll();

    Optional<User> findById(Long userId);

    User update(User user);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<User> getFriends(long id);

    List<User> getMutualFriends(long userId, long otherId);
}
