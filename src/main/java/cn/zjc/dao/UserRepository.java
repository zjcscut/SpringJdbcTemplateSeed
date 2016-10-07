package cn.zjc.dao;

import cn.zjc.entity.User;
import cn.zjc.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author zhangjinci
 * @version 2016/10/7 17:37
 * @function
 */
@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM user", new UserRowMapper());
    }

    @Transactional(readOnly = true)
    public User findById(final Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM user where id = ?", new Object[]{id}, new UserRowMapper());
    }

    public User create(final User user) {
        final String sql = "INSERT INTO user(name,age) values(?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getName());
                ps.setInt(2, user.getAge());
                return ps;
            }
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    public void delete(final Integer id) {
        final String sql = "delete from user where id=?";
        jdbcTemplate.update(sql,
                new Object[]{id},
                new int[]{java.sql.Types.INTEGER});
    }

    public void update(final User user) {
        jdbcTemplate.update(
                "update user set name=?,age=? where id=?",
                new Object[]{user.getName(), user.getAge(), user.getId()});
    }
}
