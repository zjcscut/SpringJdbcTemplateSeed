package cn.zjc.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:22
 * @function 计划任务信息
 */
public class ScheduleJob implements Serializable {

    private static final long serialVersionUID = 4888005949821878223L;

    /**
     * 任务调度的参数key
     */
    public static final String JOB_PARAM_KEY = "taskId"; //可以注入多个参数再从Context获取

    /*主键*/
    private Long id;
    /*任务名*/
    private String jobName;
    /*任务别名*/
    private String aliasNmae;
    /*任务分组名*/
    private String jobGroup;
    /*触发器*/
    private String jobTrigger;
    /*任务状态*/
    private String status;
    /*任务运行Cron表达式*/
    private String cronExpression;
    /*是否异步,1:手动,0:自动*/
    private Integer isManual;
    /*任务描述*/
    private String description;
    /*创建时间*/
    private Date createTime;
    /*修改时间*/
    private Date modifyTime;

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

    public String getAliasNmae() {
        return aliasNmae;
    }

    public void setAliasNmae(String aliasNmae) {
        this.aliasNmae = aliasNmae;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobTrigger() {
        return jobTrigger;
    }

    public void setJobTrigger(String jobTrigger) {
        this.jobTrigger = jobTrigger;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getIsManual() {
        return isManual;
    }

    public void setIsManual(Integer isManual) {
        this.isManual = isManual;
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
}
