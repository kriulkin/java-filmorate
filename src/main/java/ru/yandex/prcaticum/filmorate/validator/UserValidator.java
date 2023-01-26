package ru.yandex.prcaticum.filmorate.validator;

import ru.yandex.prcaticum.filmorate.exception.ValidationException;
import ru.yandex.prcaticum.filmorate.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.Instant;

public class UserValidator {
    public static void validate(User user) throws ValidationException, ParseException {
        boolean isValid = true;
        if (user.getEmail() == null
                || !user.getEmail().contains("@")) {
            isValid = false;
        } else if (user.getLogin() == null
                || user.getLogin().isBlank()
                || user.getLogin().contains(" ")){
            isValid = false;
        } else if (user.getBirthday() == null
                || new SimpleDateFormat("yyyy-MM-dd").parse(user.getBirthday())
                .after(Date.from(Instant.now()))) {
            isValid = false;
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (!isValid) {
            throw new ValidationException();
        }
    }
}
