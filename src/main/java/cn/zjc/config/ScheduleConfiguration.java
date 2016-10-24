package cn.zjc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:36
 * @function 任务调度配置
 */
@Configuration
public class ScheduleConfiguration {

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        return new SchedulerFactoryBean();
    }
}
