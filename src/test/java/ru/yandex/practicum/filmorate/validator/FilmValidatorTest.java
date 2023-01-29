package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Test;
import ru.yandex.prcaticum.filmorate.exception.ValidationException;
import ru.yandex.prcaticum.filmorate.model.Film;
import ru.yandex.prcaticum.filmorate.validator.FilmValidator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidatorTest {
    Film film;
    Film.FilmBuilder filmBuilder = Film.builder();

    @Test
     void testEmptyFilm() {
        film = filmBuilder
                .build();

        assertThrows(ValidationException.class, () -> FilmValidator.validate(film),
                "Некорректно обрабатывается пустой фильм");
    }

    @Test
    void testBlankName() {
        film = filmBuilder
                .name("")
                .description("фильм с пустым названием")
                .releaseDate("2015-01-01")
                .duration(1)
                .build();

        assertThrows(ValidationException.class, () -> FilmValidator.validate(film),
                "Некорректно обрабатывается фильм с пустым названием");
    }

    @Test
    void test200CharsDescription() {
        String description = "a".repeat(200);

        film = filmBuilder
                .name("фильм 200 символов")
                .description(description)
                .releaseDate("2015-01-01")
                .duration(1)
                .build();

        assertDoesNotThrow(() -> FilmValidator.validate(film),
                "Некорректно обрабатывается фильм с описанием 200 символов");
    }

    @Test
    void test201CharsDescription() {
        String description = "a".repeat(201);

        film = filmBuilder
                .name("фильм 201 символ")
                .description(description)
                .releaseDate("2015-01-01")
                .duration(1)
                .build();

        assertThrows(ValidationException.class, () -> FilmValidator.validate(film),
                "Некорректно обрабатывается фильм с описанием 200 символов");
    }

    @Test
    void testReleaseDateBeforeMinDate() {
        film = filmBuilder
                .name("27 декабря 1895 года")
                .description("27 декабря 1895 года")
                .releaseDate("1895-12-27")
                .duration(1)
                .build();

        assertThrows(ValidationException.class, () -> FilmValidator.validate(film),
                "Некорректно обрабатывается фильм 1895-12-27");
    }

    @Test
    void testReleaseDateMinDate() {
        film = filmBuilder
                .name("28 декабря 1895 года")
                .description("28 декабря 1895 года")
                .releaseDate("1895-12-28")
                .duration(1)
                .build();

        assertDoesNotThrow(() -> FilmValidator.validate(film),
                "Некорректно обрабатывается фильм 1895-12-28");
    }

    @Test
    void testZeroDurationFilm() {
        film = filmBuilder
                .name("фильм 0 минут")
                .description("фильм 0 минут")
                .releaseDate("2015-01-01")
                .duration(0)
                .build();

        assertThrows(ValidationException.class, () -> FilmValidator.validate(film),
                "Некорректно обрабатывается фильм 0 минут");
    }

    @Test
    void testDurationFilm () {
        film = filmBuilder
                .name("фильм 1 минут")
                .description("фильм 0 минут")
                .releaseDate("2015-01-01")
                .duration(1)
                .build();

        assertDoesNotThrow(() -> FilmValidator.validate(film),
                "Некорректно обрабатывается фильм 1 минут");
    }
}
