package com.dajie.message.service.worker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.dajie.common.queue.QueuePutter;
import com.dajie.message.service.mq.GoudaVerifyEvent;

@Component("goudaVerifyMQPutter")
public class GoudaVerifyPutter {

	@Autowired
	@Qualifier("goudaVerifyPutter")
	private QueuePutter<GoudaVerifyEvent> verifyPutter;

    private static final Logger logger = LoggerFactory.getLogger(GoudaVerifyPutter.class);
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(3);
	
	public void addVerifyEvent(final GoudaVerifyEvent verifyEvent){
        EXECUTOR_SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != verifyEvent && verifyEvent.validate()) {
                    	verifyPutter.put(verifyEvent);
                        logger.info("Add job verify bean: {} to job verify queue", verifyEvent.toString());
                    } else {
                        logger.warn("Ignore invalid jobVerifyBean: {}",
                                null == verifyEvent ? null : verifyEvent.toString());
                    }
                } catch (Exception e) {
                    logger.error("JOB CAUTION: Fail to execute addJobAuditMQ with jobVerifyBean: {}",
                            null == verifyEvent ? null : verifyEvent.toString(), e);
                }
            }
        });
	}
	
}
