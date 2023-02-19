package ru.yandex.prcaticum.filmorate.validator;

import ru.yandex.prcaticum.filmorate.exception.ValidationException;
import ru.yandex.prcaticum.filmorate.model.Film;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class FilmValidator {
    private final static String MIN_DATE="1895-12-28";
    public static void validate(Film film){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        if (film.getName() == null ||
                film.getName().isBlank()) {
            throw new ValidationException("Пустое имя фильма.");
        }

        if (film.getDescription() == null ||
                film.getDescription().length() > 200) {
            throw new ValidationException("В описании фильма больше 200 символов.");
        }

        try {
            if (film.getReleaseDate() == null ||
                    dateFormatter.parse(film.getReleaseDate()).before(dateFormatter.parse(MIN_DATE))) {
                throw new ValidationException("Дата создания фильма раньше 12 декабря 1895 года.");
            }
        } catch (ParseException e) {
            throw new ValidationException("Некорректная дата создания фильма");
        }

        if (film.getDuration() < 1) {
            throw new ValidationException("Длительность фильма меньше 1 минуты.");
        }
    }
}
