package cn.zjc.schedule.factory;

import cn.zjc.schedule.entity.ScheduleJob;
import cn.zjc.service.ScheduleService;
import cn.zjc.utils.Assert;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:47
 * @function 异步Job
 */
public class JobAsyncFactory implements Job {

    private static final Logger log = LoggerFactory.getLogger(JobAsyncFactory.class);

    @Autowired
    private ScheduleService scheduleService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("JobAsyncFactory execute...");
        Long taskId = (Long) jobExecutionContext
                .getMergedJobDataMap()
                .get(ScheduleJob.JOB_PARAM_KEY);
        ScheduleJob scheduleJob = scheduleService.queryByTaskId(taskId);
        Assert.notNull(scheduleJob);
        log.debug("jobName:" + scheduleJob.getJobName() + " " + scheduleJob);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("JobAsyncFactory sleep failed:" + e.getMessage());
        }
    }
}
