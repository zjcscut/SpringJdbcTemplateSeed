package cn.zjc.config;

import cn.zjc.common.converter.StringToDateConverter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author zhangjinci
 * @version 2016/10/31 15:45
 * @function 全局表单类型转换器
 */
@Configuration
public class GlobalConverterConfig implements InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        addConversionConfig();
    }

    private void addConversionConfig() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter.getWebBindingInitializer();
        if (initializer.getConversionService() != null) {
            GenericConversionService genericConversionService = (GenericConversionService) initializer.getConversionService();
            genericConversionService.addConverter(new StringToDateConverter()); //这里加入的转换器是全局的，可以添加多个
        }
    }

    @Bean
    public WebBindingInitializer config(){
      return new Config();
    }


    public class Config implements WebBindingInitializer {

        public Config() {
        }

        @Override
        public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
            webDataBinder.registerCustomEditor(Date.class,new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),true));
        }
    }

}
