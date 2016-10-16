package cn.zjc;

import cn.zjc.config.DataSourceContextHolder;
import cn.zjc.config.DataSourceType;
import cn.zjc.config.TargetDataSource;
import cn.zjc.dao.UserRepository;
import cn.zjc.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    private UserRepository userRepository;

//    @Test
//    public void findAllUsers(){
//        assertEquals(1, userRepository.findAll().size());
//        log.error("List<User> size:{}" ,userRepository.findAll().size());
//    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @TargetDataSource(value = DataSourceType.MASTER)
    public void findAllUsers() {
        Integer c = jdbcTemplate.queryForObject("Select count(*) from user", Integer.class);
        System.out.println("user count==> " + c);
    }

    @Test
    @TargetDataSource(value = DataSourceType.SLAVER)
    public void findAllUsersFromJdbc() {
        DataSourceContextHolder.setDataSourceType(DataSourceType.SLAVER);
        Integer c = jdbcTemplate.queryForObject("Select count(*) from user", Integer.class);
        System.out.println("user count==> " + c);
    }


    @Autowired
    private UserService userService;

    @Test
    public void testMaster() throws Exception{
       assertTrue(6 == userService.selectAllCount());
    }

    @Test
    public void testSlaver()throws  Exception{
        assertTrue(1 == userService.selectAllCountFromSlaver());
    }

    @Test
	public void testTransaction()throws Exception{
		userService.testTransaction();
	}

}
