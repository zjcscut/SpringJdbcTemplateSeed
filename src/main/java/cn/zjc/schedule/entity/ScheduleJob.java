package cn.zjc.schedule.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:22
 * @function 计划任务信息
 */
@Entity
@Table(name = "TB_AT_SCHEDULE_JOB")
public class ScheduleJob implements Serializable {

    private static final long serialVersionUID = 4888005949821878223L;

    /**
     * 任务调度的参数key
     */
    public static final String JOB_PARAM_KEY = "taskId"; //可以注入多个参数再从Context获取

    /*主键*/
    @Id
    @GeneratedValue
    private Long id;
    /*任务名*/
    @Column(name = "JOBNAME")
    private String jobName;
    /*任务分组名*/
    @Column(name = "JOBGROUP")
    private String jobGroup;
    /*任务运行Cron表达式*/
    @Column(name = "CRON_EXPRESSION")
    private String cronExpression;
    /*启动方式,1:手动,0:自动*/
    @Column(name = "RUN_TYPE")
    private Integer runType;
    /*启动状态,1:启动,0:停止*/
    @Column(name = "RUN_STATUS")
    private Integer runStatus;
    /*任务描述*/
    @Column(name = "DESCRTPTION")
    private String description;
    /*执行时间*/
    @Column(name = "EXECUTE_TIME")
    private Date executeTime;
    /*创建时间*/
    @Column(name = "CREATE_TIME")
    private Date createTime;
    /*修改时间*/
    @Column(name = "MODIFY_TIME")
    private Date modifyTime;
    /*执行的任务类*/
    @Column(name = "TARGET_CLASS_NAME")
    private String targetClassNmae;
    /*是否有效*/
    @Column(name = "IS_ENABLED")
    private Integer isEnabled;

    public ScheduleJob() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }


    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getRunType() {
        return runType;
    }

    public void setRunType(Integer runType) {
        this.runType = runType;
    }

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public String getTargetClassNmae() {
        return targetClassNmae;
    }

    public void setTargetClassNmae(String targetClassNmae) {
        this.targetClassNmae = targetClassNmae;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }
}
