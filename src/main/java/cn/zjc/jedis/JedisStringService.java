package cn.zjc.jedis;

import org.springframework.stereotype.Service;

/**
 * @author zhangjinci
 * @version 2016/11/4 16:17
 * @function
 */
@Service
public class JedisStringService extends AbstractJedisService<String, String> {
    @Override
    protected Class<String> getValueType() {
        return String.class;
    }
}
