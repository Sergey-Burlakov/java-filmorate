package ru.yandex.practicum.filmorate.dal.mpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Qualifier("inMemoryMpaStorage")
@Repository
public class InMemoryMpaStorage implements MpaStorage{
    private final Set<Mpa> mpaSet = new HashSet<>();

    public InMemoryMpaStorage(){
        mpaSet.add(new Mpa(1, "G"));
        mpaSet.add(new Mpa(2, "PG"));
        mpaSet.add(new Mpa(3, "PG-13"));
        mpaSet.add(new Mpa(4, "R"));
        mpaSet.add(new Mpa(5, "NC-17"));
    }

    @Override
    public Collection<Mpa> findAll() {
        return mpaSet;
    }

    @Override
    public Optional<Mpa> findById(int id) {
        return mpaSet.stream()
                .filter(mpa -> mpa.getId() == id)
                .findFirst();
    }
}
