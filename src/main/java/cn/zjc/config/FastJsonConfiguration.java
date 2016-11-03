//package cn.zjc.config;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.alibaba.fastjson.serializer.ValueFilter;
//import com.alibaba.fastjson.support.config.FastJsonConfig;
//import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author zhangjinci
// * @version 2016/10/10 9:29
// * @function fastjson配置
// */
//@Configuration
//@ConditionalOnClass({JSON.class})
//public class FastJsonConfiguration {
//
//    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
//
//
//    @Configuration
//    @ConditionalOnClass({FastJsonHttpMessageConverter4.class})
//    @ConditionalOnProperty(
//            name = {"spring.http.converters.preferred-json-mapper"},
//            havingValue = "fastjson",
//            matchIfMissing = true
//    )
//    protected static class FastJson2HttpMessageConverterConfiguration {
//
//        protected FastJson2HttpMessageConverterConfiguration() {
//        }
//
//        @Bean
//        @ConditionalOnMissingBean({FastJsonHttpMessageConverter4.class})
//        public FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter4() {
//            FastJsonHttpMessageConverter4 converter = new FastJsonHttpMessageConverter4();
//            FastJsonConfig config = new FastJsonConfig();
//            config.setSerializerFeatures(
//                    SerializerFeature.PrettyFormat,
//                    SerializerFeature.WriteMapNullValue
//            );
//            ValueFilter valueFilter = (object, name, value) -> {
//                if (null == value) {
//                    value = "";
//                }
//                return value;
//            };
//            config.setSerializeFilters(valueFilter);
//            converter.setFastJsonConfig(config);
//            converter.setDefaultCharset(DEFAULT_CHARSET);
//            List<MediaType> supportedMediaTypes = new ArrayList<>(2);
//            supportedMediaTypes.add(MediaType.APPLICATION_JSON);
//            supportedMediaTypes.add(new MediaType("application", "*+json"));
//            converter.setSupportedMediaTypes(supportedMediaTypes);
//            return converter;
//        }
//    }
//}
