package ru.yandex.prcaticum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
@Builder
public class Film {
    @NonFinal int id;
    String name;
    String description;
    String releaseDate;
    int duration;
    Set<Integer> likes = new HashSet<>();
}
