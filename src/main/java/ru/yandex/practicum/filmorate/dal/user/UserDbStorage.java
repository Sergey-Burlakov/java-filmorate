package ru.yandex.practicum.filmorate.dal.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("userDbStorage")
public class UserDbStorage extends BaseRepository<User> implements UserStorage {

    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friendships(id_user1, id_user2) VALUES (?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friendships WHERE id_user1 = ? AND id_user2 = ?";
    private static final String FIND_FRIENDS_QUERY = "SELECT u.* FROM users AS u JOIN friendships AS f ON u.id = f.id_user2 WHERE f.id_user1 = ?";
    private static final String GET_MUTUAL_FRIENDS_QUERY = "SELECT u.* FROM users AS u JOIN friendships AS f1 ON u.id = f1.id_user2 JOIN friendships AS f2 ON u.id = f2.id_user2 WHERE f1.id_user1 = ? AND f2.id_user1 = ?";

    public UserDbStorage(JdbcTemplate jdbc, UserRowMapper mapper) {
        super(jdbc, mapper, User.class);
    }

    @Override
    public User create(User user) {
        long id = insertAndReturnId(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    @Override
    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return findById(user.getId()).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь id = %d при обновлении не найден",user.getId())));
    }

    @Override
    public Collection<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        insert(ADD_FRIEND_QUERY,
                userId,
                friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        delete(DELETE_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public List<User> getFriends(long id) {
        return findMany(FIND_FRIENDS_QUERY, id);
    }

    @Override
    public List<User> getMutualFriends(long userId, long otherId) {
        return findMany(GET_MUTUAL_FRIENDS_QUERY, userId, otherId);
    }
}
