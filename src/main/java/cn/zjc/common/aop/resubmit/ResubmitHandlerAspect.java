package cn.zjc.common.aop.resubmit;

import cn.zjc.utils.FastJsonUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangjinci
 * @version 2016/10/31 10:53
 * @function 重复提交处理器切面
 */
@Component
@Aspect
public class ResubmitHandlerAspect {

    private static final ConcurrentHashMap<String, String> interfaceMap = new ConcurrentHashMap<>();

    private static final AtomicInteger count = new AtomicInteger(0);

    private static final Logger log = LoggerFactory.getLogger(ResubmitHandlerAspect.class);

    @Pointcut("@within(cn.zjc.common.aop.resubmit.ResubmitHandler)")
    public void classPointCut() {
    }

    @Pointcut("@annotation(cn.zjc.common.aop.resubmit.ResubmitHandler)")
    public void methodPointCut() {
    }

    @Around("classPointCut() || methodPointCut()")
    public Object resubmitHanlder(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        Object target = pjp.getTarget();   //目标对象
        Class<?> clazz = target.getClass(); //目标class
        Method method = ((MethodSignature) pjp.getSignature()).getMethod(); //当前执行方法
        if (clazz != null && method != null) {
            String methodName = clazz.getCanonicalName() + "." + method.getName();
            boolean isClzAnnotation = clazz.isAnnotationPresent(ResubmitHandler.class); //注解在类上
            boolean isMethondAnnotation = method.isAnnotationPresent(ResubmitHandler.class); //注解在方法上
            ResubmitHandler rh = null;
            if (isMethondAnnotation) { //方法上的注解覆盖类上的注解
                rh = method.getAnnotation(ResubmitHandler.class);
            } else if (isClzAnnotation) {
                rh = clazz.getAnnotation(ResubmitHandler.class);
            } else {
                return pjp.proceed();  //不存在注解，直接返回
            }
            if (rh != null) {
                if (interfaceMap.containsKey(methodName)) {
                    log.debug("使用ResubmitHandlerAspect进行重复请求拦截--当前执行的方法为:" + methodName + ";当前拦截次数为:" + count.getAndIncrement());
                    Map<String, String> resultMap = new HashMap<>();
                    resultMap.put(rh.codeName(), rh.codeContent());
                    resultMap.put(rh.messageName(), rh.messageContent());
                    return FastJsonUtils.toJson(resultMap);
                }
                interfaceMap.put(methodName, "sss");
                try {
                    result = pjp.proceed();
                } finally {
                    interfaceMap.remove(methodName);
                    count.set(0);
                }
            }
        }
        return result;
    }

}
