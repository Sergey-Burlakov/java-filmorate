package ru.yandex.practicum.filmorate.dal.genre;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Qualifier("inMemoryGenreStorage")
@Repository
public class InMemoryGenreStorage implements GenreStorage {
    private final Set<Genre> genres = new HashSet<>();

    public InMemoryGenreStorage() {
        genres.add(new Genre(1, "Комедия"));
        genres.add(new Genre(2, "Драма"));
        genres.add(new Genre(3, "Мультфильм"));
        genres.add(new Genre(4, "Триллер"));
        genres.add(new Genre(5, "Документальный"));
        genres.add(new Genre(6, "Боевик"));
    }

    @Override
    public Collection<Genre> findAll() {
        return genres;
    }

    @Override
    public Optional<Genre> findById(int id) {
        return genres.stream()
                .filter(genre -> genre.getId() == id)
                .findFirst();
    }
}
