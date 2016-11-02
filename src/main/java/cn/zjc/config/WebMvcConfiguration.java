package cn.zjc.config;

import cn.zjc.common.converter.StringToDateConverter;
import cn.zjc.common.request.args.CustomArgumentResolver;
import cn.zjc.common.request.args.FastJsonArgumentResolver;
import cn.zjc.interceptor.PreRequestInterceptor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author zhangjinci
 * @version 2016/10/31 17:16
 * @function 自定义参数处理器配置
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    //添加自定义参数转换器
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CustomArgumentResolver());  //添加自定义参数处理器,可以添加多个
        argumentResolvers.add(new FastJsonArgumentResolver());
    }

    //添加自定义类型转换器
    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addConverter(new StringToDateConverter());
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter converter:converters){
            System.out.println(converter.toString());
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链,按顺序执行
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new PreRequestInterceptor()).addPathPatterns("/**"); //匹配全部路径
        super.addInterceptors(registry);
    }



}
