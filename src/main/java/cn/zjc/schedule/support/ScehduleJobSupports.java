package cn.zjc.schedule.support;

import cn.zjc.entity.ScheduleJob;
import cn.zjc.exception.ScheduleException;
import cn.zjc.schedule.factory.JobAsyncFactory;
import cn.zjc.schedule.factory.JobFactory;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:58
 * @function 定时任务支持构件
 */
@Component
public class ScehduleJobSupports {

    private static final Logger log = LoggerFactory.getLogger(ScehduleJobSupports.class);

    private static final String DEFAULT_JOB_GROUP = "default_job_group";
    private static final String DEFAULT_TRIGGER_GROUP = "default_trigger_group";

    @Autowired
    private Scheduler scheduler;

    /**
     * 获取触发器key
     *
     * @param trggerName  任务名
     * @param triggerGroup 任务组
     * @return TriggerKey
     */
    public TriggerKey getTriggerKey(String trggerName, String triggerGroup) {
        return TriggerKey.triggerKey(trggerName,triggerGroup);
    }

    /**
     * 创建JobKey
     *
     * @param jobName  任务名
     * @param jobGroup 任务组
     * @return JobKey
     */
    public  JobKey createJobKey(String jobName, String jobGroup) {
        return JobKey.jobKey(jobName, jobGroup);
    }

    /**
     * 获取表达式触发器
     *
     * @return cron trigger
     */
    public CronTrigger getCronTrigger(String triggerName, String triggerGroup) {
        try {
            TriggerKey triggerKey = getTriggerKey(triggerName,triggerGroup);
            return (CronTrigger) scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            log.error("获取定时任务CronTrigger出现异常", e);
            throw new ScheduleException("获取定时任务CronTrigger出现异常");
        }
    }

    /**
     * 创建定时任务
     *
     * @param scheduleJob the schedule job
     */
    public void createScheduleJob(ScheduleJob scheduleJob) {
        createScheduleJob(scheduleJob.getJobName(), scheduleJob.getJobGroup(), scheduleJob.getJobName(), scheduleJob.getJobGroup(),
                scheduleJob.getCronExpression(), scheduleJob.getIsManual(), scheduleJob.getId());
    }


    /**
     * 创建定时任务
     *
     * @param jobName        the job name
     * @param jobGroup       the job group
     * @param cronExpression the cron expression
     * @param isManual       the is isManual
     * @param param          the param
     *
     * triggerName使用jobName
     */
    public void createScheduleJob(String jobName, String jobGroup, String triggerName, String triggerGroup,
                                  String cronExpression, Integer isManual, Object param) {
        if (StringUtils.isBlank(jobGroup)){
            jobGroup = DEFAULT_JOB_GROUP;
        }
        if (StringUtils.isBlank(triggerGroup)) {
            triggerGroup = DEFAULT_TRIGGER_GROUP;
        }
        //注入job class
        Class<? extends Job> jobClass = isManual == 1 ? JobAsyncFactory.class : JobFactory.class;
        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();
        //放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(ScheduleJob.JOB_PARAM_KEY, param);
        //表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup)
                .withSchedule(scheduleBuilder).build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("创建定时任务失败", e);
            throw new ScheduleException("创建定时任务失败");
        }
    }

    /**
     * 触发一次
     *
     * @param jobName  任务名
     * @param jobGroup 任务组
     */
    public void trigger(String jobName, String jobGroup) {
        try {
            scheduler.triggerJob(createJobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            log.error("运行一次定时任务失败", e);
            throw new ScheduleException("运行一次定时任务失败");
        }
    }

    /**
     * 暂停任务
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     */
    public void pause(String jobName, String jobGroup) {
        try {
            scheduler.pauseJob(createJobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            log.error("暂停定时任务失败", e);
            throw new ScheduleException("暂停定时任务失败");
        }
    }

    /**
     * 恢复任务
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     */
    public void resume(String jobName, String jobGroup) {
        try {
            scheduler.resumeJob(createJobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            log.error("恢复定时任务失败", e);
            throw new ScheduleException("恢复定时任务失败");
        }
    }

    /**
     * 更新ScheduleJob
     *
     * @param scheduleJob 任务
     */
    public void updateScheduleJob(ScheduleJob scheduleJob) {
        updateScheduleJob(scheduleJob.getJobName(), scheduleJob.getJobGroup(), scheduleJob.getCronExpression());
    }

    /**
     * 更新ScheduleJob
     *
     * @param jobName        任务名
     * @param jobGroup       任务组
     * @param cronExpression cron表达式
     */
    public void updateScheduleJob(String jobName, String jobGroup, String cronExpression) {
        try {
            //实际操作中发现只有Cron表达式可以修改,其他参数不能修改
            TriggerKey triggerKey = getTriggerKey(jobName, jobGroup);
            //重建表达式调度器
            // 如果当前的时间已经超过触发器的开始时间，则指定不做任何操作
            // 否则默认情况下，每次启动都会执行一次
            //trigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING)<== 旧版本
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //按新的cronExpression表达式重新构建Crontrigger
            cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, cronTrigger);
        } catch (SchedulerException e) {
            log.error("更新定时任务失败", e);
            throw new ScheduleException("更新定时任务失败");
        }
    }

    /**
     * 删除定时任务l
     *
     * @param jobName  任务名
     * @param jobGroup 任务组
     */
    public void deleteScheduleJob(String jobName, String jobGroup) {
        try {
//            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName,triggerGroup);
//            scheduler.pauseTrigger(triggerKey);  //停止触发器
//            scheduler.unscheduleJob(triggerKey); //停止调度
            scheduler.deleteJob(createJobKey(jobName, jobGroup)); //删除任务,这一步会自动停止调度
        } catch (SchedulerException e) {
            log.error("删除定时任务失败", e);
            throw new ScheduleException("删除定时任务失败");
        }
    }

}
