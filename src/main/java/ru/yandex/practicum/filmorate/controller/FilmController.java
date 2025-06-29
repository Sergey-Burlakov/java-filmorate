package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final LocalDate MOVIE_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        try {
            validateFilm(film);
            log.info("Валидация фильма при создании" +
                    " Id = {} Name = {} прошла успешно.", film.getName(), film.getId());
        } catch (ValidationException e) {
            log.error("Ошибка валидации фильма при создании {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film filmWithUpdates) {

        if (filmWithUpdates.getId() == null) {
            String message = "id должен быть заполнен";
            log.warn(message);
            throw new ValidationException(message);
        }
        if (!films.containsKey(filmWithUpdates.getId())) {
            String message = String.format("Фильм Id = %d не найден\n",filmWithUpdates.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }

        Film oldFilm = films.get(filmWithUpdates.getId());
        Film candidateFilm = new Film(oldFilm);

        if (filmWithUpdates.getName() != null && (!filmWithUpdates.getName().isBlank())){
            candidateFilm.setName(filmWithUpdates.getName());
        }
        if (filmWithUpdates.getDescription() != null && (!filmWithUpdates.getDescription().isBlank())){
            candidateFilm.setDescription(filmWithUpdates.getDescription());
        }
        if (filmWithUpdates.getReleaseDate() != null ){
            candidateFilm.setReleaseDate(filmWithUpdates.getReleaseDate());
        }
        if ((filmWithUpdates.getDuration() != 0)){
            candidateFilm.setDuration(filmWithUpdates.getDuration());
        }

        try {
            validateFilm(candidateFilm);
            log.info("Валидация фильма при обновлении" +
                    " Id = {} Name = {} прошла успешно.", candidateFilm.getName(), candidateFilm.getId());
        } catch (ValidationException e) {
            log.error("Ошибка валидации фильма при обновлении {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        films.put(candidateFilm.getId(), candidateFilm);
        log.info("Фильм Id = {}, Name = {} успешно обновлен", candidateFilm.getId(), candidateFilm.getName());
        return candidateFilm;
    }


    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("название не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(MOVIE_BIRTHDAY)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
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
