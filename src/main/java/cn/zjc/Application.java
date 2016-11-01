package cn.zjc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @author zhangjinci
 * @version 2016/10/7 16:54
 * @function
 */
@SpringBootApplication
@EnableTransactionManagement
@ImportResource(locations = {"classpath:dispatcher-servlet.xml"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}