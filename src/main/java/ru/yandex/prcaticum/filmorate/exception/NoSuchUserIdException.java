package ru.yandex.prcaticum.filmorate.exception;

public class NoSuchUserIdException extends RuntimeException {
    public NoSuchUserIdException(String message) {
        super(message);
    }
}
