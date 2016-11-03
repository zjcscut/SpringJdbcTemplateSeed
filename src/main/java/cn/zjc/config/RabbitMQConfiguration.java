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
        executor.setQueueCapacity(128); //队列长队
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

    @Bean("amqpAdmin")
    public AmqpAdmin amqpAdmin(CachingConnectionFactory connectionFactory) {
        AmqpAdmin amqpAdmin = new RabbitAdmin(connectionFactory);
//		amqpAdmin.declareExchange();  添加Exchange
//		amqpAdmin.declareBinding();   添加Binding
//		amqpAdmin.declareQueue()
        return amqpAdmin;
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

    @Bean(name = "simpleRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(CachingConnectionFactory connectionFactory,
                                                                                     ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(5);
        factory.setMaxConcurrentConsumers(10);
        factory.setTaskExecutor(threadPoolTaskExecutor);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    //设置队列
    @Bean
    public Queue queue() {
        return new Queue("myQueue", true); //第二个参数为是否持久化队列
    }

    //绑定  路由key
    @Bean
    public Binding exchangeBinding(@Qualifier("directExchange") DirectExchange directExchange,
                                   @Qualifier("queue") Queue queue) {
//		return new Binding("queueName", Binding.DestinationType.QUEUE,"exchange","routingKey",new HashMap<String,Object>());
        return BindingBuilder.bind(queue).to(directExchange).with("route-key");

    }

    //RabbitTemplate 注入connectionFactory
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setExchange("directExchange"); //设置交换器,和配置值一致
        rabbitTemplate.setEncoding("UTF-8");
//        rabbitTemplate.setRoutingKey("route-key"); //设置路由Key,和配置值一致

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
