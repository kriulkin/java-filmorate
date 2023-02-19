package ru.yandex.prcaticum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.prcaticum.filmorate.model.Film;
import ru.yandex.prcaticum.filmorate.service.FilmService;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", filmService.findAll().size());
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.debug(film.toString());
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.debug(film.toString());
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable("id") Integer filmId) {
        return filmService.getFilm(filmId);
    }
    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(
            @PathVariable("id") Integer filmId,
            @PathVariable("userId") Integer userId
    ) {
        filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unlikeFilm(
            @PathVariable("id") Integer filmId,
            @PathVariable("userId") Integer userId
    ) {
        filmService.unlikeFilm(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(name = "count", required = false, defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }
}
