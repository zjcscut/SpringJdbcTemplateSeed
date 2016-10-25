package cn.zjc.rowmapper;

import cn.zjc.schedule.entity.ScheduleJob;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zhangjinci
 * @version 2016/10/24 14:11
 * @function
 */
public class ScheduleJobRowMapper implements RowMapper<ScheduleJob> {

    @Override
    public ScheduleJob mapRow(ResultSet resultSet, int i) throws SQLException {
        ScheduleJob scheduleJob = new ScheduleJob();
        scheduleJob.setId(resultSet.getLong("ID"));
        scheduleJob.setJobName(resultSet.getString("JOBNAME"));
        scheduleJob.setJobGroup(resultSet.getString("JOBGROUP"));
        scheduleJob.setCronExpression(resultSet.getString("CRON_EXPRESSION"));
        scheduleJob.setRunType(resultSet.getInt("RUN_TYPE"));
        scheduleJob.setRunStatus(resultSet.getInt("RUN_STATUS"));
        scheduleJob.setTargetClassNmae(resultSet.getString("TARGET_CLASS_NAME"));
        scheduleJob.setDescription(resultSet.getString("DESCRTPTION"));
        scheduleJob.setCreateTime(resultSet.getTimestamp("CREATE_TIME"));
        scheduleJob.setExecuteTime(resultSet.getTimestamp("EXECUTE_TIME"));
        scheduleJob.setModifyTime(resultSet.getTimestamp("MODIFY_TIME"));
        scheduleJob.setIsEnabled(resultSet.getInt("IS_ENABLED"));
        return scheduleJob;
    }
}
