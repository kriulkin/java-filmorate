package ru.yandex.prcaticum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.prcaticum.filmorate.exception.NoLikeFromUserId;
import ru.yandex.prcaticum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.prcaticum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.prcaticum.filmorate.exception.ValidationException;
import ru.yandex.prcaticum.filmorate.model.Film;
import ru.yandex.prcaticum.filmorate.storage.FilmStorage;
import ru.yandex.prcaticum.filmorate.validator.FilmValidator;

import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    FilmStorage filmStorage;
    UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> findAll() {
        return List.copyOf(filmStorage.getFilms().values());
    }

    public Film create(Film film) throws ValidationException, ParseException {
        FilmValidator.validate(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) throws ValidationException, ParseException, NoSuchFilmIdException {
        FilmValidator.validate(film);
        Integer filmId = film.getId();

        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new NoSuchFilmIdException(String.format("Фильма с id %d не существует", filmId));
        }

        return filmStorage.update(film);
    }

    public Film getFilm(Integer filmId) throws NoSuchFilmIdException {
        Film film = filmStorage.get(filmId);

        if (film == null) {
            throw new NoSuchFilmIdException(String.format("Фильма с id %d не существует", filmId));
        }

        return film;
    }
    public void likeFilm(Integer filmId, Integer userId) throws NoSuchUserIdException, NoSuchFilmIdException {
        userService.checkUserId(userId);
        Film film = getFilm(filmId);
        film.getLikes().add(userId);
    }

    public void unlikeFilm(Integer filmId, Integer userId)
            throws NoLikeFromUserId, NoSuchFilmIdException, NoSuchUserIdException {
        userService.checkUserId(userId);
        Film film = getFilm(filmId);

        if (!film.getLikes().remove(userId)) {
            throw new NoLikeFromUserId(String.format("у фильма с id %d нет лайка от юзера с id %d", filmId, userId));
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        return findAll().stream()
                .sorted(Comparator.comparing(f -> f.getLikes().size(),Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
