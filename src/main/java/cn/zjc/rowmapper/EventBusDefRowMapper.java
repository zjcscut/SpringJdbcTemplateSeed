package cn.zjc.rowmapper;

import cn.zjc.entity.EventBusDef;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zjc
 * @version 2016/10/20 23:19
 * @description
 */
public class EventBusDefRowMapper implements RowMapper<EventBusDef> {

	@Override
	public EventBusDef mapRow(ResultSet resultSet, int i) throws SQLException {
		EventBusDef def = new EventBusDef();
		def.setId(resultSet.getInt("ID"));
		def.setCreateTime(resultSet.getTimestamp("CREATE_TIME"));
		def.setEventBusName(resultSet.getString("EVENTBUS_NAME"));
		def.setEventName(resultSet.getString("EVENT_NAME"));
		def.setMemo(resultSet.getString("MEMO"));
		def.setIsAsync(resultSet.getInt("IS_ASYNC"));
		def.setIsAvailable(resultSet.getInt("IS_AVAILABLE"));
		def.setListernerClassName(resultSet.getString("LISTERNER_CLASS_NAME"));
		return def;
	}
}
