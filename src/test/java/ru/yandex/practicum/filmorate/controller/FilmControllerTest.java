package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    FilmController filmController = new FilmController();
    Film film;

    @BeforeEach
    void serializeFilmBefore(){
        film = new Film();
        film.setName("Интерстеллар");
        film.setDescription("Девятый полнометражный фильм режиссёра Кристофера Нолана");
        film.setReleaseDate(LocalDate.of(2014, 11, 6));
        film.setDuration(169);
    }

    @Test
    void testCreateExceptionNullName() {
        film.setName("");
        assertThrows(ResponseStatusException.class, () -> {
                    filmController.create(film);
                }, "Не отработало исключение при создании фильма с пустым именем");
    }

    @Test
    void testCreateMaxLenghtDescription() {
        film.setDescription(
                "Эпическая научная фантастика 2014 года, девятый полнометражный фильм режиссёра Кристофера Нолана," +
                " который написал сценарий в соавторстве со своим братом Джонатаном. Действие фильма" +
                " разворачивается в антиутопическом будущем, где Земля страдает от катастрофического упадка" +
                " и голода.");
        assertThrows(ResponseStatusException.class, () -> {
                    filmController.create(film);
                }, "Не отработало исключение при создании фильма с длинным описанием");
    }

    @Test
    void testCreateBeforeMovieBirhday(){
        film.setReleaseDate(LocalDate.of(1894, 11, 6));
        assertThrows(ResponseStatusException.class, () -> {
                    filmController.create(film);
                }, "Не отработало исключение при создании фильма дата релиза которого раньше" +
                        " 28 декабря 1895 года");
    }

    @Test
    void testCreatePositiveDuration(){
        film.setDuration(-169);
        assertThrows(ResponseStatusException.class, () -> {
                    filmController.create(film);
                }, "Не отработало исключение при создании фильма с отрицательной продолжительностью");

    }
}