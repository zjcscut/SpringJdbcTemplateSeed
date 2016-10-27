package cn.zjc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import java.util.Map;

/**
 * @author zhangjinci
 * @version 2016/10/26 20:28
 * @function
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryBean",
        transactionManagerRef = "transactionManager",
        basePackages = {"cn.zjc.dao", "cn.zjc.schedule.dao"}  //设置dao的位置
)
public class JpaConfiguration {


    @Bean(name = "entityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder, DynamicDataSource dynamicDataSource) {
        return entityManagerFactoryBean(builder, dynamicDataSource).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder, DynamicDataSource dynamicDataSource) {
        return builder
                .dataSource(dynamicDataSource)
                .properties(getJpaProperties(dynamicDataSource))
                .packages("cn.zjc.entity", "cn.zjc.schedule.entity")  //实体类所在的包全名
                .persistenceUnit("persistenceUnit")
                .build();
    }

    @Autowired
    private JpaProperties jpaProperties;

    private Map<String, String> getJpaProperties(DynamicDataSource dynamicDataSource) {
        return jpaProperties.getHibernateProperties(dynamicDataSource);
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder, DynamicDataSource dynamicDataSource) {
        return new JpaTransactionManager(entityManagerFactoryBean(builder, dynamicDataSource).getObject());
    }

}
