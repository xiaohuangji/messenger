package com.dajie.push.netty.handler;

import com.dajie.push.netty.channel.ConnectionStats;
import com.dajie.push.netty.stats.PushStats;
import com.dajie.push.utils.Configuration;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wills on 3/12/14.
 */
public class DisruptorMqttMessageHandler implements IMqttMessageHandler {

    private static final Logger LOGGER= LoggerFactory.getLogger(DisruptorMqttMessageHandler.class);

    private Disruptor<MessageEvent> disruptor;

    private IMqttMessageHandler mqttMessageHandler;

    private static final int RINGBUFSIZE=Configuration.getInstance().getInt("disruptor_ringbuf");

    private static final int CONSUMERSIZE=Configuration.getInstance().getInt("disruptor_consumer");

    public DisruptorMqttMessageHandler(IMqttMessageHandler mqttMessageHandler){
        this.mqttMessageHandler=mqttMessageHandler;
        ExecutorService exec= Executors.newCachedThreadPool();
        disruptor=new Disruptor<MessageEvent>(EVENT_FACTORY,RINGBUFSIZE,exec);
        WorkHandler[] workHandlers=new WorkHandler[CONSUMERSIZE];
        for(int i=0;i<CONSUMERSIZE;i++){
            workHandlers[i]=new DisruptorConsumer();
        }
        disruptor.handleEventsWithWorkerPool(workHandlers);
        LOGGER.info("start disruptor");
        disruptor.start();
    }

    @Override
    public void handleMqttMessage(MessageEvent event) {
        disruptor.publishEvent(new DisruptorProducer(event));
    }

    @Override
    public PoolStats getPoolStats() {
        PoolStats stats=this.mqttMessageHandler.getPoolStats();
        stats.setRingbuf(new Long(this.disruptor.getCursor()).intValue());
        return stats;
    }

    @Override
    public void removeChannel(Channel channel) {
        this.mqttMessageHandler.removeChannel(channel);
    }

    @Override
    public PushStats getPushStats() {
        return this.mqttMessageHandler.getPushStats();
    }

    @Override
    public ConnectionStats getConnStats() {
        return this.mqttMessageHandler.getConnStats();
    }

    @Override
    public int flushPushStatsToDB() {
        return this.mqttMessageHandler.flushPushStatsToDB();
    }

    public final  EventFactory<MessageEvent> EVENT_FACTORY=new EventFactory<MessageEvent>() {
        @Override
        public MessageEvent newInstance() {
            return new MessageEvent();
        }
    };

    class DisruptorProducer implements EventTranslator<MessageEvent>{

        private MessageEvent event;

        public DisruptorProducer(MessageEvent event){
            this.event=event;
        }
        @Override
        public void translateTo(MessageEvent event, long sequence) {
            //set event
            //LOGGER.debug("disruptor producer process, id:"+Thread.currentThread().getId());
            event.setAbstractMessage(this.event.getAbstractMessage());
            event.setChannel(this.event.getChannel());
        }
    }

    class DisruptorConsumer implements WorkHandler<MessageEvent> {
        @Override
        public void onEvent(MessageEvent event) throws Exception {
            //LOGGER.debug("disruptor consumer process, id:"+Thread.currentThread().getId());
            mqttMessageHandler.handleMqttMessage(event);
        }
    }
}
