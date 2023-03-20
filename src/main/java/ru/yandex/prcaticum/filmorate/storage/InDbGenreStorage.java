package ru.yandex.prcaticum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.prcaticum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                (rs, num) -> get(rs.getInt("genre_id")), filmId).stream().collect(Collectors.toSet()));
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
}
