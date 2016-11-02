package cn.zjc.common.request.args;

import cn.zjc.common.request.json.FastJsonObjectWrapper;
import com.alibaba.fastjson.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;


/**
 * @author zhangjinci
 * @version 2016/10/31 19:23
 * @function
 */
@Component
public class FastJsonArgumentResolver implements HandlerMethodArgumentResolver {

    private final static String APPLICATION_JSON = "application/json";
    private final static String APPLICATION_HTTP_METHOD = "POST";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(FastJsonParam.class);
    }


    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        if (webDataBinderFactory == null) {
            return null;
        }
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if (!request.getContentType().contains(APPLICATION_JSON) || !APPLICATION_HTTP_METHOD.equalsIgnoreCase(request.getMethod())) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            char[] buf = new char[1024];
            int rd;
            while ((rd = reader.read(buf)) != -1) {
                builder.append(buf, 0, rd);
            }
            if (FastJsonObjectWrapper.class.isAssignableFrom(methodParameter.getParameterType())) {
                return new FastJsonObjectWrapper(JSON.parseObject(builder.toString()));
            } else {
                return JSON.parseObject(builder.toString(), methodParameter.getParameterType());
            }
        }
    }
}
