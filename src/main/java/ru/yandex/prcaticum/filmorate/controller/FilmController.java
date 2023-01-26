package ru.yandex.prcaticum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.prcaticum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.prcaticum.filmorate.exception.ValidationException;
import ru.yandex.prcaticum.filmorate.model.Film;
import ru.yandex.prcaticum.filmorate.validator.FilmValidator;

import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException, ParseException {
        log.debug(film.toString());
        FilmValidator.validate(film);
        film.setId(Film.getCurrentId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException, ParseException, NoSuchFilmIdException {
        log.debug(film.toString());
        int filmId = film.getId();
        FilmValidator.validate(film);

        if (!films.containsKey(filmId)) {
            throw new NoSuchFilmIdException();

        }

        films.put(filmId, film);
        return film;
    }
}
