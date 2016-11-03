package cn.zjc.service;

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

	public void sendMessage() {
		Message message =
				MessageBuilder.withBody("Hello SpringBoot-AMPQ".getBytes())
						.setContentEncoding("UTF-8")
						.build();
		rabbitTemplate.send("directExchange", "route-key", message, new CorrelationData(String.valueOf(System.currentTimeMillis())));
	}

	/**
	 * {@link cn.zjc.config.RabbitMQConfiguration}
	 */
	@RabbitListener(queues = "myQueue", containerFactory = "simpleRabbitListenerContainerFactory")
	public void listerning(Message message) {
		log.error("receive queue:myQueue message --------------");

		System.out.println("current thread:" + Thread.currentThread().getName() + ";message=====>:" + new String(message.getBody()));

	}
}
