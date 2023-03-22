package ru.yandex.prcaticum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.prcaticum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component("inMemoryFilmStorage")
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

    @Override
    public void likeFilm(int userId, int filmId) {
        Film film = get(filmId);
        film.getLikes().add(userId);
    }

    @Override
    public boolean unlikeFilm(int userId, int filmId) {
        Film film = get(filmId);
        return film.getLikes().remove(userId);
    }

    @Override
    public Collection<Film> getPopularFilms() {
        return findAll().stream()
                .sorted(Comparator.comparing(f -> f.getLikes().size(),Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private int getCurrentId() { return ++currentId; }
}
