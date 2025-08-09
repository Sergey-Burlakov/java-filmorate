package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private static final LocalDate MOVIE_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);
    private final FilmStorage storageFilm;
    private final UserStorage storageUser;

    public Collection<Film> findAll() {
        log.trace("Поступил запрос на вывод всех фильмов из хранилища");
        return storageFilm.findAll();
    }

    public Film create(Film film) {
        log.trace("Поступил запрос на добавление фильма");
        validateFilm(film);
        log.debug("Фильм name = «{}» прошел валидацию при создании", film.getName());
        return storageFilm.create(film);
    }

    public Film update(Film film) {
        log.trace("Поступил запрос на обновление фильма");
        if (film.getId() == null) {
            String message = "В id объекта передано null значение";
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        Film oldFilm = storageFilm.findById(film.getId());
        Film candidateFilm = new Film(oldFilm);

        if (film.getName() != null && (!film.getName().isBlank())) {
            candidateFilm.setName(film.getName());
            log.debug("FilmId = {} изменился параметр name. Новое значение «{}» старое значение «{}»",
                    film.getId(), film.getName(), oldFilm.getName());
        }
        if (film.getDescription() != null && (!film.getDescription().isBlank())) {
            candidateFilm.setDescription(film.getDescription());
            log.debug("FilmId = {} изменился параметр description. Новое значение «{}» старое значение «{}»",
                    film.getId(), film.getDescription(), oldFilm.getDescription());
        }
        if (film.getReleaseDate() != null && (!film.getReleaseDate().equals(oldFilm.getReleaseDate()))) {
            candidateFilm.setReleaseDate(film.getReleaseDate());
            log.debug("FilmId = {} изменился параметр ReleaseDate. Новое значение «{}» старое значение «{}»",
                    film.getId(), film.getReleaseDate(), oldFilm.getReleaseDate());
        }
        if ((film.getDuration() != 0) && (film.getDuration() != oldFilm.getDuration())) {
            candidateFilm.setDuration(film.getDuration());
            log.debug("FilmId = {} изменился параметр вuration. Новое значение «{}» старое значение «{}»",
                    film.getId(), film.getDuration(), oldFilm.getDuration());
        }

        validateFilm(candidateFilm);
        log.debug("Фильм id = {} прошел валидацию при обновлении", candidateFilm.getId());

        storageFilm.update(candidateFilm);
        log.info("Фильм Id = {}, Name = {} успешно обновлен", candidateFilm.getId(), candidateFilm.getName());
        return candidateFilm;
    }

    public void setLike(long filmId, long userId) {
        log.trace("Поступил запрос на установку лайка");
        chekFilmUserId(filmId, userId);
        log.debug("Значения  filmId = {}, userId = {} прошли проверку", filmId, userId);
        storageFilm.setLike(filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        log.trace("Поступил запрос на удаление лайка");
        chekFilmUserId(filmId, userId);
        log.debug("Значения  filmId = {}, userId = {} прошли проверку", filmId, userId);
        storageFilm.deleteLike(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        log.trace("Поступил запрос на вывод топ {} фильмов", count);

        Comparator<Film> byLike = new Comparator<Film>() {
            @Override
            public int compare(Film film1, Film film2) {
                return Integer.compare(storageFilm.getCountLikes(film2.getId()), storageFilm.getCountLikes(film1.getId()));
            }
        };

        if (count <= 0) {
            String message = "Количество не может быть равно нулю или отрицательным";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return storageFilm.findAll()
                .stream()
                .sorted(byLike)
                .limit(count)
                .toList();
    }

    private void chekFilmUserId(long filmId, long userId) {
        storageFilm.findById(filmId);
        storageUser.findById(userId);
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            String message = "название не может быть пустым";
            log.error(message);
            throw new ValidationException(message);
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            String message = "максимальная длина описания — 200 символов";
            log.error(message);
            throw new ValidationException(message);
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(MOVIE_BIRTHDAY)) {
            String message = "Дата релиза не может быть раньше 28 декабря 1895 года";
            log.error(message);
            throw new ValidationException(message);
        }
        if (film.getDuration() <= 0) {
            String message = "Продолжительность фильма должна быть положительным числом";
            log.error(message);
            throw new ValidationException(message);
        }
    }

}