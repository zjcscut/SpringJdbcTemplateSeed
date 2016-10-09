package cn.zjc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author zjc
 * @version 2016/10/8 23:25
 * @description Druid配置
 */

@Configuration
public class DruidConfiguration {

	@Autowired
	private Environment environment; //注入配置环境

	//配置Druid数据源
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


	//注册一个StatViewServlet
	//ip:host/druid/
	@Bean
	public ServletRegistrationBean DruidServlet() {
		ServletRegistrationBean servletRegistrationBean
				= new ServletRegistrationBean(new StatViewServlet(),"/druid/*"); //添加一个Servlet
		//添加初始化参数：initParams
		//添加白名单
		servletRegistrationBean.addInitParameter("allow", "localhost");
		//添加黑名单(优先于白名单)
		servletRegistrationBean.addInitParameter("deny", "192.168.1.1");
		//登录查看信息的账号密码
		servletRegistrationBean.addInitParameter("loginUsername", "admin");  //Druid后台登录账号
		servletRegistrationBean.addInitParameter("loginPassword", "123456"); //Druid后台登录密码
		//是否能够重置数据
		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}

	//注册一个WebStatFilter
	//匹配所有路径/*
	@Bean
	public FilterRegistrationBean druidStatFilter() {
		FilterRegistrationBean filterRegistrationBean
				= new FilterRegistrationBean(new WebStatFilter());
		//Url匹配规则
		filterRegistrationBean.addUrlPatterns("/*");
		//忽略的资源文件
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}
}
