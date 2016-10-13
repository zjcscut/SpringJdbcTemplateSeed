package cn.zjc.config;

import java.lang.annotation.*;

/**
 * @author zhangjinci
 * @version 2016/10/12 18:10
 * @function 数据源类型注解,方法级别注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {

    DataSourceType value() default DataSourceType.MASTER; //数据源类型,默认为主数据源
}
