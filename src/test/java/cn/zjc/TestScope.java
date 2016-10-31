package cn.zjc;

import cn.zjc.common.aop.resubmit.ResubmitHandler;
import cn.zjc.config.DataSourceContextHolder;
import cn.zjc.config.DataSourceType;
import cn.zjc.config.TargetDataSource;
import cn.zjc.dao.UserRepository;
import cn.zjc.entity.User;
import cn.zjc.schedule.entity.ScheduleJob;
import cn.zjc.service.EventBusService;
import cn.zjc.service.RabbitMQService;
import cn.zjc.service.ScheduleService;
import cn.zjc.service.UserService;
import cn.zjc.utils.SpringContextsUtil;
import com.google.common.eventbus.EventBus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author zhangjinci
 * @version 2016/10/7 18:01
 * @function
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public class TestScope {

    private Logger log = LoggerFactory.getLogger(TestScope.class);

    @Autowired
    private ScheduleService scheduleService;

	@Autowired
	private UserService userService;

//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private EventBusService eventBusService;
//
//	@Autowired
//	private RabbitMQService rabbitMQService;
//
    @Test
    public void findAllUsers(){
       userService.saveUser(new User("zjcscut",23));
    }
//
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
//
//	@Test
//	@TargetDataSource(value = DataSourceType.MASTER)
//	public void findAllUsers() {
//		Integer c = jdbcTemplate.queryForObject("Select count(*) from user", Integer.class);
//		System.out.println("user count==> " + c);
//	}
//
//	@Test
//	@TargetDataSource(value = DataSourceType.SLAVER)
//	public void findAllUsersFromJdbc() {
//		DataSourceContextHolder.setDataSourceType(DataSourceType.SLAVER);
//		Integer c = jdbcTemplate.queryForObject("Select count(*) from user", Integer.class);
//		System.out.println("user count==> " + c);
//	}
//
//
//	@Autowired
//	private UserService userService;
//
//	@Test
//	public void testMaster() throws Exception {
//		assertTrue(6 == userService.selectAllCount());
//	}
//
//	@Test
//	public void testSlaver() throws Exception {
//		assertTrue(1 == userService.selectAllCountFromSlaver());
//	}
//
//	@Test
//	public void testTransaction() throws Exception {
//		userService.testTransaction();
//	}
//
//	@Test
//	public void testEventBusService() throws Exception {
////        eventBusService.addEventBus("z_eventbus", "cn.zjc.eventbus.listerner.StrMessageListerner", 1);
//
//		EventBus eventBus = eventBusService.getInstance("z_eventbus");
//
//		eventBus.post("hello world!");
//
//		System.in.read();
//	}
//
//	@Test
//	public void testRabbitMQ() throws Exception {
//		rabbitMQService.sendMessage();
//		try {
//			Thread.sleep(5000);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.in.read();
//	}
//
//	@Test
//	public void testApplicationContextUtils()throws Exception{
//		Object o = SpringContextsUtil.getBean("strMessageListerner");
//		System.out.println(o);
//	}

    @Test
    public void testReflect() throws Exception {
        List<ScheduleJob> jobs = scheduleService.queryAll();
        ScheduleJob job = scheduleService.queryByTaskId(1L);

        System.out.println("1111111");

        System.in.read();
    }

    @Test
	public void testNameQuery() throws Exception{
		ScheduleJob job = scheduleService.nameQuery("ZJC");

		System.out.println(job.getJobGroup());
	}

	@Test
    public void testAop(){
       userService.testAop();
    }

    @ResubmitHandler
    public void aop(){

    }

}
