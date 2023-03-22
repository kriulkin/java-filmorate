package ru.yandex.prcaticum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.prcaticum.filmorate.exception.NoSuchEntityException;
import ru.yandex.prcaticum.filmorate.model.Mpa;
import ru.yandex.prcaticum.filmorate.storage.InDbMpaStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final InDbMpaStorage mpaStorage;

    public Collection findAll() {
        return mpaStorage.findAll();
    }

    public Mpa getMpa(int id) {
        Mpa mpa = mpaStorage.get(id);

        if (mpa == null) {
            throw new NoSuchEntityException(String.format("Рейтинга с id %d не существует", id));
        }

        return mpa;
    }
}
