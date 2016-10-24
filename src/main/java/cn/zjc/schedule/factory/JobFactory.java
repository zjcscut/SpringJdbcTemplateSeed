package cn.zjc.schedule.factory;

import cn.zjc.entity.ScheduleJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:38
 * @function
 */
@DisallowConcurrentExecution //不允许多个任务并发
public class JobFactory implements Job {

    private static final Logger log = LoggerFactory.getLogger(JobFactory.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("JobFactory execute...");
        ScheduleJob scheduleJob = (ScheduleJob) jobExecutionContext
                .getMergedJobDataMap()
                .get(ScheduleJob.JOB_PARAM_KEY);
        log.debug("jobName:" + scheduleJob.getJobName() + " " + scheduleJob);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("JobFactory sleep failed:" + e.getMessage());
        }
    }
}
