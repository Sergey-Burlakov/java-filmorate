package ru.yandex.practicum.filmorate.dal.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film create(Film film);

    Collection<Film> findAll();

    Optional<Film> findById(Long id);

    Film update(Film film);

    void setLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    int getCountLikes(long filmId);

    List<Film> getPopularFilms(int count);
}
