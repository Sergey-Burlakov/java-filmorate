package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    private final Map<Long, Set<Long>> usersLikes = new HashMap<>();

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }

    public Film findById(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new NotFoundException("Id не найден");
    }

    public void setLike(long filmId, long userId) {
        usersLikes.computeIfAbsent(filmId, k -> new HashSet<>()).add(userId);
    }

    public void deleteLike(long filmId, long userId) {
        if (usersLikes.get(filmId) != null) {
            usersLikes.get(filmId).remove(userId);
        }
    }

    public int getCountLikes(long filmId) {
        if (usersLikes.get(filmId) == null) {
            return 0;
        }
        return usersLikes.get(filmId).size();
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
