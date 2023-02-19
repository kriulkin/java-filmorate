package ru.yandex.prcaticum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.prcaticum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int currentId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    public List<Film> findAll() {
        return List.copyOf(films.values());
    }

    @Override
    public Film get(Integer id) {
        return films.get(id);
    }

    @Override
    public Film add(Film film) {
        Integer filmId = getCurrentId();
        film.setId(filmId);
        films.put(filmId, film);
        return film;
    }
    @Override
    public Film update(Film film) {
        Integer filmId = film.getId();
        return films.replace(filmId, film) != null ? film :  null;
    }

    @Override
    public Film delete(Integer id) {
        return films.remove(id);
    }

    private int getCurrentId() { return ++currentId; }
}
