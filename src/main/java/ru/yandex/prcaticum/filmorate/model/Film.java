package ru.yandex.prcaticum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level=AccessLevel.PRIVATE)
@Builder
public class Film {
    static int currentId;
    int id;
    final String name;
    final String description;
    final String releaseDate;
    final int duration;

    public static int getCurrentId() {
        return ++currentId;
    }
}
