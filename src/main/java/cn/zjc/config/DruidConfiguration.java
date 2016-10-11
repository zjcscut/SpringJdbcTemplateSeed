package cn.zjc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

	@Autowired
	private DruidBasicConfig druidBasicConfig; //注入Druid基础配置

	//Druid数据源基础配置
	private void basicDruidConfig(DruidDataSource dataSource){
		dataSource.setInitialSize(druidBasicConfig.getInitialSize());
		dataSource.setMaxActive(druidBasicConfig.getMaxActive());
		dataSource.setMinIdle(druidBasicConfig.getMinIdle());
		dataSource.setMaxWait(druidBasicConfig.getMaxWait());
		dataSource.setMinEvictableIdleTimeMillis(druidBasicConfig.getMinEvictableIdleTimeMillis());
		dataSource.setValidationQuery(druidBasicConfig.getValidationQuery());
		dataSource.setTestWhileIdle(druidBasicConfig.getTestWhileIdle());
		dataSource.setTestOnBorrow(druidBasicConfig.getTestOnBorrow());
		dataSource.setPoolPreparedStatements(druidBasicConfig.getPoolPreparedStatements());
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(druidBasicConfig.getMaxPoolPreparedStatementPerConnectionSize());
		try {
			dataSource.setFilters(druidBasicConfig.getFilters());
		} catch (SQLException e) {
			throw new RuntimeException("set druid filters fail:" + e.getMessage());
		}
		dataSource.setRemoveAbandoned(druidBasicConfig.getRemoveAbandoned());
		dataSource.setRemoveAbandonedTimeout(druidBasicConfig.getRemoveAbandonedTimeout());
	}

    //主数据源
	@Bean(name = "primaryDataSource")
	@Qualifier("primaryDataSource")
	public DataSource primaryDataSource(){

	}

	//配置Druid数据源
	@Bean
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(environment.getProperty("spring.datasource.url"));
		dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
		dataSource.setUsername(environment.getProperty("spring.datasource.username"));//用户名
		dataSource.setPassword(environment.getProperty("spring.datasource.password"));//密码
		basicDruidConfig(dataSource);
		return dataSource;
	}


	//注册一个StatViewServlet
	//ip:host/druid/
	@Bean
	public ServletRegistrationBean DruidServlet() {
		ServletRegistrationBean servletRegistrationBean
				= new ServletRegistrationBean(new StatViewServlet(),environment.getProperty("spring.druid.stat.url")); //添加一个Servlet
		//添加初始化参数：initParams
		//添加白名单
		servletRegistrationBean.addInitParameter("allow", environment.getProperty("spring.druid.stat.url"));
		//添加黑名单(优先于白名单)
		servletRegistrationBean.addInitParameter("deny", environment.getProperty("spring.druid.stat.deny"));
		//登录查看信息的账号密码
		servletRegistrationBean.addInitParameter("loginUsername", environment.getProperty("spring.druid.stat.loginUsername"));  //Druid后台登录账号
		servletRegistrationBean.addInitParameter("loginPassword", environment.getProperty("spring.druid.stat.loginPassword")); //Druid后台登录密码
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
		filterRegistrationBean.addUrlPatterns(environment.getProperty("spring.druid.webfilter.url"));
		//忽略的资源文件
		filterRegistrationBean.addInitParameter("exclusions", environment.getProperty("spring.druid.webfilter.exclusions"));
		return filterRegistrationBean;
	}
}
