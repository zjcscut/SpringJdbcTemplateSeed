package cn.zjc;

import cn.zjc.dao.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    public void findAllUsers(){
        assertEquals(1, userRepository.findAll().size());
        log.error("List<User> size:{}" ,userRepository.findAll().size());
    }
}
