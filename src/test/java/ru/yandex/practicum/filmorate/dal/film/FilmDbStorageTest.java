package ru.yandex.practicum.filmorate.dal.film;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmDbStorage.class, FilmRowMapper.class, GenreRowMapper.class, MpaRowMapper.class})
class FilmDbStorageTest {
    private final FilmDbStorage storage;

    @Autowired
    public FilmDbStorageTest(FilmDbStorage storage) {
        this.storage = storage;
    }

    @Test
    public void testFindById() {
        Film film = new Film();
        film.setName("Интерстеллар");
        film.setDescription("Девятый полнометражный фильм режиссёра Кристофера Нолана");
        film.setReleaseDate(LocalDate.of(2014, 11, 6));
        film.setDuration(169);
        Mpa mpa = new Mpa(1, "G");
        film.setMpa(mpa);
        film.setGenres(new LinkedHashSet<>());
        storage.create(film);

        Optional<Film> filmOptional = storage.findById(film.getId());
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(foundFilm ->
                        assertThat(foundFilm)
                                .hasFieldOrPropertyWithValue("id", film.getId())
                                .hasFieldOrPropertyWithValue("name", "Интерстеллар")
                                .hasFieldOrPropertyWithValue("description", "Девятый полнометражный " +
                                        "фильм режиссёра Кристофера Нолана")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2014, 11,
                                        6))
                                .hasFieldOrPropertyWithValue("duration", 169)

                );

    }

    @Test
    void testCreate() {
        Film originalFilm = new Film();
        originalFilm.setName("Интерстеллар");
        originalFilm.setDescription("Девятый полнометражный фильм режиссёра Кристофера Нолана");
        originalFilm.setReleaseDate(LocalDate.of(2014, 11, 6));
        originalFilm.setDuration(169);
        Mpa mpa = new Mpa(1, "G");
        originalFilm.setMpa(mpa);
        originalFilm.setGenres(new LinkedHashSet<>());
        storage.create(originalFilm);

        assertThat(originalFilm)
                .as("Метод create не должен возвращать null")
                .isNotNull();

        assertThat(originalFilm.getId())
                .as("Методу create должен быть присвоен ID базой данных")
                .isNotNull();

        Optional<Film> filmFromDbOptional = storage.findById(originalFilm.getId());

        assertThat(filmFromDbOptional)
                .as("Сохраненный фильм должен находиться в базе по своему ID")
                .isPresent();

        Film filmFromDb = filmFromDbOptional.get();

        assertThat(filmFromDb.getName())
                .as("Название сохраненного фильма должен совпадать с оригиналом")
                .isEqualTo(originalFilm.getName());

        assertThat(filmFromDb.getDescription())
                .as("Описание сохраненного фильма должно совпадать с оригиналом")
                .isEqualTo(originalFilm.getDescription());

        assertThat(filmFromDb.getReleaseDate())
                .as("Дата релиза сохраненного фильма должно совпадать с оригиналом")
                .isEqualTo(originalFilm.getReleaseDate());

        assertThat(filmFromDb.getDuration())
                .as("Продолжительность сохраненного фильма должна совпадать с оригиналом")
                .isEqualTo(originalFilm.getDuration());
    }

    @Test
    void testUpdate() {
        Film originalFilm = new Film();
        originalFilm.setName("Интерстеллар");
        originalFilm.setDescription("Девятый полнометражный фильм режиссёра Кристофера Нолана");
        originalFilm.setReleaseDate(LocalDate.of(2014, 11, 6));
        originalFilm.setDuration(169);
        Mpa mpa = new Mpa(1, "G");
        originalFilm.setMpa(mpa);
        originalFilm.setGenres(new LinkedHashSet<>());
        storage.create(originalFilm);

        Film updatedInfo = new Film();
        updatedInfo.setName("Начало");
        updatedInfo.setDescription("Научно-фантастический триллер Кристофера Нолана о внедрении идей во сне");
        updatedInfo.setReleaseDate(LocalDate.of(2010, 7, 22));
        updatedInfo.setDuration(148);
        Mpa updatedMpa = new Mpa(1, "PG");
        updatedInfo.setMpa(updatedMpa);
        updatedInfo.setGenres(new LinkedHashSet<>());
        storage.create(updatedInfo);

        Film filmFromDb = storage.findById(updatedInfo.getId()).get();

        assertThat(filmFromDb)
                .as("Метод update не должен возвращать null")
                .isNotNull();

        assertThat(filmFromDb.getName())
                .as("некорректно обновилось имя фильма")
                .isEqualTo(updatedInfo.getName());

        assertThat(filmFromDb.getDescription())
                .as("некорректно обновилось описание фильма")
                .isEqualTo(updatedInfo.getDescription());

        assertThat(filmFromDb.getReleaseDate())
                .as("некорректно обновилась дата релиза фильма")
                .isEqualTo(updatedInfo.getReleaseDate());

        assertThat(filmFromDb.getDuration())
                .as("некорректно обновилась продолжительность фильма")
                .isEqualTo(updatedInfo.getDuration());
    }

    @Test
    void testFindAll() {
        Film film1 = new Film();
        film1.setName("Интерстеллар");
        film1.setDescription("Девятый полнометражный фильм режиссёра Кристофера Нолана");
        film1.setReleaseDate(LocalDate.of(2014, 11, 6));
        film1.setDuration(169);
        Mpa mpa = new Mpa(1, "G");
        film1.setMpa(mpa);
        film1.setGenres(new LinkedHashSet<>());
        storage.create(film1);

        Film film2 = new Film();
        film2.setName("Начало");
        film2.setDescription("Научно-фантастический триллер Кристофера Нолана о внедрении идей во сне");
        film2.setReleaseDate(LocalDate.of(2010, 7, 22));
        film2.setDuration(148);
        Mpa updatedMpa = new Mpa(1, "PG");
        film2.setMpa(updatedMpa);
        film2.setGenres(new LinkedHashSet<>());
        storage.create(film2);

        Collection<Film> films = storage.findAll();

        assertThat(films)
                .as("Метод findAll не должен возвращать null")
                .isNotNull();

        assertThat(films.size())
                .as("Метод findAll некорректно возвращает пользователей")
                .isEqualTo(2);

    }


}