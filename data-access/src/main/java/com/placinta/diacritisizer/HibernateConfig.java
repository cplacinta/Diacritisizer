package com.placinta.diacritisizer;

import java.util.Properties;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:database.properties")
public class HibernateConfig {

  private static final String DATABASE_DRIVER_PROPERTY_NAME = "db.driver";
  private static final String DATABASE_URL_PROPERTY_NAME = "db.url";
  private static final String DATABASE_USERNAME_PROPERTY_NAME = "db.username";
  private static final String DATABASE_PASSWORD_PROPERTY_NAME = "PROJECT_DB_PASSWORD";

  private static final String HIBERNATE_DIALECT_PROPERTY_NAME = "hibernate.dialect";
  private static final String HIBERNATE_SHOW_SQL_PROPERTY_NAME = "hibernate.show_sql";
  private static final String ENTITY_MANAGER_PACKAGES_TO_SCAN_PROPERTY_NAME = "entitymanager.packages.to.scan";

  @Resource
  private Environment env;

  @Bean
  public HibernateTransactionManager getTransactionManager() {
    HibernateTransactionManager transactionManager = new HibernateTransactionManager();
    transactionManager.setSessionFactory(getSessionFactory().getObject());
    return transactionManager;
  }

  @Bean
  public LocalSessionFactoryBean getSessionFactory() {
    LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
    sessionFactoryBean.setDataSource(getDataSource());
    sessionFactoryBean.setPackagesToScan(env.getRequiredProperty(ENTITY_MANAGER_PACKAGES_TO_SCAN_PROPERTY_NAME));
    sessionFactoryBean.setHibernateProperties(getHibernateProperties());
    return sessionFactoryBean;
  }

  @Bean
  public DataSource getDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();

    dataSource.setDriverClassName(env.getRequiredProperty(DATABASE_DRIVER_PROPERTY_NAME));
    dataSource.setUrl(env.getRequiredProperty(DATABASE_URL_PROPERTY_NAME));
    dataSource.setUsername(env.getRequiredProperty(DATABASE_USERNAME_PROPERTY_NAME));
    dataSource.setPassword(System.getenv(DATABASE_PASSWORD_PROPERTY_NAME));

    return dataSource;
  }

  private Properties getHibernateProperties() {
    Properties properties = new Properties();
    properties.put(HIBERNATE_DIALECT_PROPERTY_NAME, env.getRequiredProperty(HIBERNATE_DIALECT_PROPERTY_NAME));
    properties.put(HIBERNATE_SHOW_SQL_PROPERTY_NAME, env.getRequiredProperty(HIBERNATE_SHOW_SQL_PROPERTY_NAME));
    return properties;
  }

}
