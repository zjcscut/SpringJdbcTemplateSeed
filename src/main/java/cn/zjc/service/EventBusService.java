package cn.zjc.service;

import cn.zjc.dao.EventBusDefDao;
import cn.zjc.entity.EventBusDef;
import cn.zjc.eventbus.constant.EventConst;
import cn.zjc.eventbus.listerner.DeadEventListerner;
import cn.zjc.eventbus.listerner.StrMessageListerner;
import cn.zjc.utils.Assert;
import cn.zjc.utils.SpringContextsUtil;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zjc
 * @version 2016/10/20 23:13
 * @description 消息总线服务类
 */
@Service
@ConditionalOnBean(value = StrMessageListerner.class)
public class EventBusService implements InitializingBean, DisposableBean {

    @Autowired
    private EventBusDefDao eventBusDefDao;

    private static final String DEFAULT_EVENTBUS_NAME = "default_eventbus";

    private static final Logger log = LoggerFactory.getLogger(EventBusService.class);

    private static Map<String, EventBus> eventBusMap = new HashMap<>();

    private static ExecutorService executorService;

    private static final Integer DEFAULT_POOL_SIZE = 8;


    @Override
    public void destroy() throws Exception {
        release();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    private void init() {
        log.debug("EventBus配置正在启动.....");
        executorService = Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
        List<EventBusDef> eventBusDefs = eventBusDefDao.queryAll();
        for (EventBusDef def : eventBusDefs) {
            String busName = def.getEventBusName();
            EventBus eventBus;
            if (!eventBusMap.containsKey(busName)) {
                if (EventConst.IS_ASYNC == def.getIsAsync()) { //异步
                    eventBus = new AsyncEventBus(busName,executorService);
                } else { //同步
                    eventBus = new EventBus(busName);
                }
                eventBusMap.put(busName, eventBus);
            } else {
                eventBus = eventBusMap.get(busName);
            }
            String listerner = def.getListernerClassName();
            Class<?> clazz = null;
            try {
                clazz = Class.forName(listerner);
                eventBus.register(clazz.newInstance());  //这里只需要传入class的实例,不一定需要Spring的构件
            } catch (Exception e) {
                log.error(String.format("EventBus配置失败,配置失败的监听器为:%s", listerner));
            }
        }
        //deadEvent eventbus
        EventBus defaultEventBus = new EventBus(DEFAULT_EVENTBUS_NAME);
        eventBusMap.put(DEFAULT_EVENTBUS_NAME, defaultEventBus);
        //listern deadEvent
        defaultEventBus.register(new DeadEventListerner());
        log.debug("EventBus配置正在启动成功");
    }

    private void release() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    public EventBus getInstance(String eventBusName) {
        return eventBusMap.get(eventBusName);
    }

    //deadevent
    public EventBus getInstance() {
        return eventBusMap.get(DEFAULT_EVENTBUS_NAME);
    }

    public void register(String eventBusName, String listernerName, Integer isAsync) { //注册EventBus不记录在数据库
        Assert.isTrue(!eventBusMap.containsKey(eventBusName), "EventBus %s has been registered", eventBusName);
        EventBus newEventBus;
        if (isAsync == EventConst.IS_ASYNC) {
            newEventBus = new AsyncEventBus(executorService);
        } else {
            newEventBus = new EventBus();
        }
        eventBusMap.put(eventBusName, newEventBus);
        try {
            newEventBus.register(SpringContextsUtil.getBean(Class.forName(listernerName)));
        } catch (Exception e) {
            log.error("register eventbus failed,eventbusName:{},listernerName:{}", eventBusName, listernerName);
        }

    }

    public void unregister(String eventBusName, String listernerName) { //只卸载不删除数据库记录
        Assert.notNull(eventBusMap.get(eventBusName), "eventBus %s has not been registered", eventBusName);
        try {
            eventBusMap.get(eventBusName).unregister(Class.forName(listernerName));
        } catch (ClassNotFoundException e) {
            log.error("unregister eventBus {} failed", eventBusName);
        }

    }

    public void unregister(String eventBusName, Class<?> listernerClass) { //只卸载不删除数据库记录
        Assert.notNull(eventBusMap.get(eventBusName), "eventBus %s has not been registered", eventBusName);
        eventBusMap.get(eventBusName).unregister(listernerClass);
    }

    public void delEventBus(String eventBusName) { //先卸载后删除数据库记录
        EventBusDef def = eventBusDefDao.querySingleByName(eventBusName);
        if (def != null) {
            unregister(def.getEventBusName(), def.getListernerClassName());
            eventBusDefDao.delete(def);
        }
    }

    public void addEventBus(String eventBusName,String eventName,String listernerClassName,String memo,Integer isAsync) { //注册并且把EventBus记录在数据库
        register(eventBusName,listernerClassName,isAsync);
        EventBusDef def = new EventBusDef();
        def.setEventName(eventName);
        def.setEventBusName(eventBusName);
        def.setCreateTime(new Date());
        def.setIsAvailable(EventConst.IS_AVAILABLE);
        def.setIsAsync(isAsync);
        def.setMemo(memo);
        def.setListernerClassName(listernerClassName);
        eventBusDefDao.save(def);
    }

    public void addEventBus(String eventBusName,String listernerClassName,Integer isAsync) { //注册并且把EventBus记录在数据库
        register(eventBusName,listernerClassName,isAsync);
        EventBusDef def = new EventBusDef();
        def.setEventBusName(eventBusName);
        def.setCreateTime(new Date());
        def.setIsAvailable(EventConst.IS_AVAILABLE);
        def.setIsAsync(isAsync);
        def.setListernerClassName(listernerClassName);
        eventBusDefDao.save(def);
    }
}
