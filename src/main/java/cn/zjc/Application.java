package cn.zjc;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author zhangjinci
 * @version 2016/10/7 16:54
 * @function
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
		SpringApplication.run(Application.class,args);
    }

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));//用户名
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));//密码
        dataSource.setInitialSize(5);
        dataSource.setMaxActive(20);
        dataSource.setMinIdle(3);
        dataSource.setMaxWait(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestWhileIdle(false);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(50);
        try {
            dataSource.setFilters("stat");
        } catch (SQLException e) {
            throw new RuntimeException("set druid filters fail:" + e.getMessage());
        }
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(1800);
        return dataSource;
    }
}