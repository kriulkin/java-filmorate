package ru.yandex.prcaticum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.prcaticum.filmorate.model.Film;
import ru.yandex.prcaticum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InDbGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public InDbGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Genre> findAll() {
        return jdbcTemplate.query("select * from genres;", (rs, num) -> makeGenre(rs));
    }

    public Genre get(int id) {
        return jdbcTemplate.query("select * from genres where genre_id = ?;", (rs, num) -> makeGenre(rs), id)
                .stream().findFirst().orElse(null);
    }

    public Set<Genre> getFilmGenres(int filmId) {
        return new HashSet(jdbcTemplate.query("select g.genre_id, g.name from genres g " +
                        "join film_genre fg on g.genre_id = fg.genre_id where fg.film_id = ?" +
                        "order by g.genre_id;",
                (rs, num) -> get(rs.getInt("genre_id")), filmId));
    }

    public void addFilmGenres(int filmId, Set<Genre> genres) {
        for (Genre genre: genres) {
            jdbcTemplate.update("insert into film_genre (film_id, genre_id) values (?, ?)", filmId, genre.getId());
        }
    }

    public boolean deleteFilmGenre(int filmId, int genreId) {
        return jdbcTemplate.update("delete from film_genre where film_id = ? and genre_id = ?;",
                filmId,
                genreId
        ) > 0;
    }

    private Genre makeGenre(ResultSet resultSet) throws SQLException {
        return new Genre(
                resultSet.getInt("genre_id"),
                resultSet.getString("name")
        );
    }

    public void setFilmsGenres(List<Film> films) {

        String sql = "select fg.film_id, fg.genre_id, g.name from film_genre fg join genres g on fg.genre_id=g.genre_id where fg.film_id in (?);";
        String sqlIn = films.stream()
                .map(Film::getId)
                .map(x -> String.valueOf(x))
                .collect(Collectors.joining(",", "(", ")"));

        sql = sql.replace("(?)", sqlIn);

        List<Map.Entry<Integer, Genre>> list = jdbcTemplate.query(
                sql,
                (rs, num) -> Map.entry(rs.getInt("film_id"),
                        new Genre(rs.getInt("genre_id"),rs.getString("name"))
                ));

        log.debug(list.toString());

        Map<Integer, Set<Genre>> filmsGenresMap = new HashMap<>();

        for (Map.Entry<Integer,Genre> filmsGenresEntry: list) {
            Integer filmId = filmsGenresEntry.getKey();
            Genre genre = filmsGenresEntry.getValue();
            if (!filmsGenresMap.containsKey(filmId)) {
                filmsGenresMap.put(filmId, new HashSet<>() {{add(genre);}});
            } else {
                Set currentGenres = filmsGenresMap.get(filmId);
                currentGenres.add(genre);
                filmsGenresMap.put(filmId, currentGenres);
            }
        }

        for (Film film: films) {
            Set<Genre> filmGenres = film.getGenres();
            Set<Genre> newFilmGenres = filmsGenresMap.get(film.getId());
            if (newFilmGenres != null && !newFilmGenres.isEmpty() ) {
                log.debug(newFilmGenres.toString());
                filmGenres.addAll(newFilmGenres);
            }
        }
    }
}
