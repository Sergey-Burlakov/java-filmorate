package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.genre.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class GenreService {
    private final GenreStorage storage;

    public GenreService(@Qualifier("genreDbStorage") GenreStorage storage) {
        this.storage = storage;
    }

    public Collection<Genre> findAll() {
        log.trace("Поступил запрос на вывод всех жанров из хранилища");
        return storage.findAll();
    }

    public Optional<Genre> findById(int id) {
        log.trace("Поступил запрос на получение жанра ID = «{}»", id);
        return storage.findById(id);
    }
}
