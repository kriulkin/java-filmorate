package ru.yandex.prcaticum.filmorate.storage;

import ru.yandex.prcaticum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    Map<Integer,Film> getFilms();

    Film get(Integer id);

    Film add(Film film);

    Film update(Film film);

    Film delete(Integer id);

}
