package cn.zjc.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:36
 * @function 任务调度配置
 */
@Configuration
@Component
public class ScheduleConfiguration {

    @Bean
    @Qualifier(value = "schedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean() {
        return new SchedulerFactoryBean();
    }
}
