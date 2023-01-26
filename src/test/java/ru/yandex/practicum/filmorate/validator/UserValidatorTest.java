package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Test;
import ru.yandex.prcaticum.filmorate.exception.ValidationException;
import ru.yandex.prcaticum.filmorate.model.User;
import ru.yandex.prcaticum.filmorate.validator.UserValidator;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTest {
    @Test
    void testEmptyUser() {
    User user = User.builder()
            .build();

        assertThrows(ValidationException.class, () -> UserValidator.validate(user),
                "Некорректно обрабатывается пустой юзер");
    }

    @Test
    void testEmailWithoutAt() {
        User user = User.builder()
                .email("test")
                .name("test")
                .login("test")
                .birthday("2000-01-01")
                .build();

        assertThrows(ValidationException.class, () -> UserValidator.validate(user),
                "Некорректно обрабатывается юзер c неправильным email");
    }

    @Test
    void testBlankEmail() {
        User user = User.builder()
                .email("")
                .name("test")
                .login("test")
                .birthday("2000-01-01")
                .build();

        assertThrows(ValidationException.class, () -> UserValidator.validate(user),
                "Некорректно обрабатывается юзер c пустым email");
    }

    @Test
    void testLoginWithSpace() {
        User user = User.builder()
                .email("test@mail.ru")
                .name("test")
                .login("test test")
                .birthday("2000-01-01")
                .build();

        assertThrows(ValidationException.class, () -> UserValidator.validate(user),
                "Некорректно обрабатывается юзер c неправильным login");
    }

    @Test
    void testBlankLogin() {
        User user = User.builder()
                .email("test@mail.ru")
                .name("test")
                .login("")
                .birthday("2000-01-01")
                .build();

        assertThrows(ValidationException.class, () -> UserValidator.validate(user),
                "Некорректно обрабатывается юзер c пустым login");
    }

    @Test
    void testBlankName() throws ValidationException, ParseException {
        User user = User.builder()
                .email("test@mail.ru")
                .name("")
                .login("login")
                .birthday("2000-01-01")
                .build();

        UserValidator.validate(user);

        assertEquals(user.getLogin(), user.getName(),
        "Некорректно обрабатывается юзер c пустым name");
    }

    @Test
    void testBirthdayAfterNow() {
        User user = User.builder()
                .email("test@mail.ru")
                .name("test")
                .login("")
                .birthday("2025-01-01")
                .build();

        assertThrows(ValidationException.class, () -> UserValidator.validate(user),
                "Некорректно обрабатывается юзер c датой рождения в будущем");
    }

    @Test
    void testValidUser() {
        User user = User.builder()
                .email("test@mail.ru")
                .name("test")
                .login("test")
                .birthday("2000-01-01")
                .build();

        assertDoesNotThrow(() -> UserValidator.validate(user),
                "Некорректно обрабатывается валидный юзер");
    }
}
