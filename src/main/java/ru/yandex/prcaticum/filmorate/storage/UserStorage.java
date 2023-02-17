package ru.yandex.prcaticum.filmorate.storage;

import ru.yandex.prcaticum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getUsers();

    User get(Integer id);

    User add(User user);

    User update(User user);

    User delete(Integer id);
}
