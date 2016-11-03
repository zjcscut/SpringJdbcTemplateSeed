package cn.zjc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @author zjc
 * @version 2016/10/24 0:38
 * @description
 */
@Configuration
public class RabbitMQConfiguration {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQConfiguration.class);

	private static final boolean CONFIRMS_AUTO = false;
	private static final boolean CONFIRMS_MANUAL = true;

	@Autowired
	private Environment environment;

	//配置Spring线程池
	@Bean(name = "threadPoolTaskExecutor")
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);  //核心池数量
		executor.setMaxPoolSize(256); //最大池数量
		executor.setQueueCapacity(128); //队列长度
		executor.setKeepAliveSeconds(60); //当队列满并且最大池数量也满,多余的线程等待的最大时间
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); //拒绝策略
		return executor;
	}

	@Bean
	@ConditionalOnBean(name = "threadPoolTaskExecutor")
	public CachingConnectionFactory connectionFactory(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setUsername(environment.getProperty("spring.custom.rabbitmq.username"));
		connectionFactory.setPassword(environment.getProperty("spring.custom.rabbitmq.password"));
		connectionFactory.setHost(environment.getProperty("spring.custom.rabbitmq.host"));
		connectionFactory.setPort(Integer.valueOf(environment.getProperty("spring.custom.rabbitmq.port")));
		connectionFactory.setVirtualHost(environment.getProperty("spring.custom.rabbitmq.virtual_port"));
		connectionFactory.setPublisherConfirms(CONFIRMS_MANUAL);  //必须手工设置确认,发布确认
		connectionFactory.setExecutor(threadPoolTaskExecutor);
		return connectionFactory;
	}

	//AmqpAdmin:职能类似进行Rabbit的Admin操作,可以添加交换器、队列和绑定等等
	@Bean("amqpAdmin")
	public AmqpAdmin amqpAdmin(CachingConnectionFactory connectionFactory) {
		AmqpAdmin amqpAdmin = new RabbitAdmin(connectionFactory);
		//设置交换机
	/* 针对消费者配置
	   FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
	   HeadersExchange ：通过添加Headers属性key-value匹配
	   DirectExchange:按照routingkey分发到指定队列
	   TopicExchange:多关键字匹配
     */
		AbstractExchange directExchange = new DirectExchange("directExchange", true, false);
		amqpAdmin.declareExchange(directExchange);  //添加Exchange
		Queue queue = new Queue("myQueue", true);
		amqpAdmin.declareQueue(queue);      //添加队列
		Binding binding = BindingBuilder.bind(queue).to(directExchange).with("route-key").noargs();
		amqpAdmin.declareBinding(binding);   //添加Binding
		return amqpAdmin;
	}


	@Bean(name = "simpleRabbitListenerContainerFactory")
	public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(CachingConnectionFactory connectionFactory,
																					 ThreadPoolTaskExecutor threadPoolTaskExecutor) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConcurrentConsumers(5);  //异步消费者数量
		factory.setMaxConcurrentConsumers(10); //最大异步消费者数量
		factory.setTaskExecutor(threadPoolTaskExecutor);
		factory.setMessageConverter(new Jackson2JsonMessageConverter()); //设置字符串转换器
		factory.setConnectionFactory(connectionFactory);
		factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置手动确认消费成功
		return factory;
	}

	//	//设置队列
//	@Bean
//	public Queue queue() {
//		return new Queue("myQueue", true); //第二个参数为是否持久化队列
//	}
//
//	//绑定  路由key
//	@Bean
//	public Binding exchangeBinding(@Qualifier("directExchange") DirectExchange directExchange,
//								   @Qualifier("queue") Queue queue) {
////		return new Binding("queueName", Binding.DestinationType.QUEUE,"exchange","routingKey",new HashMap<String,Object>());
//		return BindingBuilder.bind(queue).to(directExchange).with("route-key");
//	}

	//RabbitTemplate 注入connectionFactory
	@Bean
	public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setEncoding("UTF-8");
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter()); //设置字符串转换器

		//对于返回消息，模板的mandatory属性必须被设定为true
		//同样要求CachingConnectionFactory的publisherReturns属性被设定为true
		rabbitTemplate.setMandatory(true);

		//发布消息确认
		rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				if (ack) {   //消息发布成功,为要发布的消息添加缓存,成功则删除缓存
					log.debug("消息发布成功,CorrelationData id==>" + correlationData.getId());
				} else {   //消息发布失败,通过缓存获取消息并进行失败处理
					log.debug("消息发布失败,CorrelationData id==>" + correlationData.getId());
				}
			}
		});
		RetryTemplate retryTemplate = new RetryTemplate();
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy(); //指数退避算法，为了避免多个重试同时进行
		backOffPolicy.setInitialInterval(500);
		backOffPolicy.setMultiplier(10);
		backOffPolicy.setMaxInterval(10000);
		retryTemplate.setBackOffPolicy(backOffPolicy);
		rabbitTemplate.setRetryTemplate(retryTemplate);  //设置重试模板
		return rabbitTemplate;
	}

}
