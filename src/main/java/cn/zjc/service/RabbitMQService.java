package cn.zjc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
		rabbitTemplate.convertAndSend("Hello SpringBoot-RabbitMQ");
	}

	/**
	 * @link cn.zjc.config.RabbitMQConfiguration
	 */
	@RabbitListener(queues = "myQueue")
	public void listerning(Message message) {
		log.error("receive queue:myQueue message --------------");
		System.out.println("message=====>:" + new String(message.getBody()));

	}
}
