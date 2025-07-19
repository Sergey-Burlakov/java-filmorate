package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<User> findAll() {
        return service.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User create(@RequestBody User user) {
        return service.create(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(
            @PathVariable("id") long userId,
            @PathVariable("friendId") long friendId) {
        service.addFriend(userId, friendId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable("id") long userId,
            @PathVariable("friendId") long friendId) {
        service.deleteFriend(userId, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") long id) {
        return service.getFriends(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(
            @PathVariable("id") long userId,
            @PathVariable("otherId") long otherId) {
        return service.getMutualFriends(userId, otherId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public User update(@RequestBody User user) {
        return service.update(user);
    }

}
