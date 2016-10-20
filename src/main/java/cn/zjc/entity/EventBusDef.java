package cn.zjc.entity;

import java.util.Date;

/**
 * @author zjc
 * @version 2016/10/20 23:15
 * @description 消息总线配置
 */
public class EventBusDef {

	private Integer id;
	private String eventBusName;
	private String eventName;
	private String listernerClassName;
	private Integer isAvailable;
	private Integer isAsync;
	private String  memo;
	private Date createTime;

	public EventBusDef() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEventBusName() {
		return eventBusName;
	}

	public void setEventBusName(String eventBusName) {
		this.eventBusName = eventBusName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getListernerClassName() {
		return listernerClassName;
	}

	public void setListernerClassName(String listernerClassName) {
		this.listernerClassName = listernerClassName;
	}

	public Integer getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Integer isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Integer getIsAsync() {
		return isAsync;
	}

	public void setIsAsync(Integer isAsync) {
		this.isAsync = isAsync;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
