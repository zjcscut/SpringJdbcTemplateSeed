package cn.zjc.service;

import cn.zjc.dao.ScheduleJobDao;
import cn.zjc.entity.ScheduleJob;
import cn.zjc.schedule.support.ScehduleJobSupports;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:51
 * @function
 */
@Service(value = "scheduleService")
public class ScheduleService {

    /*调度工厂Bean*/
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ScheduleJobDao scheduleJobDao;

    @Autowired
    private ScehduleJobSupports scehduleJobSupports;

    public void initScheduleService() {
        List<ScheduleJob> scheduleJobList = scheduleJobDao.queryList();
        if (CollectionUtils.isEmpty(scheduleJobList)) {
            return;
        }
        for (ScheduleJob scheduleJob : scheduleJobList) {

        }
    }
}
