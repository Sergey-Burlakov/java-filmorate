package ru.yandex.practicum.filmorate.dal.film;

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

    @Override
    public List<Film> getPopularFilms(int count) {

        Comparator<Film> byLike = new Comparator<Film>() {
            @Override
            public int compare(Film film1, Film film2) {
                return Integer.compare(getCountLikes(film2.getId()), getCountLikes(film1.getId()));
            }
        };

        return findAll()
                .stream()
                .sorted(byLike)
                .limit(count)
                .toList();
    }

    public Optional<Film> findById(Long id) {
        return Optional.ofNullable(films.get(id));
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
