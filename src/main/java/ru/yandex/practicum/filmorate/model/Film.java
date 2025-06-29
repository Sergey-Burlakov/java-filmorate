package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    // Конструктор копирования
    public Film(Film other) {
        this.id = other.id;
        this.name = other.name;
        this.description = other.description;
        this.releaseDate = other.releaseDate;
        this.duration = other.duration;
    }
}
