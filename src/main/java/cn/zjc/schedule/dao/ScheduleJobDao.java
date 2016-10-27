package cn.zjc.schedule.dao;

import cn.zjc.schedule.entity.ScheduleJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:55
 * @function
 */

public interface ScheduleJobDao extends JpaRepository<ScheduleJob, Long> {


    @Query(value = "SELECT * FROM TB_AT_SCHEDULE_JOB WHERE RUN_TYPE = 0 AND IS_ENABLED = 1", nativeQuery = true)
    List<ScheduleJob> queryAutoList();

    @Query(value = "SELECT * FROM TB_AT_SCHEDULE_JOB WHERE ID = :id", nativeQuery = true)
    ScheduleJob queryById(@Param("id") Long id);

    @Query(value = "SELECT * FROM TB_AT_SCHEDULE_JOB", nativeQuery = true)
    List<ScheduleJob> queryAll();


}
