package ru.yandex.prcaticum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.prcaticum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.prcaticum.filmorate.exception.ValidationException;
import ru.yandex.prcaticum.filmorate.model.User;
import ru.yandex.prcaticum.filmorate.validator.UserValidator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        log.debug("Текущее количество юзеров {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException, ParseException {
        log.debug(user.toString());
        UserValidator.validate(user);
        user.setId(User.getCurrentId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException, NoSuchUserIdException, ParseException {
        log.debug(user.toString());
        UserValidator.validate(user);
        int userId = user.getId();

        if (!users.containsKey(userId)) {
            throw new NoSuchUserIdException();
        }
        users.put(userId, user);
        return user;
    }
}
