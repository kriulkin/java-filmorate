package ru.yandex.prcaticum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Film {
    private static int currentId;
    private int id;
    private final String name;
    private final String description;
    private final String releaseDate;
    private final int duration;

    public static int getCurrentId() {
        return ++currentId;
    }
}
