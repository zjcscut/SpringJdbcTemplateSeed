package cn.zjc.schedule.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zjc
 * @version 2016/10/24 23:36
 * @description
 */
public class ScheduleRecord implements Serializable{

	/*主键*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/*任务id*/
	@Column(name = "TASK_ID")
	private Long taskId;
    /*触发器实例id*/
    @Column(name = "TRIGGER_INST_ID")
	private String triggerInstId;
    /*开始时间*/
    @Column(name = "START_TIME")
	private Date startTime;
	/*结束时间*/
	@Column(name = "END_TIME")
	private Date endTime;
    /*耗时 ms*/
    @Column(name = "COST")
	private Long cost;

	public ScheduleRecord() {
	}

	public ScheduleRecord(Long taskId, String triggerInstId, Date startTime, Date endTime,Long cost) {
		this.taskId = taskId;
		this.triggerInstId = triggerInstId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.cost = cost;
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
