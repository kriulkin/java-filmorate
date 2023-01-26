package ru.yandex.prcaticum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    private static int currentId = 0;
    private int id;
    @EqualsAndHashCode.Include private final String email;
    private final String login;
    private String name;
    private final String birthday;

    public static int getCurrentId() {
        return ++currentId;
    }
}
