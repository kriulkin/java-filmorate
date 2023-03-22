package ru.yandex.prcaticum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.prcaticum.filmorate.model.Mpa;
import ru.yandex.prcaticum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public Collection<Mpa> findAll() {
        log.debug("Список всех рейтигов");
        return mpaService.findAll();
    }

    @GetMapping("/{id}")
    public Mpa get(@PathVariable("id") Integer id) { return mpaService.getMpa(id);}

}
