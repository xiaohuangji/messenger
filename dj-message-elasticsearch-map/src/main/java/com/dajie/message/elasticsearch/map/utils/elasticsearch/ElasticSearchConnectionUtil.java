package com.dajie.message.elasticsearch.map.utils.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;

import com.dajie.framework.config.EnvironmentEnum;
import com.dajie.framework.config.impl.DefaultConfigManager;
import com.dajie.message.elasticsearch.map.utils.LoggerManager;

public class ElasticSearchConnectionUtil{
	
	private static Logger logger = LoggerManager.getLogger(ElasticSearchConnectionUtil.class);
	
	private static Client instance = null;
	
	private static byte[] lock = new byte[1];
	
	public static Client getInstance()
	{
			if (null == instance) {
			synchronized (lock) {
				if (null == instance) {
					try{
						if(DefaultConfigManager.getInstance().getEnvironmentEnum() == EnvironmentEnum.DEV)
						{
							instance = new TransportClient().addTransportAddress(new InetSocketTransportAddress("10.10.65.95", 9300))
									.addTransportAddress(new InetSocketTransportAddress("10.10.65.95", 9301));
//							instance = new TransportClient().addTransportAddress(new InetSocketTransportAddress("192.168.27.56", 9300));
						}
						else if(DefaultConfigManager.getInstance().getEnvironmentEnum() == EnvironmentEnum.TEST)
						{
							instance = new TransportClient().addTransportAddress(new InetSocketTransportAddress("172.20.10.3", 9300))
									.addTransportAddress(new InetSocketTransportAddress("172.20.10.3", 9301));
						}else if(DefaultConfigManager.getInstance().getEnvironmentEnum() == EnvironmentEnum.PRE_RELEASE)
						{
							Settings settings = ImmutableSettings.settingsBuilder()
							        .put("cluster.name", "mobileapp_elasticsearch").build();
							
							instance = new TransportClient(settings)
							.addTransportAddress(new InetSocketTransportAddress("10.10.65.20", 9300))
							.addTransportAddress(new InetSocketTransportAddress("10.10.65.21", 9300));
						}else if(DefaultConfigManager.getInstance().getEnvironmentEnum() == EnvironmentEnum.PRODUCT)
						{
							Settings settings = ImmutableSettings.settingsBuilder()
							        .put("cluster.name", "mobileapp_elasticsearch").build();
							
							instance =  new TransportClient(settings)
							.addTransportAddress(new InetSocketTransportAddress("10.10.65.20", 9300))
							.addTransportAddress(new InetSocketTransportAddress("10.10.65.21", 9300));
						}else
						{
							
						}
					}catch(Exception e)
					{
						logger.error("Get ElasticSearch INSTANCE Error. \n", e);
					}
				}
				Runtime.getRuntime().addShutdownHook(new Cleaner(instance));
			}
		}
		
		return instance;
	}
	
	private ElasticSearchConnectionUtil()
	{
		
	}
	
	

}

class Cleaner extends Thread {
	
	Client client;
	
	public Cleaner(Client client){
		this.client = client;
	}
    public void run() {  
    	client.close();
    }  
}  