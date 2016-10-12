package cn.zjc.config;

/**
 * @author zhangjinci
 * @version 2016/10/11 21:19
 * @function 数据源上下文容器
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<>();

    public static void setDataSourceType(DataSourceType dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    public static DataSourceType getDataSourceType() {
        return contextHolder.get();
    }
}
