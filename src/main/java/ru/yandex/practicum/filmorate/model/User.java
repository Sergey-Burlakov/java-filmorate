package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    // Конструктор копирования
    public User(User other) {
        this.id = other.id;
        this.name = other.name;
        this.login = other.login;
        this.email = other.email;
        this.birthday = other.birthday;
    }
}
