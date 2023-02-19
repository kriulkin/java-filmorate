package ru.yandex.prcaticum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.prcaticum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private int currentId;
    private final Map<Integer, User> users = new HashMap<>();
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    private int getCurrentId() { return ++currentId; }

    @Override
    public User add(User user) {
        Integer userId = getCurrentId();
        user.setId(userId);
        users.put(userId, user);
        return user;
    }
    @Override
    public User update(User user) {
        Integer userId = user.getId();
        return users.replace(userId, user) != null ? user : null;
    }

    @Override
    public User delete(Integer id) {
        return users.remove(id);
    }

    @Override
    public User get(Integer id) {
        return users.get(id);
    }
}
