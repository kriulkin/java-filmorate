package ru.yandex.prcaticum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.prcaticum.filmorate.model.User;
import ru.yandex.prcaticum.filmorate.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Текущее количество юзеров {}", userService.findAll().size());
        return userService.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.debug(user.toString());
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.debug(user.toString());
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(
            @PathVariable("id") Integer userId,
            @PathVariable("friendId") Integer friendId
    ) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable("id") Integer userId,
            @PathVariable("friendId") Integer friendId
    ) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsList(@PathVariable("id") Integer userId) {
        return userService.getFriendsList(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(
        @PathVariable("id") Integer userId,
        @PathVariable("otherId") Integer friendId
    ) {
        return userService.getCommonFriends(userId, friendId);
    }
}
