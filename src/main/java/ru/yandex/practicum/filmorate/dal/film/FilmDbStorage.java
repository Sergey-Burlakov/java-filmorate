package ru.yandex.practicum.filmorate.dal.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Repository
@Qualifier("filmDbStorage")
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    //добавление
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO film_genres(film_id, genre_id) VALUES(?, ?)";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO film_likes(id_film, id_user) VALUES(?, ?)";
    //удаление
    private static final String DELETE_FILM_GENRES_QUERY = "DELETE FROM film_genres WHERE film_id = ?";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM film_likes WHERE film_id = ? AND id_user = ?";
    //обновление
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?  WHERE id = ?";
    //поиск
    private static final String FIND_BY_ID_QUERY = "SELECT f.*, m.name AS mpa_name FROM films AS f JOIN mpa AS m ON f.mpa_id = m.id WHERE f.id = ?";
    private static final String FIND_ALL_QUERY = "SELECT f.*, m.name AS mpa_name FROM films AS f JOIN mpa AS m ON f.mpa_id = m.id";
    private static final String FIND_GENRES_BY_FILM = "SELECT * FROM   genres AS g JOIN film_genres AS fg ON g.id = fg.genre_id WHERE  fg.film_id = ? ORDER BY g.id ASC";
    private static final String GET_TOP_POPULAR_QUERY = "SELECT f.*, m.name AS mpa_name FROM films AS f JOIN mpa AS m ON f.mpa_id = m.id LEFT JOIN( SELECT film_id, COUNT(id_user) AS likes_count FROM film_likes GROUP BY film_id ) AS likes_rating ON f.id = likes_rating.film_id ORDER BY likes_rating.likes_count DESC LIMIT ?";

    private static final String COUNT_LIKES_QUERY = "COUNT * FROM film_genres WHERE film_id = ?";

    private final GenreRowMapper genreRowMapper;

    public FilmDbStorage(JdbcTemplate jdbc, FilmRowMapper mapper, GenreRowMapper genreRowMapper) {
        super(jdbc, mapper, Film.class);
        this.genreRowMapper = genreRowMapper;
    }

    @Override
    public Film create(Film film) {
        long id = insertAndReturnId(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);

        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            for (Genre g : genres) {
                addFilmGenres(id, g.getId());
            }
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        delete(DELETE_FILM_GENRES_QUERY, film.getId());
        Set<Genre> genres = film.getGenres();

        if (genres != null) {
            for (Genre g : genres) {
                addFilmGenres(film.getId(), g.getId());
            }
        }

        return findById(film.getId()).orElseThrow(() ->
                new NotFoundException(String.format("Фильм id = %d при обновлении не найден", film.getId())));
    }


    @Override
    public Collection<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Film> findById(Long id) {

        Optional<Film> filmOptional = findOne(FIND_BY_ID_QUERY, id);
        if (filmOptional.isPresent()) {
            Film film = filmOptional.get();
            film.setGenres(new HashSet<>(jdbc.query(
                    FIND_GENRES_BY_FILM,
                    genreRowMapper,
                    film.getId()
            )));
            return Optional.of(film);
        }
        return Optional.empty();
    }

    @Override
    public void setLike(long filmId, long userId) {
        insert(
                INSERT_LIKE_QUERY,
                filmId,
                userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        delete(
                DELETE_LIKE_QUERY,
                filmId,
                userId);
    }

    @Override
    public int getCountLikes(long filmId) {
        Optional<Integer> optionalCount = getCount(
                COUNT_LIKES_QUERY,
                filmId
        );
        return optionalCount.orElse(0);
    }

    @Override
    public List<Film> getPopularFilms(int count){
       return findMany(
                GET_TOP_POPULAR_QUERY,
                count
        );
    }

    private void addFilmGenres(long filmId, int genreId) {
        insert(
                INSERT_FILM_GENRES_QUERY,
                filmId,
                genreId
        );
    }
}
