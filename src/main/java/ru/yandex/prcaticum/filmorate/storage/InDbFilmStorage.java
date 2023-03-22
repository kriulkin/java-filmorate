package ru.yandex.prcaticum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.prcaticum.filmorate.model.Film;
import ru.yandex.prcaticum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component("inDbFilmStorage")
@AllArgsConstructor
public class InDbFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final InDbGenreStorage genreStorage;
    private final InDbMpaStorage mpaStorage;

    @Override
    public List<Film> findAll() {
        String sql = "select * from films";
        return List.copyOf(jdbcTemplate.query(sql, (rs, num) -> makeFilm(rs)));
    }


    @Override
    public Film get(Integer id) {
        return jdbcTemplate.query("select * from films where film_id = ?;",
                        (rs, num) -> makeFilm(rs),
                        id
                )
                .stream().findFirst().orElse(null);
    }

    @Override
    public Film add(Film film) {
        jdbcTemplate.update("insert into films (name, description, release_date, duration, mpa_id) values (?,?,?,?,?);",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );

        SqlRowSet filmRow = jdbcTemplate.queryForRowSet("select film_id from films where name = ? and " +
                "description = ? and " +
                "release_date = ? and " +
                "duration = ? and " +
                "mpa_id = ?;",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );

        if (filmRow.next()) {
            int filmId = filmRow.getInt("film_id");
            if (film.getGenres() != null) {
                genreStorage.addFilmGenres(filmId, film.getGenres());
            }

            return get(filmId);
        }

        return null;
    }

    @Override
    public Film update(Film film) {

        int updatedRowsNum = jdbcTemplate.update("update films set " +
                        "name = ?, " +
                        "description = ?, " +
                        "release_date = ?, " +
                        "duration = ?, " +
                        "mpa_id = ? " +
                        "where film_id = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        if (updatedRowsNum > 0) {
            for (Genre genre: genreStorage.getFilmGenres(film.getId())) {
                genreStorage.deleteFilmGenre(film.getId(), genre.getId());
            }

            if (film.getGenres() != null) {
                genreStorage.addFilmGenres(film.getId(), film.getGenres());
            }

            return get(film.getId());
        }

        return null;
    }

    @Override
    public Film delete(Integer id) {
        Film film = get(id);

        if (film == null) {
            return null;
        }

        jdbcTemplate.update("delete from films where film_id = ?", id);

        for (Genre genre: film.getGenres()) {
            genreStorage.deleteFilmGenre(id, genre.getId());
        }
        return film;
    }

    public void likeFilm(int filmId, int userId) {
        jdbcTemplate.update("insert into likes (user_id, film_id) values (?,?);", userId, filmId);
    }

    public boolean unlikeFilm(int filmId, int userId) {
        return jdbcTemplate.update("delete from likes where user_id = ? and film_id = ?;", userId, filmId) > 0;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        return new Film(
                rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toString(),
                rs.getInt("duration"),
                mpaStorage.get(rs.getInt("mpa_id")),
                new HashSet<>()
        );
    }

    public Collection<Film> getPopularFilms() {
        return jdbcTemplate.query("select f.*, m.mpa_id, count(l.film_id) cnt from films f " +
                        "join mpa m on f.film_id=m.mpa_id " +
                        "left join likes l on f.film_id=l.film_id " +
                        "group by f.film_id " +
                        "order by cnt DESC;",
                (rs, num) -> makeFilm(rs));
    }
}
