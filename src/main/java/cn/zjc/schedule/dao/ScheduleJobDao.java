package cn.zjc.schedule.dao;

import cn.zjc.schedule.entity.ScheduleJob;
import cn.zjc.rowmapper.ScheduleJobRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:55
 * @function
 */

@Repository
public class ScheduleJobDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<ScheduleJob> queryAutoList() {
		return jdbcTemplate.query("SELECT * FROM TB_AT_SCHEDULE_JOB WHERE RUN_TYPE = 0 AND IS_ENABLED = 1", new ScheduleJobRowMapper());
	}

	public ScheduleJob queryById(Long id) {
		return jdbcTemplate.queryForObject("SELECT * FROM TB_AT_SCHEDULE_JOB WHERE ID = ?", new Object[]{id}, new ScheduleJobRowMapper());
	}

	public List<ScheduleJob> queryAll() {
		return jdbcTemplate.query("SELECT * FROM TB_AT_SCHEDULE_JOB", new ScheduleJobRowMapper());
	}
}
