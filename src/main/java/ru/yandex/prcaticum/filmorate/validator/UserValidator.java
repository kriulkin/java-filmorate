package ru.yandex.prcaticum.filmorate.validator;

import ru.yandex.prcaticum.filmorate.exception.ValidationException;
import ru.yandex.prcaticum.filmorate.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.Instant;

public class UserValidator {
    public static void validate(User user) {
        if (user.getEmail() == null ||
                !user.getEmail().contains("@")
        ) {
            throw new ValidationException("Пустой идентификатор почтового ящика");
        }

        if (user.getLogin() == null ||
                user.getLogin().isBlank() ||
                user.getLogin().contains(" ")
        ) {
            throw new ValidationException("Пустое имя пользователя");
        }

        try {
            if (user.getBirthday() == null ||
                    new SimpleDateFormat("yyyy-MM-dd").parse(user.getBirthday()).after(Date.from(Instant.now()))
            ) {
                throw new ValidationException("Дата рождения в будущем");
            }
        } catch (ParseException e) {
            throw new ValidationException("Некорректная дата рождения");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
