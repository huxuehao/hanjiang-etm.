package com.hanjiang.etm.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 数据源配置类
 *
 * @author huxuehao
 **/
@Configuration
@EnableTransactionManagement
public class DruidDBConfig {
    /* adi数据库连接信息 */
    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    /* 连接池连接信息 */
    @Value("${spring.datasource.initial-size}")
    private int initialSize;
    @Value("${spring.datasource.min-idle}")
    private int minIdle;
    @Value("${spring.datasource.max-active}")
    private int maxActive;
    @Value("${spring.datasource.max-wait}")
    private int maxWait;
    @Value("${spring.datasource.time-between-eviction-runs-millis}")
    private long timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.min-evictable-idle-time-millis}")
    private long minEvictableIdleTimeMillis;
    @Value("${spring.datasource.max-evictable-idle-time-millis}")
    private long maxEvictableIdleTimeMillis;
    @Value("${spring.datasource.validation-query}")
    private String validationQuery;
    @Value("${spring.datasource.test-while-idle}")
    private boolean testWhileIdle;
    @Value("${spring.datasource.test-on-borrow}")
    private boolean testOnBorrow;
    @Value("${spring.datasource.test-on-return}")
    private boolean testOnReturn;
    @Value("${spring.datasource.filters}")
    private String filters;

    @Bean
    @Primary /* 在同样的DataSource中，首先使用被标注的DataSource */
    @Qualifier("mainDataSource")
    public DataSource dataSource() throws SQLException {
        DruidDataSource datasource = new DruidDataSource();
        /* 基础连接信息 */
        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        /* 连接池连接信息 */
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        /* 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。 */
        datasource.setPoolPreparedStatements(true);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(20);
        /* 对于耗时长的查询sql，会受限于ReadTimeout的控制，单位毫秒 */
        datasource.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=15000");
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        /* 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，
           执行validationQuery检测连接是否有效。
        */
        datasource.setTestOnReturn(testOnReturn);
        /* 用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、
           testWhileIdle都不会起作用。
        */
        datasource.setValidationQuery(validationQuery);
        /* 属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有：监控统计用的filter:stat日志用的filter:log4j防御sql注入的filter:wall */
        datasource.setFilters(filters);
        /* 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 */
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        /* 配置一个连接在池中最小生存的时间，单位是毫秒 */
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);
        /* 打开druid.keepAlive之后，当连接池空闲时，池中的minIdle数量以内的连接，空闲时间超过minEvictableIdleTimeMillis，
           则会执行keepAlive操作，即执行druid.validationQuery指定的查询SQL，一般为select * from dual，
           只要minEvictableIdleTimeMillis设置的小于防火墙切断连接时间，就可以保证当连接空闲时自动做保活检测，不会被防火墙切断
        */
        datasource.setKeepAlive(true);
        /* 是否移除泄露的连接/超过时间限制是否回收。 */
        datasource.setRemoveAbandoned(true);
        /* 泄露连接的定义时间(要超过最大事务的处理时间)；单位为秒。这里配置为1小时 */
        datasource.setRemoveAbandonedTimeout(3600);
        /* 移除泄露连接发生是是否记录日志 */
        datasource.setLogAbandoned(true);
        return datasource;
    }
}
