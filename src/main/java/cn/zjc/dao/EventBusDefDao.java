package cn.zjc.dao;

import cn.zjc.entity.EventBusDef;
import cn.zjc.eventbus.constant.EventConst;
import cn.zjc.rowmapper.EventBusDefRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zjc
 * @version 2016/10/20 23:24
 * @description
 */
@Repository
public class EventBusDefDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<EventBusDef> queryAll() {
        return jdbcTemplate.
                query("SELECT * FROM TB_AT_EVENTBUS_DEF WHERE IS_AVAILABLE = ?", new Object[]{EventConst.IS_AVAILABLE}, new EventBusDefRowMapper());
    }

    public EventBusDef querySingleByName(String eventBusName) {
        return jdbcTemplate.
                queryForObject("SELECT * FROM TB_AT_EVENTBUS_DEF WHERE IS_AVAILABLE = ? AND EVENTBUS_NAME = ?", new Object[]{EventConst.IS_AVAILABLE, eventBusName}, new EventBusDefRowMapper());
    }

    public void delete(EventBusDef def) {
        jdbcTemplate.update("DELETE FROM TB_AT_EVENTBUS_DEF WHERE ID = ?", def.getId());
    }

    public void save(EventBusDef def) {
        jdbcTemplate.update("INSERT INTO TB_AT_EVENTBUS_DEF(EVENTBUS_NAME,EVENT_NAME,LISTERNER_CLASS_NAME,MEMO,IS_AVAILABLE,IS_ASYNC,CREATE_TIME) VALUES (?,?,?,?,?,?,?)",
                def.getEventBusName(), def.getEventName(), def.getListernerClassName(), def.getMemo(), def.getIsAvailable(), def.getIsAsync(), def.getCreateTime());
    }
}
