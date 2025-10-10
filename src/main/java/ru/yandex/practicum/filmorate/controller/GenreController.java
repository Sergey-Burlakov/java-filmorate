package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<Genre> findAll() {
        return service.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Genre findById(@PathVariable("id") int id) {
        return service.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Жанр с ID = %d не найден", id)));
    }
}
