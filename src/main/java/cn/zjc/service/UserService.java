package cn.zjc.service;

import cn.zjc.config.DataSourceType;
import cn.zjc.config.TargetDataSource;
import cn.zjc.dao.UserRepository;
import cn.zjc.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Integer selectAllCount() {
		return jdbcTemplate.queryForObject("Select count(*) from user", Integer.class);
	}

	@TargetDataSource(value = DataSourceType.SLAVER)
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer selectAllCountFromSlaver() { //使用从数据源
		return jdbcTemplate.queryForObject("Select count(*) from user", Integer.class);
	}


	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void testTransaction() {
		jdbcTemplate.update("INSERT INTO user(name,age) VALUES (?,?)", new Object[]{"zjc", "100"});

		int s = 1 / 0;
		Integer count = jdbcTemplate.queryForObject("Select count(*) from user", Integer.class);

		System.out.println(count);
	}


	@Transactional
	public boolean saveUser(User u) {
		if (userRepository.queryByName(u.getName()) != null) {
			return false;
		}
		userRepository.save(u);
		return true;
	}
}
