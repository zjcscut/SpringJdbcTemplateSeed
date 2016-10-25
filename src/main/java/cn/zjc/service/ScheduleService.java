package cn.zjc.service;

import cn.zjc.schedule.dao.ScheduleJobDao;
import cn.zjc.schedule.entity.ScheduleJob;
import cn.zjc.schedule.support.ScehduleJobSupports;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:51
 * @function
 */
@Service
public class ScheduleService {


    @Autowired
    private ScheduleJobDao scheduleJobDao;

    @Autowired
    private ScehduleJobSupports scehduleJobSupports;

    /**
     *
     */
    public void initScheduleService() {
        List<ScheduleJob> scheduleJobList = scheduleJobDao.queryAutoList();
        if (CollectionUtils.isEmpty(scheduleJobList)) {
            return;
        }
        for (ScheduleJob scheduleJob : scheduleJobList) {
            scehduleJobSupports.createScheduleJob(scheduleJob);
        }
        scehduleJobSupports.start();
    }

    /**
     *
     */
    public void shutScheduleService() {
        scehduleJobSupports.shutdown(true);
    }

    /**
     * @param id
     * @return
     */
    public ScheduleJob queryByTaskId(Long id) {
        return scheduleJobDao.queryById(id);
    }

    public List<ScheduleJob> queryAll(){

    }
}
