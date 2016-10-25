package cn.zjc.schedule.support;

import cn.zjc.schedule.entity.ScheduleJob;
import cn.zjc.exception.ScheduleException;
import cn.zjc.schedule.listerner.ScheduleListerner;
import cn.zjc.utils.Assert;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Date;

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
    @Qualifier(value = "schedulerFactoryBean")
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 获取触发器key
     *
     * @param trggerName   任务名
     * @param triggerGroup 任务组
     * @return TriggerKey
     */
    public TriggerKey getTriggerKey(String trggerName, String triggerGroup) {
        return TriggerKey.triggerKey(trggerName, triggerGroup);
    }

    /**
     * 创建JobKey
     *
     * @param jobName  任务名
     * @param jobGroup 任务组
     * @return JobKey
     */
    public JobKey createJobKey(String jobName, String jobGroup) {
        return JobKey.jobKey(jobName, jobGroup);
    }

    /**
     * 获取表达式触发器
     *
     * @return cron trigger
     */
    public CronTrigger getCronTrigger(String triggerName, String triggerGroup) {
        try {
            TriggerKey triggerKey = getTriggerKey(triggerName, triggerGroup);
            return (CronTrigger) schedulerFactoryBean.getScheduler().getTrigger(triggerKey);
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
        createScheduleJob(scheduleJob.getJobName(), scheduleJob.getJobGroup(), scheduleJob.getTargetClassNmae(), scheduleJob.getJobName(), scheduleJob.getJobGroup(),
                scheduleJob.getCronExpression(), scheduleJob.getRunType(), scheduleJob.getExecuteTime(), scheduleJob.getId());
    }


    /**
     * 创建定时任务
     *
     * @param jobName        the job name
     * @param jobGroup       the job group
     * @param cronExpression the cron expression
     * @param param          the param
     *                       <p>
     *                       triggerName使用jobName
     */
    @SuppressWarnings("unchecked")
    public void createScheduleJob(String jobName, String jobGroup, String targetClassName, String triggerName, String triggerGroup,
                                  String cronExpression, Integer runType, Date startTime, Object param) {
        if (StringUtils.isBlank(jobGroup)) {
            jobGroup = DEFAULT_JOB_GROUP;
        }
        if (StringUtils.isBlank(triggerGroup)) {
            triggerGroup = DEFAULT_TRIGGER_GROUP;
        }
        try {
            TriggerKey triggerKey = getTriggerKey(triggerName, triggerGroup);
            if (triggerKey == null) {
                //注入job class
                Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(targetClassName);
                //构建job信息
                JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();
                //放入参数，运行时的方法可以获取
                jobDetail.getJobDataMap().put(ScheduleJob.JOB_PARAM_KEY, param);
                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
                //按新的cronExpression表达式构建一个新的trigger
                CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup).startAt(startTime)
                        .withSchedule(scheduleBuilder).build();
                Scheduler scheduler = schedulerFactoryBean.getScheduler();
                Assert.notNull(scheduler);
                if (runType == 1) { //自动任务,则让其加入
                    scheduler.scheduleJob(jobDetail, trigger);
                }
            } else {
                updateScheduleJob(jobName, jobGroup, cronExpression);
            }
        } catch (SchedulerException | ClassNotFoundException e) {
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
            schedulerFactoryBean.getScheduler().triggerJob(createJobKey(jobName, jobGroup));
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
            schedulerFactoryBean.getScheduler().pauseJob(createJobKey(jobName, jobGroup));
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
            schedulerFactoryBean.getScheduler().resumeJob(createJobKey(jobName, jobGroup));
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
            CronTrigger cronTrigger = (CronTrigger) schedulerFactoryBean.getScheduler().getTrigger(triggerKey);
            //按新的cronExpression表达式重新构建Crontrigger
            cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            schedulerFactoryBean.getScheduler().rescheduleJob(triggerKey, cronTrigger);
        } catch (SchedulerException e) {
            log.error("更新定时任务失败", e);
            throw new ScheduleException("更新定时任务失败");
        }
    }

    /**
     * 删除定时任务
     *
     * @param jobName  任务名
     * @param jobGroup 任务组
     */
    public void deleteScheduleJob(String jobName, String jobGroup) {
        try {
            schedulerFactoryBean.getScheduler().deleteJob(createJobKey(jobName, jobGroup)); //删除任务,这一步会自动停止调度
        } catch (SchedulerException e) {
            log.error("删除定时任务失败", e);
            throw new ScheduleException("删除定时任务失败");
        }
    }


    /**
     * 开始调度
     */
    public void start() {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            Assert.notNull(scheduler);
            if (!scheduler.isStarted()) {
                scheduler.getListenerManager().addJobListener(new ScheduleListerner());
                scheduler.start();
            }
        } catch (SchedulerException e) {
            throw new ScheduleException(e.getMessage(), e);
        }
    }

    /**
     * 停止调度
     *
     * @param isForce isForce 如果为真，则会强制停止调度,如果为假，则会等待所有任务完成后停止调度
     */
    public void shutdown(Boolean isForce) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            Assert.notNull(scheduler);
            if (!scheduler.isShutdown()) {
                scheduler.shutdown(!isForce);
            }
        } catch (SchedulerException e) {
            throw new ScheduleException(e.getMessage(), e);
        }
    }


}
