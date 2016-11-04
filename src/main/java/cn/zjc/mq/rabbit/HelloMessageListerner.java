package cn.zjc.mq.rabbit;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author zhangjinci
 * @version 2016/11/3 17:58
 * @function 监听器实现 ChannelAwareMessageListener 可以实现手动确认消费是否成功
 * detail see {@link cn.zjc.config.RabbitMQConfiguration}
 */


@Component
public class HelloMessageListerner implements ChannelAwareMessageListener {

	private static final Logger log = LoggerFactory.getLogger(HelloMessageListerner.class);

	@Override
	@RabbitListener(admin = "amqpAdmin", containerFactory = "simpleRabbitListenerContainerFactory",
			bindings = @QueueBinding(
					value = @Queue(value = "myQueue", durable = "true"),
					exchange = @Exchange(value = "directExchange", type = ExchangeTypes.DIRECT, durable = "true"),
					key = "route-key"
			))
	public void onMessage(Message message, Channel channel) throws Exception {
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消费
        log.error("确认消费,消息为:===>" + new String(message.getBody()) + ";时间:" + System.currentTimeMillis());
	}
}
