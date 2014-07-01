package com.dajie.push.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by wills on 3/20/14.
 */
public class Configuration {

    private static final Logger LOGGER= LoggerFactory.getLogger(Configuration.class);

    private Properties properties = new Properties();

    private static Configuration configuration=new Configuration();

    private Configuration() {
        try {
            properties.load(this.getClass().getResourceAsStream("/mqtt.properties"));
        } catch ( Exception e ) {
            LOGGER.error("Unable to load configuration",e);
        }
    }


    public static final Configuration getInstance () {
        return configuration;
    }

    public  String getString (String propertyName) {
        return properties.getProperty(propertyName);
    }

    public  int getInt (String propertyName) {
        String config= properties.getProperty(propertyName);
        return config==null?0:Integer.valueOf(config);
    }

    public boolean getBoolean(String propertyName){
        String config= properties.getProperty(propertyName);
        if(config==null){
            return false;
        }
        if(config.toLowerCase().equals("true")){
            return true;
        }else{
            return false;
        }

    }

}
