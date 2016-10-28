package cn.zjc.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;



/**
 * @author zhangjinci
 * @version 2016/10/24 11:36
 * @function 任务调度配置
 */
@Configuration
public class ScheduleConfiguration {

    @Bean(name = "schedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        ClassPathResource quartzResource = new ClassPathResource("quartz/quartz.properties"); //quartz的配置文件
        factoryBean.setConfigLocation(quartzResource);
        return factoryBean;
    }
}
