package ru.yandex.practicum.filmorate.dal.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage  {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> friends = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public void addFriend(Long userId, Long friendId) {
        friends.computeIfAbsent(userId, k -> new HashSet<>()).add(friendId);
        friends.computeIfAbsent(friendId, k -> new HashSet<>()).add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (friends.get(userId) != null) {
            friends.get(userId).remove(friendId);
        }
        if (friends.get(friendId) != null) {
            friends.get(friendId).remove(userId);
        }
    }

    public List<User> getFriends(long id) {
        List<User> friendsList = new ArrayList<>();
        if (friends.get(id) == null || friends.get(id).isEmpty()) {
            return friendsList;
        } else {
            for (Long entryId : friends.get(id))
                friendsList.add(users.get(entryId));
        }
        return friendsList;
    }

    public List<User> getMutualFriends(long userId, long otherId) {
        if (chekId(userId) && chekId(otherId)) {
            Set<Long> copyUserFriends = new HashSet<>(friends.get(userId));
            copyUserFriends.retainAll(friends.get(otherId));
            return copyUserFriends
                    .stream()
                    .map(users::get)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Optional<User> findById(Long userId) {
            return Optional.ofNullable(users.get(userId));
    }

    private boolean chekId(long id) {
        return (!(friends.get(id) == null || friends.get(id).isEmpty()));
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
