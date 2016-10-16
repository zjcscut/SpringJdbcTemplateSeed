package cn.zjc.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zhangjinci
 * @version 2016/10/12 18:07
 * @function 数据源切换切面函数
 */
@Aspect
@Component
public class DynamicDataSourceAspect {

	//匹配所有被@TargetDataSource标注的执行中的方法
    @Pointcut("@annotation(cn.zjc.config.TargetDataSource)")
    public void point() {
    }

    @Before("point()")
    public void setDataSourceContext(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature(); //获取方法签名
        if (methodSignature != null) {
            Method method = methodSignature.getMethod();
            if (method != null && method.isAnnotationPresent(TargetDataSource.class)) {
                TargetDataSource targetDataSource = method.getDeclaredAnnotation(TargetDataSource.class);
                DataSourceContextHolder.setDataSourceType(targetDataSource.value()); //切换数据源类型
            }
        }
    }


}
