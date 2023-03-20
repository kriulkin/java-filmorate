package ru.yandex.prcaticum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.prcaticum.filmorate.model.Film;
import ru.yandex.prcaticum.filmorate.model.Genre;
import ru.yandex.prcaticum.filmorate.model.Mpa;
import ru.yandex.prcaticum.filmorate.model.User;
import ru.yandex.prcaticum.filmorate.storage.InDbFilmStorage;
import ru.yandex.prcaticum.filmorate.storage.InDbUserStorage;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateAppTests {
    private final InDbUserStorage userStorage;
    private final InDbFilmStorage filmStorage;
    private Film film;
    private User user;
    private final Film.FilmBuilder filmBuilder = Film.builder();
    private final User.UserBuilder userBuilder = User.builder();

    Film makeTestFilm() {
        return filmBuilder
                .name("фильм")
                .description("обычный фильм")
                .releaseDate("2015-01-01")
                .duration(100)
                .genres((Set.of(new Genre(1, "Комедия"))))
                .mpa(new Mpa(1, "G"))
                .build();
    }

    User makeTestUser() {
        return userBuilder
                .name("юзер")
                .email("email@com.com")
                .login("login")
                .birthday("2000-01-02")
                .build();
    }

    @BeforeEach
    void beforeEach() {
        List<Film> films = filmStorage.findAll();
        if (films.isEmpty()) {
            return;
        }
        for (Film film: films) {
            filmStorage.delete(film.getId());
        }

        List<User> users = userStorage.findAll();
        if (users.isEmpty()) {
            return;
        }
        for (User user: users) {
            userStorage.delete(user.getId());
        }
    }
    @Test
    void testEmptyFindAllFilms() {
        assertTrue(filmStorage.findAll().isEmpty(), "Вернулся не пустой список фильсов");
    }

    @Test
    void testCreateFilm() {
        filmStorage.add(makeTestFilm());

        assertEquals(filmStorage.findAll().size(), 1, "Неверный список фильмов");
    }

    @Test
    void testGetFilm() {
        filmStorage.add(makeTestFilm());

        film = filmStorage.findAll().stream().findFirst().get();

        assertEquals(film.getName(), "фильм", "Имя фильма не сопадавет");
        assertEquals(film.getDescription(), "обычный фильм", "Описание фильма не сопадавет");
        assertEquals(film.getReleaseDate(), "2015-01-01", "Дата выпуска фильма не сопадавет");
        assertEquals(film.getDuration(), 100, "Длительность фильма не сопадавет");
        assertEquals(film.getMpa(), new Mpa(1, "G"), "Рейтинг фильма не совпадает");
        assertEquals(film.getGenres(), Set.of(new Genre(1, "Комедия")), "Жанр фильма не совпадает");
    }

    @Test
    void testUpdateFilm() {
        filmStorage.add(makeTestFilm());

        Film film = filmBuilder
                .id(1)
                .name("фильм")
                .description("супер фильм")
                .releaseDate("2015-01-01")
                .duration(100)
                .genres((Set.of(new Genre(1, "Комедия"))))
                .mpa(new Mpa(1, "G"))
                .build();
        filmStorage.update(film);

        assertEquals(filmStorage.findAll().stream().findFirst().get().getDescription(),
                "супер фильм", "Обновление фильма не работает");
    }

    @Test
    void testDeleteFilm() {
        filmStorage.add(makeTestFilm());

        filmStorage.delete(1);

        assertNull(filmStorage.get(1), "Удаление фильма не работает");
    }

    @Test
    void testEmptyFindAllUsers() {
        assertTrue(userStorage.findAll().isEmpty(), "Список пользователей не пустой");
    }

    @Test
    void testFindAllUsers() {
        user = makeTestUser();

        userStorage.add(user);

        assertEquals(userStorage.findAll().size(), 1, "Неверный список пользователей" );
    }

    @Test
    void testGetUser() {
        userStorage.add(makeTestUser());

        user = userStorage.findAll().stream().findFirst().get();

        assertEquals(user.getName(), "юзер", "Имя пользователя не совпадает");
        assertEquals(user.getEmail(), "email@com.com", "Почта полтзователя не совпадает");
        assertEquals(user.getLogin(), "login", "Логин пользователя не совпадает");
        assertEquals(user.getBirthday(), "2000-01-02", "Дата рождения пользователя не совпадает");
    }

    @Test
    void testUpdateUser() {
        userStorage.add(makeTestUser());

        int userId = userStorage.findAll().stream().findFirst().get().getId();
        User user = userBuilder
                .id(userId)
                .name("name")
                .login("login")
                .email("email@com.com")
                .birthday("2020-01-02")
                .build();
        userStorage.update(user);

        assertEquals(userStorage.findAll().stream().findFirst().get().getName(),
                "name", "Обновление пользователя не работает") ;
    }
}
