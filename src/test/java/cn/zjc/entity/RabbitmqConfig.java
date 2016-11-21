package cn.zjc.entity;

/**
 * @author zjc
 * @version 2016/11/22 0:05
 * @description
 */
public class RabbitmqConfig {

	private String queueName;
	private String exchangeName;
	private String exchangeType;
	private String routingKey;

	public RabbitmqConfig() {
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}
}
