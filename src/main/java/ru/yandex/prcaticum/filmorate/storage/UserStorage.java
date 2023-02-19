package ru.yandex.prcaticum.filmorate.storage;

import ru.yandex.prcaticum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAll();

    User get(Integer id);

    User add(User user);

    User update(User user);

    User delete(Integer id);
}
