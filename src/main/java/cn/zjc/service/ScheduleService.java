package cn.zjc.service;

import cn.zjc.schedule.dao.ScheduleJobDao;
import cn.zjc.schedule.dao.ScheduleRecordDao;
import cn.zjc.schedule.entity.ScheduleJob;
import cn.zjc.schedule.entity.ScheduleRecord;
import cn.zjc.schedule.support.ScehduleJobSupports;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:51
 * @function 调度Service
 */
@Service
@Transactional
public class ScheduleService {


	@Autowired
	private ScheduleJobDao scheduleJobDao;

	@Autowired
	private ScehduleJobSupports scehduleJobSupports;

	@Autowired
	private ScheduleRecordDao scheduleRecordDao;

	/**
	 * 初始化调度服务
	 */
	public void initScheduleService() {
		List<ScheduleJob> scheduleJobList = scheduleJobDao.queryAutoList();
		if (CollectionUtils.isEmpty(scheduleJobList)) {
			return;
		}
		for (ScheduleJob scheduleJob : scheduleJobList) {
			scehduleJobSupports.createScheduleJob(scheduleJob);
		}
		scehduleJobSupports.start();
	}

	/**
	 * 关闭调度服务
	 */
	public void shutScheduleService() {
		scehduleJobSupports.shutdown(true);
	}

	/**
	 * 根据taskId查询调度任务
	 *
	 * @param id
	 * @return
	 */
	public ScheduleJob queryByTaskId(Long id) {
		return scheduleJobDao.queryById(id);
	}

	/**
	 * 查询全部调度任务
	 *
	 * @return
	 */
	public List<ScheduleJob> queryAll() {
		return scheduleJobDao.queryAll();
	}

	/**
	 * 根据taskId查询调度记录
	 *
	 * @param taskId
	 * @return
	 */
	public ScheduleRecord queryRecordByTaskId(Long taskId) {
		return scheduleRecordDao.queryByTaskId(taskId);
	}


}
