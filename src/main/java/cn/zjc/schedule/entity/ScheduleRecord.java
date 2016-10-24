package cn.zjc.schedule.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zjc
 * @version 2016/10/24 23:36
 * @description
 */
public class ScheduleRecord implements Serializable{

	private Integer id;

	private Integer taskId;

	private String triggerInstId;

	private Date startTime;

	private Date endTime;

	private Long cost;

	public ScheduleRecord() {
	}

	public ScheduleRecord(Integer taskId, String triggerInstId, Date startTime, Date endTime) {
		this.taskId = taskId;
		this.triggerInstId = triggerInstId;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getTriggerInstId() {
		return triggerInstId;
	}

	public void setTriggerInstId(String triggerInstId) {
		this.triggerInstId = triggerInstId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getCost() {
		return cost;
	}

	public void setCost(Long cost) {
		this.cost = cost;
	}
}
