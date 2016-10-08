package cn.zjc.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zjc
 * @version 2016/10/8 23:25
 * @description Druid配置
 */

@Configuration
public class DruidConfiguration {


	//注册一个StatViewServlet
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
