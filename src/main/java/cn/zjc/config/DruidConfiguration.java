package cn.zjc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
	private void basicDruidConfig(DruidDataSource dataSource) {
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
	@Bean(name = "masterDataSource")
	@Primary //指定在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@Autowire注解报错
	public DataSource masterDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(environment.getProperty("spring.datasource.master.url"));
		dataSource.setDriverClassName(environment.getProperty("spring.datasource.master.driver-class-name"));
		dataSource.setUsername(environment.getProperty("spring.datasource.master.username"));//用户名
		dataSource.setPassword(environment.getProperty("spring.datasource.master.password"));//密码
		basicDruidConfig(dataSource);
		return dataSource;
	}

	//从数据源
	@Bean(name = "slaverDataSource")
	public DataSource slaverDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(environment.getProperty("spring.datasource.salver.url"));
		dataSource.setDriverClassName(environment.getProperty("spring.datasource.salver.driver-class-name"));
		dataSource.setUsername(environment.getProperty("spring.datasource.salver.username"));//用户名
		dataSource.setPassword(environment.getProperty("spring.datasource.salver.password"));//密码
		basicDruidConfig(dataSource);
		return dataSource;
	}

	//配置Druid动态数据源
	@Bean
	@ConditionalOnBean(name = {"masterDataSource", "slaverDataSource"})
	public DynamicDataSource dynamicDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
											   @Qualifier("slaverDataSource") DataSource slaverDataSource) {
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DataSourceType.MASTER, masterDataSource);
		targetDataSources.put(DataSourceType.SLAVER, slaverDataSource);
		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		dynamicDataSource.setTargetDataSources(targetDataSources);  //配置动态数据源
		dynamicDataSource.setDefaultTargetDataSource(masterDataSource); //配置默认数据源
		return dynamicDataSource;
	}


	//注册一个StatViewServlet
	//ip:host/druid/
	@Bean
	public ServletRegistrationBean DruidServlet() {
		ServletRegistrationBean servletRegistrationBean
				= new ServletRegistrationBean(new StatViewServlet(), environment.getProperty("spring.druid.stat.url")); //添加一个Servlet
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

	//配置jdbcTemplate
	@Bean
	public JdbcTemplate jdbcTemplate(DynamicDataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

//	//配置事务
//	@Bean
//	public PlatformTransactionManager platformTransactionManager(DynamicDataSource dynamicDataSource) {
//		return new DataSourceTransactionManager(dynamicDataSource);
//	}
}
