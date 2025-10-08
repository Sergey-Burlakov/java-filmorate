package ru.yandex.practicum.filmorate.dal.mpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("mpaDbStorage")
public class MpaDbStorage extends BaseRepository<Mpa> implements MpaStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";

    public MpaDbStorage(JdbcTemplate jdbc, MpaRowMapper mapper){
        super(jdbc, mapper, Mpa.class);
    }

    @Override
    public Collection<Mpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Mpa> findById(int id) {
        return findOne(
                FIND_BY_ID_QUERY,
                id
        );
    }
}
