package ru.yandex.prcaticum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.prcaticum.filmorate.exception.NoSuchEntityException;
import ru.yandex.prcaticum.filmorate.model.User;
import ru.yandex.prcaticum.filmorate.storage.UserStorage;
import ru.yandex.prcaticum.filmorate.validator.UserValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        UserValidator.validate(user);
        return userStorage.add(user);
    }

    public User update(User user) {
        UserValidator.validate(user);

        if (userStorage.update(user) == null) {
            throw new NoSuchEntityException(String.format("Пользователя с id %d не существует", user.getId()));
        }

        return user;
    }

    public User getUserById(Integer userId) {
        if (userStorage.get(userId) == null) {
            throw new NoSuchEntityException(String.format("Пользователя с id %d не существует", userId));
        }

        return userStorage.get(userId);
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user1 = getUserById(userId);
        User user2 = getUserById(friendId);
        user1.getFriends().add(friendId);
        user2.getFriends().add(userId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user1 = getUserById(userId);
        User user2 = getUserById(friendId);
        if (!user1.getFriends().remove(friendId)) {
            throw new NoSuchEntityException(String.format("Пользователя с id %d нет в списке друзей", friendId));
        }
        user2.getFriends().remove(userId);
    }


    public List<User> getFriendsList(Integer userId) {
        User user = getUserById(userId);
        return user.getFriends().stream()
                .map(userStorage::get)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        User user = getUserById(userId);
        User otherUser = getUserById(otherId);
        return user.getFriends().stream()
                .filter(id -> otherUser.getFriends().contains(id))
                .map(userStorage::get)
                .collect(Collectors.toList());
    }
}
