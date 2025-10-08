package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class MpaServise {
    private final MpaStorage storage;

    public MpaServise(@Qualifier("mpaDbStorage") MpaStorage storage){
        this.storage = storage;
    }

    public Collection<Mpa> findAll(){
        log.trace("Поступил запрос на вывод всех рейтингов из хранилища");
        return storage.findAll();
    }

    public Optional<Mpa> findById(int id){
        log.trace("Поступил запрос на получение рейтинга ID = «{}»", id);
        return storage.findById(id);
    }
}
