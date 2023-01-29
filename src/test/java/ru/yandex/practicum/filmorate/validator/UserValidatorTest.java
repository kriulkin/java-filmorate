package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Test;
import ru.yandex.prcaticum.filmorate.exception.ValidationException;
import ru.yandex.prcaticum.filmorate.model.User;
import ru.yandex.prcaticum.filmorate.validator.UserValidator;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTest {
    User user;
    User.UserBuilder userBuilder = User.builder();

    @Test
    void testEmptyUser() {
    user = userBuilder.build();

        assertThrows(ValidationException.class, () -> UserValidator.validate(user),
                "Некорректно обрабатывается пустой пользователь");
    }

    @Test
    void testEmailWithoutAt() {
        user = userBuilder
                .email("test")
                .name("test")
                .login("test")
                .birthday("2000-01-01")
                .build();

        assertThrows(ValidationException.class, () -> UserValidator.validate(user),
                "Некорректно обрабатывается юзер c пользователь email");
    }

    @Test
    void testBlankEmail() {
        user = userBuilder
                .email("")
                .name("test")
                .login("test")
                .birthday("2000-01-01")
                .build();

        assertThrows(ValidationException.class, () -> UserValidator.validate(user),
                "Некорректно обрабатывается пользователь c пустым email");
    }

    @Test
    void testLoginWithSpace() {
        user = userBuilder
                .email("test@mail.ru")
                .name("test")
                .login("test test")
                .birthday("2000-01-01")
                .build();

        assertThrows(ValidationException.class, () -> UserValidator.validate(user),
                "Некорректно обрабатывается пользователь c неправильным login");
    }

    @Test
    void testBlankLogin() {
        user = userBuilder
                .email("test@mail.ru")
                .name("test")
                .login("")
                .birthday("2000-01-01")
                .build();

        assertThrows(ValidationException.class, () -> UserValidator.validate(user),
                "Некорректно обрабатывается пользователь c пустым login");
    }

    @Test
    void testBlankName() throws ValidationException, ParseException {
        user = userBuilder
                .email("test@mail.ru")
                .name("")
                .login("login")
                .birthday("2000-01-01")
                .build();

        UserValidator.validate(user);

        assertEquals(user.getLogin(), user.getName(),
        "Некорректно обрабатывается пользователь c пустым name");
    }

    @Test
    void testBirthdayAfterNow() {
        user = userBuilder
                .email("test@mail.ru")
                .name("test")
                .login("")
                .birthday("2025-01-01")
                .build();

        assertThrows(ValidationException.class, () -> UserValidator.validate(user),
                "Некорректно обрабатывается пользователь c датой рождения в будущем");
    }

    @Test
    void testValidUser() {
        user = userBuilder
                .email("test@mail.ru")
                .name("test")
                .login("test")
                .birthday("2000-01-01")
                .build();

        assertDoesNotThrow(() -> UserValidator.validate(user),
                "Некорректно обрабатывается валидный пользователь");
    }
}
