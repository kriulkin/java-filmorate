package ru.yandex.prcaticum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.prcaticum.filmorate.exception.NoSuchEntityException;
import ru.yandex.prcaticum.filmorate.model.Film;
import ru.yandex.prcaticum.filmorate.storage.FilmStorage;
import ru.yandex.prcaticum.filmorate.validator.FilmValidator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    private final UserService userService;

    public FilmService(@Qualifier("inDbFilmStorage") FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        FilmValidator.validate(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        FilmValidator.validate(film);

        Film resultFilm = filmStorage.update(film);
        if (resultFilm == null) {
            throw new NoSuchEntityException(String.format("Фильма с id %d не существует", film.getId()));
        }

        return resultFilm;
    }

    public Film getFilm(Integer filmId) {
        Film film = filmStorage.get(filmId);

        if (film == null) {
            throw new NoSuchEntityException(String.format("Фильма с id %d не существует", filmId));
        }

        return film;
    }
    public void likeFilm(Integer filmId, Integer userId) {
        if (userService.getUserById(userId) == null) {
            throw new NoSuchEntityException(String.format("Пользователя с id %d не существует", userId));
        }

        Film film = getFilm(filmId);
        if (film == null) {
            throw new NoSuchEntityException(String.format("Фильма с id %d не существует", filmId));
        }
        filmStorage.likeFilm(filmId, userId);
    }

    public void unlikeFilm(Integer filmId, Integer userId) {
        if (userService.getUserById(userId) == null) {
            throw new NoSuchEntityException(String.format("Пользователя с id %d не существует", userId));
        }

        Film film = getFilm(filmId);

        if (film == null) {
            throw new NoSuchEntityException(String.format("Фильма с id %d не существует", filmId));
        }

        if (!filmStorage.unlikeFilm(filmId, userId)) {
            throw new NoSuchEntityException(
                    String.format("у фильма с id %d нет лайка от юзера с id %d", filmId, userId)
            );
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms()
                .stream()
                .limit(count)
                .collect(Collectors.toList());
    }
}
