package cn.zjc.schedule.dao;

import cn.zjc.rowmapper.ScheduleRecordRowMapper;
import cn.zjc.schedule.entity.ScheduleRecord;
import org.springframework.beans.factory.annotation.Autowired;
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
        jdbcTemplate.update("INSERT INTO ");
    }

    public void update(ScheduleRecord record) {
        jdbcTemplate.update("");
    }

    public ScheduleRecord queryByTaskId(Integer taskId) {
        return jdbcTemplate.queryForObject("SELECT * FROM TB_AT_SCHEDULE_RECORD WHERE TASK_ID = ?", new Object[]{taskId}, new ScheduleRecordRowMapper());
    }
}
