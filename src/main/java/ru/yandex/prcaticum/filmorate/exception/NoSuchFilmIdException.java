package ru.yandex.prcaticum.filmorate.exception;

public class NoSuchFilmIdException extends RuntimeException {
    public NoSuchFilmIdException(String message) {
        super(message);
    }
}
