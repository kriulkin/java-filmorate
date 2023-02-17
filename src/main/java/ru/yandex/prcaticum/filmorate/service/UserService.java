package ru.yandex.prcaticum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.prcaticum.filmorate.exception.NoSuchFriendIdException;
import ru.yandex.prcaticum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.prcaticum.filmorate.exception.ValidationException;
import ru.yandex.prcaticum.filmorate.model.User;
import ru.yandex.prcaticum.filmorate.storage.UserStorage;
import ru.yandex.prcaticum.filmorate.validator.UserValidator;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return List.copyOf(userStorage.getUsers().values());
    }

    public User create(User user) throws ValidationException, ParseException {
        UserValidator.validate(user);
        return userStorage.add(user);
    }

    public User update(User user) throws ValidationException, ParseException, NoSuchUserIdException {
        UserValidator.validate(user);

        if (userStorage.update(user) == null) {
            throw new NoSuchUserIdException(String.format("Пользователя с id %d не существует", user.getId()));
        }

        return userStorage.update(user);
    }

    public User getUserById(Integer userId) throws NoSuchUserIdException {
        if (userStorage.get(userId) == null) {
            throw new NoSuchUserIdException(String.format("Пользователя с id %d не существует", userId));
        }

        return userStorage.get(userId);
    }

    public void addFriend(Integer userId, Integer friendId) throws NoSuchUserIdException {
        User user1 = checkUserId(userId);
        User user2 = checkUserId(friendId);
        user1.getFriends().add(friendId);
        user2.getFriends().add(userId);
    }

    public void deleteFriend(Integer userId, Integer friendId) throws NoSuchUserIdException, NoSuchFriendIdException {
        User user1 = checkUserId(userId);
        User user2 = checkUserId(friendId);
        if (!user1.getFriends().remove(friendId)) {
            throw new NoSuchFriendIdException(String.format("Пользователя с id %d нет в списке друзей", friendId));
        }
        user2.getFriends().remove(userId);
    }

    public User checkUserId(Integer userId) throws NoSuchUserIdException {
        User user = userStorage.get(userId);
        if (user == null) {
            throw new NoSuchUserIdException(String.format("Пользователя с id %d не существует", userId));
        }
        return user;
    }

    public List<User> getFriendsList(Integer userId) throws NoSuchUserIdException {
        User user = checkUserId(userId);
        return user.getFriends().stream()
                .map(id -> userStorage.get(id))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) throws NoSuchUserIdException {
        User user = checkUserId(userId);
        User otherUser = checkUserId(otherId);
        return user.getFriends().stream()
                .filter(id -> otherUser.getFriends().contains(id))
                .map(id -> userStorage.get(id))
                .collect(Collectors.toList());
    }
}
