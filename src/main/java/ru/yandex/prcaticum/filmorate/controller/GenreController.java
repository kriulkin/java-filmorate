package ru.yandex.prcaticum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.prcaticum.filmorate.model.Genre;
import ru.yandex.prcaticum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor
public class GenreController {
        private final GenreService genreService;

        @GetMapping
        public Collection<Genre> findAll() {
            log.debug("Список всех рейтигов");
            return genreService.findAll();
        }

        @GetMapping("/{id}")
        public Genre get(@PathVariable("id") Integer id) { return genreService.getGenre(id);}

}
