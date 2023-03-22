package ru.yandex.prcaticum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.prcaticum.filmorate.exception.NoSuchEntityException;
import ru.yandex.prcaticum.filmorate.model.User;
import ru.yandex.prcaticum.filmorate.storage.InDbFriendStorage;
import ru.yandex.prcaticum.filmorate.storage.UserStorage;
import ru.yandex.prcaticum.filmorate.validator.UserValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final InDbFriendStorage friendStorage;

    public UserService(@Qualifier("inMemoryUserStorage") UserStorage userStorage, InDbFriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

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
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user1 = getUserById(userId);
        User user2 = getUserById(friendId);
        if (!friendStorage.getFriendsList(userId).contains(friendId)) {
            throw new NoSuchEntityException(String.format("Пользователя с id %d нет в списке друзей", friendId));
        }
        friendStorage.deleteFriend(userId, friendId);
    }


    public List<User> getFriendsList(Integer userId) {
        User user = getUserById(userId);
        return friendStorage.getFriendsList(userId)
                .stream()
                .map(userStorage::get)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        User user = getUserById(userId);
        User otherUser = getUserById(otherId);
        return friendStorage.getFriendsList(userId).stream()
                .filter(id -> friendStorage.getFriendsList(otherId).contains(id))
                .map(userStorage::get)
                .collect(Collectors.toList());
    }
}
