package cn.zjc.schedule.init;

import cn.zjc.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:52
 * @function
 */
@Component
public class ScheduleInit implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(ScheduleInit.class);

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("init Schedule Service...");
        scheduleService.initScheduleService();
        log.debug("init Schedule Service succeeded!");
    }
}
