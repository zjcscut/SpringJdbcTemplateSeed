package cn.zjc.config;

import cn.zjc.security.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zjc
 * @version 2016/10/18 22:53
 * @description 安全配置
 */
@Configuration
public class SecuritySupportConfig {

	//配置Xss过滤器
	@Bean
	public FilterRegistrationBean XssFilterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean
				= new FilterRegistrationBean(new XssFilter());
		filterRegistrationBean.setName("XssFilter");
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}

}
