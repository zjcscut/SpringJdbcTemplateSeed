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

	@Pointcut("@annotation(cn.zjc.config.TargetDataSource)")
	public void point() {
	}

	@Before("point()")
	public void setDataSourceContext(JoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature(); //获取方法签名
		if (methodSignature != null) {
			Method method = methodSignature.getMethod();
			if (method != null && method.isAnnotationPresent(TargetDataSource.class)) {     //出现过此注解
				TargetDataSource targetDataSource = method.getDeclaredAnnotation(TargetDataSource.class);
				DataSourceContextHolder.setDataSourceType(targetDataSource.value());

			}
		}
	}


}
