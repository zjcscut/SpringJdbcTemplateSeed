package cn.zjc.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author zjc
 * @version 2016/10/17 1:04
 * @description 继承WebMvcConfigurerAdapter,配置拦截器
 */
@Configuration
public class WebInterceptorAdapter extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 多个拦截器组成一个拦截器链,按顺序执行
		// addPathPatterns 用于添加拦截规则
		// excludePathPatterns 用户排除拦截
		registry.addInterceptor(new PreRequestInterceptor()).addPathPatterns("/**"); //匹配全部路径
		super.addInterceptors(registry);
	}
}
