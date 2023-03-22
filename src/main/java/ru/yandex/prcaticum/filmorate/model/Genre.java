package ru.yandex.prcaticum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Genre {
    private final int id;
    private final String name;
}
