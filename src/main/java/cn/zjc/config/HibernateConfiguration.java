package cn.zjc.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;


/**
 * @author zhangjinci
 * @version 2016/10/26 20:28
 * @function
 */
@Configuration
@EnableTransactionManagement
public class HibernateConfiguration {

    @Autowired
    private Environment environment;

    //Hibernate的sessionFactory
    @Bean
    public LocalSessionFactoryBean sessionFactory(DynamicDataSource dynamicDataSource){
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dynamicDataSource);
        sessionFactoryBean.setPackagesToScan("cn.zjc.schedule.entity","cn.zjc.entity"); //实体所在的包,不定参数，可以多传
        sessionFactoryBean.setHibernateProperties(getHibernateProps());
        return sessionFactoryBean;
    }

    //Hibernate事务管理器
    @Bean
    public HibernateTransactionManager transactionManager(DynamicDataSource dynamicDataSource){
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory(dynamicDataSource).getObject());
        return transactionManager;
    }

    private Properties getHibernateProps(){
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql",environment.getProperty("hibernate.show_sql"));
        properties.setProperty("hibernate.hbm2ddl.auto",environment.getProperty("hibernate.hbm2ddl.auto"));
        properties.setProperty("hibernate.dialect",environment.getProperty("hibernate.dialect"));
        return properties;
    }

}
