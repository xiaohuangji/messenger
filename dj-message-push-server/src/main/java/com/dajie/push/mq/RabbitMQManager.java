package com.dajie.push.mq;

import com.dajie.push.utils.Configuration;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by wills on 3/25/14.
 */
public class RabbitMQManager implements IMQueueManager{

    private static final Logger LOGGER= LoggerFactory.getLogger(RabbitMQManager.class);

    private String host= Configuration.getInstance().getString("rabbit_host");

    private int port=Configuration.getInstance().getInt("rabbit_port");

    private String virtualHost=Configuration.getInstance().getString("rabbit_vhost");

    private String exchange=Configuration.getInstance().getString("rabbit_exchange");

    private String username=Configuration.getInstance().getString("rabbit_user");

    private String password=Configuration.getInstance().getString("rabbit_pwd");

    private static final int ConnectionTimeOut=1000;

    private Channel channel;

    private Connection connection;

    public RabbitMQManager() {

    }

    private Channel getChannel(){
        try{
            if(channel==null||!channel.isOpen()){
                if(connection==null||!connection.isOpen()){
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost(host);
                    factory.setPort(port);
                    factory.setUsername(username);
                    factory.setPassword(password);
                    factory.setVirtualHost(virtualHost);
                    factory.setConnectionTimeout(ConnectionTimeOut);
                    LOGGER.info("reconnect the rabbitmq");
                    connection = factory.newConnection();
                }
                LOGGER.info("rebuild the rabbitmq channel");
                channel = connection.createChannel();
                channel.exchangeDeclare(exchange,"direct",true);
            }
            return channel;
        }catch(IOException e){
            LOGGER.error("rabbitmq create connection fail,",e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public void put(String appId, byte[] data) {
        try{
            Channel channel=getChannel();
            channel.basicPublish(exchange,appId,null,data);
        }catch(IOException e){
            LOGGER.error("rabbit mq publish error,",e);
        }
    }

//    public void consumer()throws Exception{
//        QueueingConsumer consumer = new QueueingConsumer(getChannel());
//        getChannel().basicConsume("common_push_appId1", true, consumer);
//
//        while (true) {
//            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//            String message = new String(delivery.getBody());
//            System.out.println(" [x] Received '" + message + "'");
//        }
//
//    }
//
//    public static void main(String[] args)throws Exception{
//        RabbitMQManager m=new RabbitMQManager();
//        m.put("appId1","refection".getBytes());
//        System.out.println("0000000");
//        m.consumer();
//
//    }
}
