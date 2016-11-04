package cn.zjc.service;

import cn.zjc.entity.User;
import cn.zjc.jedis.JedisStringService;
import cn.zjc.mq.support.MessageBuildHelper;
import cn.zjc.utils.FastJsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zjc
 * @version 2016/10/24 0:58
 * @description
 */
@Service
public class RabbitMQService {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private JedisStringService jedisStringService;

    public void sendMessage() {
        User u = new User("zjc", 23);
        Message message =
                MessageBuilder.withBody(FastJsonUtils.toJson(u).getBytes())
                        .setContentEncoding("UTF-8")
                        .setContentType("application/json")
                        .build();
        CorrelationData correlationData = MessageBuildHelper.createCorrelationData("directExchange", "routekey");
        jedisStringService.set(correlationData.getId(), FastJsonUtils.toJson(message));
        rabbitTemplate.send("directExchange", "routekey", message, correlationData);
    }

    /**
     * {@link cn.zjc.config.RabbitMQConfiguration}
     */
//	@RabbitListener(admin = "amqpAdmin", containerFactory = "simpleRabbitListenerContainerFactory",
//			bindings = @QueueBinding(
//					value = @Queue(value = "myQueue", durable = "true"),
//					exchange = @Exchange(value = "directExchange", type = ExchangeTypes.DIRECT, durable = "true"),
//					key = "route-key"
//			))
    public void listerning(Message message) {
        log.error("receive queue:myQueue message --------------");

        System.out.println("current thread:" + Thread.currentThread().getName() + ";message=====>:" + new String(message.getBody()));

    }
}
