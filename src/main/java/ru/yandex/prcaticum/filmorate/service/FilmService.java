package ru.yandex.prcaticum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
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

        if (filmStorage.update(film) == null) {
            throw new NoSuchEntityException(String.format("Фильма с id %d не существует", film.getId()));
        }

        return film;
    }

    public Film getFilm(Integer filmId) {
        Film film = filmStorage.get(filmId);

        if (film == null) {
            throw new NoSuchEntityException(String.format("Фильма с id %d не существует", filmId));
        }

        return film;
    }
    public void likeFilm(Integer filmId, Integer userId) {
        userService.getUserById(userId);
        Film film = getFilm(filmId);
        film.getLikes().add(userId);
    }

    public void unlikeFilm(Integer filmId, Integer userId)
            throws NoSuchEntityException {
        userService.getUserById(userId);
        Film film = getFilm(filmId);

        if (!film.getLikes().remove(userId)) {
            throw new NoSuchEntityException(
                    String.format("у фильма с id %d нет лайка от юзера с id %d", filmId, userId)
            );
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        return findAll().stream()
                .sorted(Comparator.comparing(f -> f.getLikes().size(),Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
