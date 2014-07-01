package com.dajie.push.storage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by wills on 3/17/14.
 */
@Deprecated
public class DBStorageManagerFactory {

    private static DBStorageManager dbStorageManager;

    private DBStorageManagerFactory() {
    }

    public static DBStorageManager getInstance(){
        if(dbStorageManager==null){
            ApplicationContext context= new ClassPathXmlApplicationContext("applicationContext.xml");
            dbStorageManager=(DBStorageManager)context.getBean("dbStorageManager");
        }
        return dbStorageManager;
    }
}
