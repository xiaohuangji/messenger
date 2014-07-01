package com.dajie.message.util.database;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.dajie.framework.config.UpdateableConfig;
import com.dajie.framework.config.impl.DefaultConfigManager;
import com.dajie.message.util.log.LoggerInformation;

public class DataSource  extends BasicDataSource implements InitializingBean {

	private static final Logger logger = Logger.getLogger(DataSource.class);
	
	private static final String DB_CONFIG_NAME = "db_jobmessage";//数据库名字

	private String dbName;

	@Override
	public void afterPropertiesSet() throws Exception {
		if(StringUtils.isEmpty(dbName)){
			LoggerInformation.LoggerErr(logger, "datasource db name should not be null or empty,please check out configration", new Exception());
			dbName = DB_CONFIG_NAME;
		}
		
		UpdateableConfig  config = DefaultConfigManager.getInstance().getDBConfigByName(dbName);
		//System.out.println(config.getString(config.getString("driverClass")));
		super.setDriverClassName(config.getString("driverClass"));
		super.setUsername(config.getString("username"));
		super.setPassword(config.getString("password"));
		super.setUrl(config.getString("jdbcUrl"));
		super.setMaxIdle(config.getInt("idleMaxAge"));
		super.setMaxActive(config.getInt("maxPoolSize"));
		LoggerInformation.LoggerInfo(logger, "database datasource init ",
				"driverClass",getDriverClassName(),
				"userName",getUsername(),
				"password",getPassword(),
				"jdbcUrl",getUrl(),
				"idleMaxAge",getMaxIdle(),
				"maxPoolSize",getMaxActive());		
	}
	

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

}
