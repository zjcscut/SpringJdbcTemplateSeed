package cn.zjc.config;

import cn.zjc.common.concurrency.MonitingThreaPoolTaskExecutor;
import cn.zjc.jedis.JedisStringService;
import cn.zjc.mq.converter.FastJsonMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
	private static final String X_EXCEPTION_MESSAGE = "x-exception-message";
	private static final String X_ORIGINAL_EXCHANGE = "x-original-exchange";
	private static final String X_ORIGINAL_ROUTING_KEY = "x-original-routingKey";
	public static final String DEFAULT_CHARSET = "UTF-8";

	@Autowired
	private Environment environment;

	@Autowired
	private JedisStringService jedisStringService;

	//自定义带监控的线程池
	@Bean(name = "monitingThreaPoolTaskExecutor")
	public MonitingThreaPoolTaskExecutor monitingThreaPoolTaskExecutor() {
		MonitingThreaPoolTaskExecutor executor = new MonitingThreaPoolTaskExecutor();
		executor.setCorePoolSize(15);  //核心池数量,这个过小会阻塞消费者队列,最好比MaxConcurrentConsumers大
		executor.setMaxPoolSize(256); //最大池数量
		executor.setQueueCapacity(128); //队列长度
		executor.setKeepAliveSeconds(60); //当队列满并且最大池数量也满,多余的线程等待的最大时间
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); //拒绝策略
		executor.setAllowCoreThreadTimeOut(true);
		return executor;
	}

	//配置Spring线程池,用于消费者消费消息
	@Bean(name = "threadPoolTaskExecutor")
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(15);  //核心池数量,这个过小会阻塞消费者队列,最好比MaxConcurrentConsumers大
		executor.setMaxPoolSize(256); //最大池数量
		executor.setQueueCapacity(128); //队列长度
		executor.setKeepAliveSeconds(60); //当队列满并且最大池数量也满,多余的线程等待的最大时间
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); //拒绝策略
		return executor;
	}

	//配置Spring线程池,用于生产者发送消息
	@Bean(name = "threadPoolTaskExecutorEx")
	public ThreadPoolTaskExecutor threadPoolTaskExecutorEx() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);  //核心池数量
		executor.setMaxPoolSize(10); //最大池数量
		executor.setQueueCapacity(20); //队列长度
		executor.setKeepAliveSeconds(60); //当队列满并且最大池数量也满,多余的线程等待的最大时间
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); //拒绝策略
		return executor;
	}

	//带缓存的连接工厂
	@Bean(name = "connectionFactory")
	@ConditionalOnBean(name = "threadPoolTaskExecutorEx")
	public CachingConnectionFactory connectionFactory(ThreadPoolTaskExecutor threadPoolTaskExecutorEx) {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setUsername(environment.getProperty("spring.custom.rabbitmq.username"));
		connectionFactory.setPassword(environment.getProperty("spring.custom.rabbitmq.password"));
		connectionFactory.setHost(environment.getProperty("spring.custom.rabbitmq.host"));
		connectionFactory.setPort(Integer.valueOf(environment.getProperty("spring.custom.rabbitmq.port")));
		connectionFactory.setVirtualHost(environment.getProperty("spring.custom.rabbitmq.virtual_port"));
		connectionFactory.setPublisherConfirms(CONFIRMS_MANUAL);  //必须手工设置确认==>发布确认
		connectionFactory.setPublisherReturns(true);  //允许错误路由的发布消息返回
		connectionFactory.setExecutor(threadPoolTaskExecutorEx);
		return connectionFactory;
	}

	//AmqpAdmin:职能类似进行Rabbit的Admin操作,可以添加交换器、队列和绑定等等
	@Bean("amqpAdmin")
	public AmqpAdmin amqpAdmin(CachingConnectionFactory connectionFactory) {
		AmqpAdmin amqpAdmin = new RabbitAdmin(connectionFactory);
		Queue x_queue = new Queue(X_EXCEPTION_MESSAGE);
		amqpAdmin.declareQueue(x_queue);
		DirectExchange x_exchange = new DirectExchange(X_ORIGINAL_EXCHANGE);
		amqpAdmin.declareExchange(x_exchange);
		amqpAdmin.declareBinding(BindingBuilder.bind(x_queue).to(x_exchange).with(X_ORIGINAL_ROUTING_KEY));

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

	/**
	 * 提供客户端操作
	 *
	 * @return
	 */
	@Bean(name = "amqpManagementOperations")
	public AmqpManagementOperations amqpManagementOperations() {
		String uri = environment.getProperty("spring.custom.rabbitmq.host") + ":" +
				Integer.valueOf(environment.getProperty("spring.custom.rabbitmq.port")) + "/api/";
		return new RabbitManagementTemplate(
				uri, environment.getProperty("spring.custom.rabbitmq.username"),
				environment.getProperty("spring.custom.rabbitmq.password")
		);
	}


	/**
	 * 这个类是在1.4.2版本中引入的，并可基于MessageProperties的contentType属性允许委派给一个特定的MessageConverter.
	 * 默认情况下，如果没有contentType属性或值没有匹配配置转换器时，它会委派给SimpleMessageConverter.
	 * see {@link cn.zjc.mq.converter.FastJsonMessageConverter}
	 *
	 * @return converter
	 */
	@Bean(name = "contentTypeDelegatingMessageConverter")
	public ContentTypeDelegatingMessageConverter contentTypeDelegatingMessageConverter() {
		ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter();
		converter.addDelegate(MediaType.APPLICATION_JSON_VALUE, new FastJsonMessageConverter()); //添加处理application/json消息的转换器
		converter.addDelegate(MediaType.TEXT_PLAIN_VALUE, new Jackson2JsonMessageConverter());
		return converter;
	}


	@Bean(name = "simpleRabbitListenerContainerFactory")
	@ConditionalOnBean(name = {"threadPoolTaskExecutor", "contentTypeDelegatingMessageConverter"})
	public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(CachingConnectionFactory connectionFactory,
																					 ThreadPoolTaskExecutor threadPoolTaskExecutor,
																					 ContentTypeDelegatingMessageConverter contentTypeDelegatingMessageConverter) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConcurrentConsumers(5);  //异步消费者数量
		factory.setMaxConcurrentConsumers(10); //最大异步消费者数量
		factory.setTaskExecutor(threadPoolTaskExecutor);
		factory.setMessageConverter(contentTypeDelegatingMessageConverter); //设置字符串转换器
		factory.setConnectionFactory(connectionFactory);
		factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置手动确认消费成功
		return factory;
	}


	//RabbitTemplate 注入connectionFactory
	//注入消息类型转换器
	@Bean
	@ConditionalOnBean(name = {"contentTypeDelegatingMessageConverter"})
	public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory,
										 ContentTypeDelegatingMessageConverter contentTypeDelegatingMessageConverter) {
		final RabbitTemplate errorTemplate = new RabbitTemplate(connectionFactory);
		errorTemplate.setEncoding(DEFAULT_CHARSET);
		errorTemplate.setMessageConverter(contentTypeDelegatingMessageConverter);

		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setEncoding(DEFAULT_CHARSET);
		rabbitTemplate.setMessageConverter(contentTypeDelegatingMessageConverter); //设置字符串转换器

		//对于返回消息，模板的mandatory属性必须被设定为true
		//同样要求CachingConnectionFactory的publisherReturns属性被设定为true
		rabbitTemplate.setMandatory(true);

		//发布消息确认,这个其实是broker的ack
		//实际上消息丢失了(路由错误)，却仍旧可以收到来自服务器的Ack。这也是实际使用中容易犯的错误
		//也就是broker的comfirm机制是无法确认消息正确路由,返回的Ack只是确认消息发送到了broker
		rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				if (ack) {
					log.debug("Broker comfirm succeed,CorrelationData id==>" + correlationData.getId());
					jedisStringService.del(correlationData.getId());
				} else {
					log.debug("Broker comfirm failed,CorrelationData id==>" + correlationData.getId() + ";cause:" + cause);
				}
			}
		});
		//发布消息返回?什么时候会出现这种情况？
		//对于返回消息，模板的mandatory属性必须被设定为true
		//确认消息是否到达broker服务器，也就是只确认是否正确到达exchange中即可，只要正确的到达exchange中，broker即可确认该消息返回给客户端ack。
		//换句话说,只要有ReturnCallback说明消息没有发送到broker服务器,或者说没有正确到达exchange,就是错误路由
		//错误路由的话一般不应该出现,属于比较严重的逻辑错误,采用重新推送到x-exception-message队列(候选方案)
		//可以选择把消息入库或者放进去Nosql
		rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
			@Override
			public void returnedMessage(Message message, int replyCode, String replyText,
										String exchange, String routingKey) {
				log.error("route failed and begin to republish,replyCode:" + replyCode + ",replyText:" + replyText);
				RepublishMessageRecoverer recoverer = new RepublishMessageRecoverer(errorTemplate, X_ORIGINAL_EXCHANGE, X_ORIGINAL_ROUTING_KEY);
				Throwable cause = new Exception("route_fail_and_republish");
				recoverer.recover(message, cause);
			}
		});
		RetryTemplate retryTemplate = new RetryTemplate();
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy(); //指数退避算法，为了避免多个重试同时进行
		backOffPolicy.setInitialInterval(100);
		backOffPolicy.setMultiplier(2);
		backOffPolicy.setMaxInterval(30000);
		retryTemplate.setBackOffPolicy(backOffPolicy);
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();  //重试代理,重试5次
		retryPolicy.setMaxAttempts(5);   //最大重试次数
		retryTemplate.setRetryPolicy(retryPolicy);
		rabbitTemplate.setRetryTemplate(retryTemplate);  //设置重试模板
		return rabbitTemplate;
	}

}
