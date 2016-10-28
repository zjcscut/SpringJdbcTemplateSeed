package cn.zjc.schedule.dao;


import cn.zjc.dao.AbstractCRUDDAO;
import cn.zjc.schedule.entity.ScheduleRecord;
import org.springframework.stereotype.Repository;


/**
 * @author zjc
 * @version 2016/10/24 23:53
 * @description
 */
@Repository
public class ScheduleRecordDao extends AbstractCRUDDAO<ScheduleRecord> {

    @Override
    protected Class<ScheduleRecord> getEntityClass() {
        return ScheduleRecord.class;
    }

    public ScheduleRecord queryByTaskId(Long taskId) {
        return (ScheduleRecord) getCurrentSession().createSQLQuery("SELECT * FROM TB_AT_SCHEDULE_RECORD WHERE TASK_ID = :taskId")
                .setLong("taskId", taskId)
                .uniqueResult();
    }
}
