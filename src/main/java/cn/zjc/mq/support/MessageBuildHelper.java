package cn.zjc.mq.support;

import cn.zjc.utils.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * @author zhangjinci
 * @version 2016/11/4 15:35
 * @function 创建每个发送消息的绑定id,可以防止消息在宕机情况下丢失
 */
public class MessageBuildHelper {

    private static final String SEPERATOR = ":";

    public static String buildCorrelationData(final String exchanger, final String routingKey, final String id) {
        Assert.notBlank(exchanger, "exchanger must not be null");
        Assert.notBlank(routingKey, "routingKey must not be null");
        if (StringUtils.isBlank(id)) {
            return exchanger + SEPERATOR + routingKey + SEPERATOR + System.currentTimeMillis();
        }
        return exchanger + SEPERATOR + routingKey + SEPERATOR + id;
    }

    public static CorrelationData createCorrelationData(final String exchanger, final String routingKey, final String id) {
        return new CorrelationData(buildCorrelationData(exchanger, routingKey, id));
    }

    public static CorrelationData createCorrelationData(final String exchanger, final String routingKey) {
        return new CorrelationData(buildCorrelationData(exchanger, routingKey, null));
    }
}
