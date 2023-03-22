package ru.yandex.prcaticum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.prcaticum.filmorate.exception.NoSuchEntityException;
import ru.yandex.prcaticum.filmorate.model.Film;
import ru.yandex.prcaticum.filmorate.storage.FilmStorage;
import ru.yandex.prcaticum.filmorate.validator.FilmValidator;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreService genreService;

    private final UserService userService;

    public FilmService(@Qualifier("inDbFilmStorage") FilmStorage filmStorage, GenreService genreService, UserService userService) {
        this.filmStorage = filmStorage;
        this.genreService = genreService;
        this.userService = userService;
    }

    public List<Film> findAll() {
        List<Film> filmList = filmStorage.findAll();
        genreService.setFilmsGenres(filmList);
        return filmList;
    }

    public Film create(Film film) {
        FilmValidator.validate(film);
        Film resultFilm = filmStorage.add(film);
        resultFilm.getGenres().addAll(genreService.getFilmGenres(resultFilm.getId()));
        return resultFilm;
    }

    public Film update(Film film) {
        FilmValidator.validate(film);

        Film resultFilm = filmStorage.update(film);
        if (resultFilm == null) {
            throw new NoSuchEntityException(String.format("Фильма с id %d не существует", film.getId()));
        }
        resultFilm.getGenres().addAll(genreService.getFilmGenres(film.getId()));
        return resultFilm;
    }

    public Film getFilm(Integer filmId) {
        Film film = filmStorage.get(filmId);

        if (film == null) {
            throw new NoSuchEntityException(String.format("Фильма с id %d не существует", filmId));
        }

        film.getGenres().addAll(genreService.getFilmGenres(filmId));
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
        List<Film> filmList = filmStorage.getPopularFilms()
                .stream()
                .limit(count)
                .collect(Collectors.toList());
        genreService.setFilmsGenres(filmList);
        return filmList;
    }
}
