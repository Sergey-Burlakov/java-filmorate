package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film create(Film film);

    Collection<Film> findAll();

    Film findById(Long id);

    Film update(Film film);

    void setLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    int getCountLikes(long filmId);
}
