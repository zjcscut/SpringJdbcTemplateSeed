package cn.zjc.schedule.factory;

import cn.zjc.schedule.entity.ScheduleJob;
import cn.zjc.service.ScheduleService;
import cn.zjc.utils.Assert;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:38
 * @function
 */
//@DisallowConcurrentExecution //不允许多个任务并发
public class JobFactory implements Job {

    private static final Logger log = LoggerFactory.getLogger(JobFactory.class);

    @Autowired
    private ScheduleService scheduleService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("JobFactory正在执行调度-----------------------------------");
    }
}
