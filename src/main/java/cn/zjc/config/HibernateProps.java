package cn.zjc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zjc
 * @version 2016/11/7 1:12
 * @description
 */
@Component
@ConfigurationProperties(prefix = "hibernate")
public class HibernateProps {

	private String show_sql = "true";
	private String format_sql = "false";
	private String hbm2ddlAuto = "update";
	private String dialect = "org.hibernate.dialect.MySQL5Dialect";
	private String use_second_level_cache = "false";

	public String getShow_sql() {
		return show_sql;
	}

	public void setShow_sql(String show_sql) {
		this.show_sql = show_sql;
	}

	public String getFormat_sql() {
		return format_sql;
	}

	public void setFormat_sql(String format_sql) {
		this.format_sql = format_sql;
	}

	public String getHbm2ddlAuto() {
		return hbm2ddlAuto;
	}

	public void setHbm2ddlAuto(String hbm2ddlAuto) {
		this.hbm2ddlAuto = hbm2ddlAuto;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getUse_second_level_cache() {
		return use_second_level_cache;
	}

	public void setUse_second_level_cache(String use_second_level_cache) {
		this.use_second_level_cache = use_second_level_cache;
	}

	@Override
	public String toString() {
		return "HibernateProps{" +
				"show_sql='" + show_sql + '\'' +
				", format_sql='" + format_sql + '\'' +
				", hbm2ddlAuto='" + hbm2ddlAuto + '\'' +
				", dialect='" + dialect + '\'' +
				", use_second_level_cache='" + use_second_level_cache + '\'' +
				'}';
	}
}
