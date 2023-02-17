package ru.yandex.prcaticum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.prcaticum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    public Map<Integer, Film> getFilms() {
        return films;
    }

    @Override
    public Film get(Integer id) {
        return films.get(id);
    }

    @Override
    public Film add(Film film) {
        Integer filmId = Film.getCurrentId();
        film.setId(filmId);
        films.put(filmId, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Integer filmId = film.getId();
        films.replace(filmId, film);
        return film;
    }

    @Override
    public Film delete(Integer id) {
        return films.remove(id);
    }
}
