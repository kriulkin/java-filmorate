package ru.yandex.prcaticum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.Set;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @NonFinal int id;
    @NonFinal String name;
    @EqualsAndHashCode.Include String email;
    String login;
    String birthday;
    Set<Integer> friends;
}





