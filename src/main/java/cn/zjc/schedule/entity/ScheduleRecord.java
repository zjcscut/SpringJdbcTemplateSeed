package cn.zjc.schedule.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zjc
 * @version 2016/10/24 23:36
 * @description
 */
public class ScheduleRecord implements Serializable{

	/*主键*/
	private Long id;

	/*任务id*/
	private Long taskId;
    /*触发器实例id*/
	private String triggerInstId;
    /*开始时间*/
	private Date startTime;
	/*结束时间*/
	private Date endTime;
    /*耗时 ms*/
	private Long cost;

	public ScheduleRecord() {
	}

	public ScheduleRecord(Long taskId, String triggerInstId, Date startTime, Date endTime) {
		this.taskId = taskId;
		this.triggerInstId = triggerInstId;
		this.startTime = startTime;
		this.endTime = endTime;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
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
