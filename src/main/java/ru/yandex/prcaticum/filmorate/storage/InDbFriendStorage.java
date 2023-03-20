package ru.yandex.prcaticum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InDbFriendStorage {
    private final JdbcTemplate jdbcTemplate;

    public InDbFriendStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Set<Integer> getFriendsList(int userId) {
        return new HashSet<>(jdbcTemplate.query(
                "select user_id2 from friends where user_id1 = ?",
                (rs, num) -> rs.getInt("user_id2"),
                userId));
    }

    public void addFriend(int userId, int friendId) {
        SqlRowSet friendInfo = jdbcTemplate.queryForRowSet(
                "select * from friends where user_id1 = ? and user_id2 = ?", userId, friendId);
        if (friendInfo.next()) {
            if (!friendInfo.getBoolean("approved")) {
                jdbcTemplate.update("update freinds set approve = true where user_id1 = ? and user_id2 = ?",
                        userId, friendId);
            }
        } else {
            jdbcTemplate.update("insert into friends (user_id1, user_id2, approved) values (?,?,false)",
                    userId, friendId);
        }
    }

    public boolean deleteFriend(int userId, int friendId) {
        return jdbcTemplate.update("delete from friends where user_id1 = ? and user_id2 = ?;", userId, friendId) > 0;
    }
}
