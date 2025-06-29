package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
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
