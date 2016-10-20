package cn.zjc.eventbus;

import cn.zjc.dao.EventBusDefDao;
import cn.zjc.entity.EventBusDef;
import cn.zjc.eventbus.constant.EventConst;
import cn.zjc.eventbus.listerner.DeadEventListerner;
import cn.zjc.utils.SpringContextsUtil;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @description
 */
@Service
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
		Date now = new Date();
		log.debug(String.format(" %tF %tT EventBus配置正在启动.....", now, now));
		executorService = Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
		List<EventBusDef> eventBusDefs = eventBusDefDao.queryAll();
		for (EventBusDef def : eventBusDefs) {
			String busName = def.getEventBusName();
			EventBus eventBus;
			if (!eventBusMap.containsKey(busName)) {
				if (EventConst.IS_ASYNC == def.getIsAsync()) { //异步
					eventBus = new AsyncEventBus(executorService);
				} else { //同步
					eventBus = new EventBus();
				}
				eventBusMap.put(busName, eventBus);
			} else {
				eventBus = eventBusMap.get(busName);
			}
			String listerner = def.getListernerClassName();
			try {
				eventBus.register(SpringContextsUtil.getBean(listerner));
			} catch (Exception e) {
				log.error(String.format(" %tF %tT EventBus配置失败,配置失败的监听器为:%s", now, now, listerner));
			}
		}

		EventBus defaultEventBus = new EventBus(DEFAULT_EVENTBUS_NAME);
		eventBusMap.put(DEFAULT_EVENTBUS_NAME, defaultEventBus);
		defaultEventBus.register(new DeadEventListerner());

		log.debug(String.format(" %tF %tT EventBus配置正在启动成功", now, now));
	}

	private void release() {
		if (executorService != null && !executorService.isShutdown()) {
			executorService.shutdown();
		}
	}
}
