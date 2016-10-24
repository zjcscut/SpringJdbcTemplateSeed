package cn.zjc.dao;

import cn.zjc.entity.ScheduleJob;
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

    public List<ScheduleJob> queryList(){
        return jdbcTemplate.query("SELECT * FROM TB_AT_SCHEDULE",new ScheduleJobRowMapper());
    }
}
