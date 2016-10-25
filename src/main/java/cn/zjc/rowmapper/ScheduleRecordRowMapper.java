package cn.zjc.rowmapper;

import cn.zjc.schedule.entity.ScheduleRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zhangjinci
 * @version 2016/10/25 10:50
 * @function
 */
public class ScheduleRecordRowMapper implements RowMapper<ScheduleRecord> {

    @Override
    public ScheduleRecord mapRow(ResultSet resultSet, int i) throws SQLException {
        ScheduleRecord record = new ScheduleRecord();
        record.setId(resultSet.getLong("ID"));
        record.setTaskId(resultSet.getLong("TASK_ID"));
        record.setTriggerInstId(resultSet.getString("TRIGGER_INST_ID"));
        record.setStartTime(resultSet.getTimestamp("START_TIME"));
        record.setEndTime(resultSet.getTimestamp("END_TIME"));
        record.setCost(resultSet.getLong("COST"));
        return record;
    }
}
