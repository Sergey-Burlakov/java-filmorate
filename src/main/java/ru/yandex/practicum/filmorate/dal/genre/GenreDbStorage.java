package ru.yandex.practicum.filmorate.dal.genre;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Repository
@Qualifier("genreDbStorage")
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, GenreRowMapper mapper) {
        super(jdbc, mapper, Genre.class);
    }

    @Override
    public Collection<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Genre> findById(int id) {
        return findOne(
                FIND_BY_ID_QUERY,
                id
        );
    }
}
