package cn.zjc.schedule.dao;

import cn.zjc.dao.AbstractCRUDDAO;
import cn.zjc.schedule.entity.ScheduleJob;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangjinci
 * @version 2016/10/24 11:55
 * @function
 */
@Repository
public class ScheduleJobDao extends AbstractCRUDDAO<ScheduleJob> {


	@Override
	protected Class<ScheduleJob> getEntityClass() {
		return ScheduleJob.class;
	}


	public List<ScheduleJob> queryAutoList() {
		return getCurrentSession().createQuery("FROM ScheduleJob s  where s.runType = 0 AND s.isEnabled = 1")
				.list();
	}

	public ScheduleJob queryById(Long id) {
		return fetch(id);
	}


	public List<ScheduleJob> queryAll() {
		return findAll();
	}

	public ScheduleJob nameQuery(String jobName) {
		return (ScheduleJob) getCurrentSession().getNamedQuery("selectByName")
				.setString("jobName", jobName)
				.uniqueResult();
	}


}
