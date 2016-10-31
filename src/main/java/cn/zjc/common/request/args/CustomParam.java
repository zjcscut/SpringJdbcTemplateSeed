package cn.zjc.common.request.args;

import java.lang.annotation.*;

/**
 * @author zhangjinci
 * @version 2016/10/31 12:10
 * @function 用于接收特殊表单参数的参数注解
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomParam {

    String value() default "";
}
