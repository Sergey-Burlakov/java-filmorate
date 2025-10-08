package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Mpa mpa;
    Set<Genre> genres = new LinkedHashSet<>();

    // Конструктор копирования
    public Film(Film other) {
        this.id = other.id;
        this.name = other.name;
        this.description = other.description;
        this.releaseDate = other.releaseDate;
        this.duration = other.duration;
        this.mpa = other.mpa;
        this.genres = new LinkedHashSet<>(other.genres);
    }
}
