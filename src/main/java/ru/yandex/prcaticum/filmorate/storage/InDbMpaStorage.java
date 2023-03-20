package ru.yandex.prcaticum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.prcaticum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class InDbMpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public InDbMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Mpa> findAll() {
        return jdbcTemplate.query("select * from mpa;", (rs, num) -> makeMpa(rs));
    }

    public Mpa get(int id) {
        return jdbcTemplate.query("select * from mpa where mpa_id = ?;", (rs, num) -> makeMpa(rs), id)
                .stream().findFirst().orElse(null);
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(
                rs.getInt("mpa_id"),
                rs.getString("name")
        );
    }
}
