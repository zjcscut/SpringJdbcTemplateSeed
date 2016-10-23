package cn.zjc.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


/**
 * @author zjc
 * @version 2016/10/24 0:38
 * @description
 */
@Configuration
public class RabbitMQConfiguration {

	@Autowired
	private Environment environment;

	@Bean
	public CachingConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setUsername(environment.getProperty("spring.custom.rabbitmq.username"));
		connectionFactory.setPassword(environment.getProperty("spring.custom.rabbitmq.password"));
		connectionFactory.setHost(environment.getProperty("spring.custom.rabbitmq.host"));
		connectionFactory.setPort(Integer.valueOf(environment.getProperty("spring.custom.rabbitmq.port")));
		connectionFactory.setVirtualHost(environment.getProperty("spring.custom.rabbitmq.virtual_port"));
		return connectionFactory;
	}

	//设置交换机
	/* 针对消费者配置
	   FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
	   HeadersExchange ：通过添加属性key-value匹配
	   DirectExchange:按照routingkey分发到指定队列
	   TopicExchange:多关键字匹配
     */
	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange("directExchange", true, false);
	}

	//设置队列
	@Bean
	public Queue queue() {
		return new Queue("myQueue", true);
	}

	//绑定  路由key
	@Bean
	public Binding exchangeBinding(@Qualifier("directExchange") DirectExchange directExchange,
								   @Qualifier("queue") Queue queue) {
		return BindingBuilder.bind(queue).to(directExchange).with("route-key");
	}

	//RabbitTemplate 注入connectionFactory
	@Bean
	public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setExchange("directExchange"); //设置交换器,和配置值一致
		rabbitTemplate.setEncoding("UTF-8");
		rabbitTemplate.setRoutingKey("route-key"); //设置路由Key,和配置值一致
		return rabbitTemplate;
	}
}
