package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dal.genre.InMemoryGenreStorage;
import ru.yandex.practicum.filmorate.dal.mpa.InMemoryMpaStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dal.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.dal.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmServiceTest {

    private FilmService filmService;
    private InMemoryFilmStorage filmStorage;
    private InMemoryUserStorage userStorage;
    private InMemoryGenreStorage genreStorage;
    private InMemoryMpaStorage mpaStorage;

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        genreStorage = new InMemoryGenreStorage();
        mpaStorage = new InMemoryMpaStorage();
        filmService = new FilmService(filmStorage, userStorage, genreStorage,mpaStorage);
    }

    @Test
    void testCreateExceptionNullName() {
        Film film = new Film();
        film.setName(" ");
        film.setDescription("Девятый полнометражный фильм режиссёра Кристофера Нолана");
        film.setReleaseDate(LocalDate.of(2014, 11, 6));
        film.setDuration(169);

        assertThrows(ValidationException.class, () -> {
            filmService.create(film);
        }, "Не отработало исключение при создании фильма с пустым именем");
    }

    @Test
    void testCreateMaxLenghtDescription() {
        Film film = new Film();
        film.setName(" ");
        film.setReleaseDate(LocalDate.of(2014, 11, 6));
        film.setDuration(169);
        film.setDescription(
                "Эпическая научная фантастика 2014 года, девятый полнометражный фильм режиссёра Кристофера Нолана," +
                        " который написал сценарий в соавторстве со своим братом Джонатаном. Действие фильма" +
                        " разворачивается в антиутопическом будущем, где Земля страдает от катастрофического упадка" +
                        " и голода.");
        assertThrows(ValidationException.class, () -> {
            filmService.create(film);
        }, "Не отработало исключение при создании фильма с длинным описанием");
    }

    @Test
    void testCreateBeforeMovieBirhday() {
        Film film = new Film();
        film.setName("Интерстеллар");
        film.setDescription("Девятый полнометражный фильм режиссёра Кристофера Нолана");
        film.setDuration(169);
        film.setReleaseDate(LocalDate.of(1894, 11, 6));
        assertThrows(ValidationException.class, () -> {
            filmService.create(film);
        }, "Не отработало исключение при создании фильма дата релиза которого раньше" +
                " 28 декабря 1895 года");
    }

    @Test
    void testCreatePositiveDuration() {
        Film film = new Film();
        film.setName("Интерстеллар");
        film.setDescription("Девятый полнометражный фильм режиссёра Кристофера Нолана");
        film.setReleaseDate(LocalDate.of(2014, 11, 6));
        film.setDuration(-169);
        assertThrows(ValidationException.class, () -> {
            filmService.create(film);
        }, "Не отработало исключение при создании фильма с отрицательной продолжительностью");
    }
}