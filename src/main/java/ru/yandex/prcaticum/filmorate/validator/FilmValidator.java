package ru.yandex.prcaticum.filmorate.validator;

import ru.yandex.prcaticum.filmorate.exception.ValidationException;
import ru.yandex.prcaticum.filmorate.model.Film;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class FilmValidator {
    private final static String MIN_DATE="1895-12-28";
    public static void validate(Film film) throws ValidationException, ParseException {
        boolean isValid = true;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        if (film.getName() == null || film.getName().isBlank()) {
            isValid = false;
        } else if (film.getDescription() == null || film.getDescription().length() > 200) {
            isValid = false;
        } else if (film.getReleaseDate() == null ||
                dateFormatter.parse(film.getReleaseDate()).before(dateFormatter.parse(MIN_DATE))) {
            isValid = false;
        } else if (film.getDuration() < 1) {
            isValid = false;
        }

        if (!isValid) {
            throw new ValidationException();
        }
    }
}
