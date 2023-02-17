package ru.yandex.prcaticum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    static int currentId = 0;
    int id;
    @EqualsAndHashCode.Include private final String email;
    final String login;
    String name;
    final String birthday;
    final Set<Integer> friends = new HashSet<>();

    public static int getCurrentId() { return ++currentId; }
}
