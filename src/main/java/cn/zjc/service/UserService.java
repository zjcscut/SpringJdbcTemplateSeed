package cn.zjc.service;

import cn.zjc.config.DataSourceType;
import cn.zjc.config.TargetDataSource;
import cn.zjc.dao.UserRepository;
import cn.zjc.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangjinci
 * @version 2016/10/9 14:39
 * @function
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> selectAll() {
        return userRepository.findAll();
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @TargetDataSource(value = DataSourceType.MASTER) //使用主数据源
    public Integer selectAllCount() {
        return jdbcTemplate.queryForObject("Select count(*) from user", Integer.class);
    }
    
    @TargetDataSource(value = DataSourceType.SLAVER)
    public Integer selectAllCountFromSlaver() { //使用从数据源
        return jdbcTemplate.queryForObject("Select count(*) from user", Integer.class);
    }
}
