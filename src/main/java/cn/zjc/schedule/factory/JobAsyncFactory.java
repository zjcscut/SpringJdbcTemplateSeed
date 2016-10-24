package cn.zjc.schedule.factory;

import cn.zjc.entity.ScheduleJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:47
 * @function 异步Job
 */
public class JobAsyncFactory implements Job {

    private static final Logger log = LoggerFactory.getLogger(JobAsyncFactory.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("JobAsyncFactory execute...");
        ScheduleJob scheduleJob = (ScheduleJob) jobExecutionContext
                .getMergedJobDataMap()
                .get(ScheduleJob.JOB_PARAM_KEY);
        log.debug("jobName:" + scheduleJob.getJobName() + " " + scheduleJob);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("JobAsyncFactory sleep failed:" + e.getMessage());
        }
    }
}
