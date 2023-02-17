package ru.yandex.prcaticum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level=AccessLevel.PRIVATE)
@Builder
public class Film {
    static int currentId = 0;
    int id;
    final String name;
    final String description;
    final String releaseDate;
    final int duration;
    final Set<Integer> likes = new HashSet<>();

    public static int getCurrentId() {
        return ++currentId;
    }
}
