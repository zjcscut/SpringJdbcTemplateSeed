package cn.zjc.dao;

import cn.zjc.entity.EventBusDef;
import cn.zjc.eventbus.constant.EventConst;
import cn.zjc.rowmapper.EventBusDefRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zjc
 * @version 2016/10/20 23:24
 * @description
 */
@Repository
public class EventBusDefDao {


	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public List<EventBusDef> queryAll() {
		return jdbcTemplate.query("SELECT * FROM tb_at_eventbus_def WHERE IS_AVAILABLE = ?", new Object[]{EventConst.IS_AVAILABLE}, new EventBusDefRowMapper());
	}
}
