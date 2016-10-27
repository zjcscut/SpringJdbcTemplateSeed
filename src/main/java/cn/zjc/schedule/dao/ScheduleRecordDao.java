package cn.zjc.schedule.dao;


import cn.zjc.schedule.entity.ScheduleRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;


/**
 * @author zjc
 * @version 2016/10/24 23:53
 * @description
 */
@Repository
public class ScheduleRecordDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;


	public void save(ScheduleRecord record) {
		jdbcTemplate.update("INSERT INTO TB_AT_SCHEDULE_RECORD(TASK_ID,TRIGGER_INST_ID,START_TIME,END_TIME,COST) VALUES (?,?,?,?,?)",
				preparedStatement -> {
					preparedStatement.setLong(1, record.getId());
					preparedStatement.setString(2, record.getTriggerInstId());
					preparedStatement.setObject(3, record.getStartTime());
					preparedStatement.setObject(4, record.getEndTime());
					preparedStatement.setLong(5, record.getCost());
				});
	}

	public void update(ScheduleRecord record) {
		jdbcTemplate.update("UPDATE TB_AT_SCHEDULE_RECORD SET ");
	}

	public ScheduleRecord queryByTaskId(Long taskId) {
		return jdbcTemplate.queryForObject("SELECT * FROM TB_AT_SCHEDULE_RECORD WHERE TASK_ID = ?", new Object[]{taskId}, new BeanPropertyRowMapper<ScheduleRecord>());
	}
}
