//package cn.zjc.config;
//
//import cn.zjc.common.converter.StringToDateConverter;
//import cn.zjc.common.request.args.CustomArgumentResolver;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.format.FormatterRegistry;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
//
//import java.util.List;
//
///**
// * @author zhangjinci
// * @version 2016/10/31 17:16
// * @function 自定义参数处理器配置
// */
//@Configuration
//public class CustomArgumentResolverConfig extends WebMvcConfigurerAdapter {
//
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(new CustomArgumentResolver());  //添加自定义参数处理器,可以添加多个
//    }
//
//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new StringToDateConverter());  //添加自定义转换器
//    }
//
//
//}
