package cn.zjc.common.aop.resubmit;

import java.lang.annotation.*;

/**
 * @author zhangjinci
 * @version 2016/10/31 10:50
 * @function 重复提交处理注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResubmitHandler {

    String codeName() default "code";

    String messageName() default "message";

    String codeContent() default "201";

    String messageContent() default "business is handling,please wait";
}
