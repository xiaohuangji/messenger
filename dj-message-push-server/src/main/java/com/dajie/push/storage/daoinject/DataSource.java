
package com.dajie.push.storage.daoinject;

import com.dajie.framework.config.UpdateableConfig;
import com.dajie.framework.config.impl.DefaultConfigManager;
import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.LoggerFactory;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Created by wills on 3/11/14.
 */

public class DataSource extends BasicDataSource {

    private static final org.slf4j.Logger LOGGER= LoggerFactory.getLogger(DataSource.class);

    private static final String DB_CONFIG_NAME = "db_jobmessage";//数据库名字

    private String dbName;

    public void init()  {
        try{
            if(dbName==null||dbName.equals("")){
                dbName = DB_CONFIG_NAME;
            }
            UpdateableConfig  config = DefaultConfigManager.getInstance().getDBConfigByName(dbName);
            super.setDriverClassName(config.getString("driverClass"));
            super.setUsername(config.getString("username"));
            super.setPassword(config.getString("password"));
            super.setUrl(config.getString("jdbcUrl"));
            super.setMaxIdle(config.getInt("idleMaxAge"));
            super.setMaxActive(config.getInt
                    ("maxPoolSize"));
        }catch(Exception e){
            LOGGER.error("datasource error:",e);
        }
    }


    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }


    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
