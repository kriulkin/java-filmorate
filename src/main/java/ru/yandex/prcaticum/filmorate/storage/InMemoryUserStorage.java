package ru.yandex.prcaticum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.prcaticum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    public Map<Integer, User> getUsers() { return users; }

    @Override
    public User get(Integer id) {
        return users.get(id);
    }

    @Override
    public User add(User user) {
        Integer userId = User.getCurrentId();
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public User update(User user) {
        Integer userId = user.getId();
        return users.replace(userId, user);

    }

    @Override
    public User delete(Integer id) {
        return users.remove(id);
    }
}
