package com.dajie;

import com.dajie.push.mqtt.decoder.MQTTDecoderHandler;
import com.dajie.push.mqtt.encoder.MQTTEncoderHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;

/**
 * Created by wills on 3/12/14.
 */
public class TestChannel {

   // @Test
    public void testEchoHandle(){
        //ByteBuf byteBuf= Unpooled.buffer();
        EmbeddedChannel channel=new EmbeddedChannel(null);
        String msg="ssss";
        channel.writeInbound(msg);
        Assert.assertEquals(msg, channel.readOutbound());
    }


    //@Test
    public void testMQTTDecoder(){
        EmbeddedChannel channel=new EmbeddedChannel(new MQTTDecoderHandler());
        String msg="ssss";
        channel.writeInbound(msg);
        Assert.assertEquals(msg, channel.readInbound());
    }

   // @Test
    public void testMQTTEncoder(){
        EmbeddedChannel channel=new EmbeddedChannel(new MQTTEncoderHandler());
        String msg="ssss";
        channel.writeOutbound(msg);

        ByteBuf byteBuf=channel.readOutbound();
        byte[] bytes=new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        for(int i=0;i<msg.length();i++){
            Assert.assertEquals(msg.charAt(i),(char)bytes[0]);
        }

    }
}
