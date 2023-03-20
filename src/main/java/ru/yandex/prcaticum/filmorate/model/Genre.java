package ru.yandex.prcaticum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode
public class Genre {
    private final int id;
    private final String name;
}
