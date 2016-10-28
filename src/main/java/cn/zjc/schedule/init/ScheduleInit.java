package cn.zjc.schedule.init;

import cn.zjc.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:52
 * @function 调度初始化
 */
@Component
public class ScheduleInit implements InitializingBean, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(ScheduleInit.class);

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("Quartz调度服务启动...");
        scheduleService.initScheduleService();
        log.debug("Quartz调度服务启动成功...!");
    }

    @Override
    public void destroy() throws Exception {
        log.debug("Quartz调度服务关闭...");
        scheduleService.shutScheduleService();
        log.debug("Quartz调度服务关闭成功...");
    }
}
