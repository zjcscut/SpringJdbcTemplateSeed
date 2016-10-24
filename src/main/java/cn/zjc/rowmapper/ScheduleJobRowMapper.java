package cn.zjc.rowmapper;

import cn.zjc.entity.ScheduleJob;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zhangjinci
 * @version 2016/10/24 14:11
 * @function
 */
public class ScheduleJobRowMapper implements RowMapper<ScheduleJob>{

    @Override
    public ScheduleJob mapRow(ResultSet resultSet, int i) throws SQLException {
        ScheduleJob scheduleJob = new ScheduleJob();
        scheduleJob.setId(resultSet.getLong("ID"));
        scheduleJob.setJobName("JOBNAME");
        scheduleJob.setJobGroup("JOBGROUP");
        return scheduleJob;
    }
}
