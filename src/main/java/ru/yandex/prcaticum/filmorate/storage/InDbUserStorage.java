package ru.yandex.prcaticum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.prcaticum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("inDbUserStorage")
@AllArgsConstructor
public class InDbUserStorage implements UserStorage {


    private final JdbcTemplate jdbcTemplate;
    private final InDbFriendStorage inDbFriendStorage;

    @Override
    public List<User> findAll() {
        String sql = "select * from users";
        return List.copyOf(jdbcTemplate.query(sql, (rs, num) -> makeUser(inDbFriendStorage, rs)));
    }

    @Override
    public User get(Integer id) {
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("select * from users where user_id = ?", id);
        if (userRow.next()) {
            User user = makeUser(inDbFriendStorage, userRow);
            return user;
        }

        return null;
    }

    @Override
    public User add(User user) {
        jdbcTemplate.update("insert into users (name, email, login, birthday) values (?,?,?,?)",
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday()
                );

        SqlRowSet userRow = jdbcTemplate.queryForRowSet("select * from users where email = ?", user.getEmail());
        if (userRow.next()) {
            return get(userRow.getInt("user_id"));
        }

        return null;
    }

    @Override
    public User update(User user) {
        int updatedRowsNum = jdbcTemplate.update("update users set " +
                "name = ?, " +
                "email = ?, " +
                "login = ?, " +
                "birthday = ? " +
                "where user_id = ?",
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());

        if (updatedRowsNum > 0) {
            return get(user.getId());
        }

        return null;
    }

    @Override
    public User delete(Integer id) {
        User user = get(id);

        if (user == null) {
            return null;
        }

        jdbcTemplate.update("delete from users where user_id = ?", id);

        for (int friendId: user.getFriends()) {
            inDbFriendStorage.deleteFriend(id, friendId);
        }
        return user;
    }

    private User makeUser(InDbFriendStorage inDbFriendStorage, ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("user_id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getDate("birthday").toString(),
                inDbFriendStorage.getFriendsList(resultSet.getInt("user_id"))
        );
    }

    private User makeUser(InDbFriendStorage inDbFriendStorage, SqlRowSet sqlRowSet) {
        return new User(
                sqlRowSet.getInt("user_id"),
                sqlRowSet.getString("name"),
                sqlRowSet.getString("email"),
                sqlRowSet.getString("login"),
                sqlRowSet.getDate("birthday").toString(),
                inDbFriendStorage.getFriendsList(sqlRowSet.getInt("user_id"))
        );
    }
}
