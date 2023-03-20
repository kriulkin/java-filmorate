package ru.yandex.prcaticum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.prcaticum.filmorate.exception.NoSuchEntityException;
import ru.yandex.prcaticum.filmorate.model.Genre;
import ru.yandex.prcaticum.filmorate.storage.InDbGenreStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final InDbGenreStorage genreStorage;

    public Collection<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre getGenre(int id) {
        Genre genre = genreStorage.get(id);

        if (genre == null) {
            throw new NoSuchEntityException(String.format("Жанра с id %d не существует", id));
        }

        return genre;
    }
}
