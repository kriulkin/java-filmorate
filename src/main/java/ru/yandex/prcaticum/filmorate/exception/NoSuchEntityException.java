package ru.yandex.prcaticum.filmorate.exception;

public class NoSuchEntityException extends RuntimeException {

    public NoSuchEntityException(String message) {
        super(message);
    }
}
