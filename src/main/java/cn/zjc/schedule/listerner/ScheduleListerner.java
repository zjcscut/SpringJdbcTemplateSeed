package cn.zjc.schedule.listerner;

import cn.zjc.schedule.entity.ScheduleJob;
import cn.zjc.exception.ScheduleException;
import cn.zjc.schedule.dao.ScheduleRecordDao;
import cn.zjc.schedule.entity.ScheduleRecord;
import cn.zjc.utils.Assert;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zjc
 * @version 2016/10/24 23:33
 * @description 调度监听器
 */
@Component
public class ScheduleListerner implements JobListener {

	private static final Logger log = LoggerFactory.getLogger(ScheduleListerner.class);

	@Autowired
	private ScheduleRecordDao scheduleRecordDao;

	@Override
	public String getName() {
		return "ScheduleJobListerner";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
		Long taskId = (Long) jobExecutionContext.getMergedJobDataMap().get(ScheduleJob.JOB_PARAM_KEY);
		Assert.notNull(taskId, "taskId must not be null,id:%d", taskId);
		ScheduleRecord scheduleRecord = new ScheduleRecord(
				taskId, jobExecutionContext.getFireInstanceId(), new Date(), null);
		scheduleRecordDao.save(scheduleRecord);
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
		log.debug("---------------------------------jobExecutionVetoed");
	}

	@Override
	public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
		Integer taskId = (Integer) jobExecutionContext.getMergedJobDataMap().get(ScheduleJob.JOB_PARAM_KEY);
		Assert.notNull(taskId, "taskId must not be null,id:%d", taskId);
		try {
			ScheduleRecord record = scheduleRecordDao.queryByTaskId(taskId);
			if (record == null) {
				throw new ScheduleException(String.format("定时任务监听器异常：保存执行完成记录时错误，找不到任务开始记录;triggerInstId:%s", jobExecutionContext.getFireInstanceId()));
			}
			record.setEndTime(new Date());
			record.setCost(record.getEndTime().getTime() - record.getStartTime().getTime());
			scheduleRecordDao.update(record);
		} catch (Exception ex) {
			log.error("定时任务监听器异常,异常信息为:{}", e.getMessage());
		}

	}
}
