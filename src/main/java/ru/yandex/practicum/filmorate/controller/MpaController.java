package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaServise;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaServise service;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<Mpa> findAll() {
        return service.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Mpa findById(@PathVariable("id") int id) {
        return service.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Рейтинг с ID = %d не найден", id)));
    }
}
