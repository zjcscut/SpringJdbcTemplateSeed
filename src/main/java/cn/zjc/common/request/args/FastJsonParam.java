package cn.zjc.common.request.args;

import java.lang.annotation.*;

/**
 * @author zhangjinci
 * @version 2016/11/2 14:07
 * @function
 */
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FastJsonParam {
}
