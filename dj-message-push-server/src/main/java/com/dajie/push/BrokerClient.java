package com.dajie.push;

import com.dajie.push.mqtt.decoder.MQTTDecoderHandler;
import com.dajie.push.mqtt.encoder.MQTTEncoderHandler;
import com.dajie.push.mqtt.message.CommandMessage;
import com.dajie.push.mqtt.message.PublishMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;

/**
 * Created by wills on 4/1/14.
 */
public class BrokerClient{

    private final String host;
    private final int port;
    private Channel channel;

    private EventLoopGroup group = new NioEventLoopGroup();

    public BrokerClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new HandlerInitializer(this));
            ChannelFuture f = b.connect().sync();
            channel=f.channel();
        }catch(Exception e){
            Out.print(e);
        }
    }

    public void close() throws Exception{
        group.shutdownGracefully().sync();
        Out.print("\n");
    }

    public boolean isConnect(){
        return channel==null?false:channel.isOpen();
    }

    public void sendCommmand(String command)throws Exception{
        synchronized (this){
            command.replace("\r\n","");
            CommandMessage commandMessage=new CommandMessage();
            commandMessage.setCommand(command);
            channel.writeAndFlush(commandMessage);
            this.wait();
        }

    }

    class HandlerInitializer extends ChannelInitializer<SocketChannel>{

        private  BrokerClient nettyClient;

        HandlerInitializer(BrokerClient nettyClient) {
            this.nettyClient = nettyClient;
        }

        @Override
        public void initChannel(SocketChannel ch)
                throws Exception {
            ch.pipeline().addLast("mqttdecoder", new MQTTDecoderHandler());
            ch.pipeline().addLast("mqttencoder", new MQTTEncoderHandler());
            ch.pipeline().addLast("handle",new ClientHandler(nettyClient));
        }
    }

    class ClientHandler extends SimpleChannelInboundHandler{

        private BrokerClient nettyClient;

        ClientHandler(BrokerClient nettyClient) {
            this.nettyClient=nettyClient;
        }

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
            if(msg instanceof PublishMessage){
                synchronized (nettyClient){
                    PublishMessage message=(PublishMessage)msg;
                    byte[] content=message.getPayload().array();
                    Out.print(new String(content) + "\n");
                    nettyClient.notifyAll();
                }
            }else{
                Out.print("not publish message");
            }
        }
    }

    public static void main(String[] args)throws Exception{

        String host="localhost";
        int port=1883;
        if(args.length>0&&args[0].length()!=0){
            try{
                String server=args[0];
                host=server.split(":")[0];
                port=Integer.valueOf(server.split(":")[1]);
            }
            catch(Exception e){
                host="localhost";
                port=1883;
                Out.print("server is not valid");
            }
        }
        BrokerClient client=new BrokerClient(host,port);
        client.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        int index=0;
        while (client.isConnect()) {
            Out.print("["+host+":"+port+" "+index+"]");
            String line = in.readLine();
            if(line==null||line.equals("\n")||line.equals("")){
                continue;
            }
            if (line.toLowerCase().equals("exit")||line.toLowerCase().equals("quit")) {
                Thread t=new Thread(new PrintBye());
                t.setDaemon(true);
                t.start();
                break;
            }

            client.sendCommmand(line);
            index++;
        }
        client.close();
    }


}

class Out {
    static private BufferedWriter out=new BufferedWriter(new OutputStreamWriter(System.out));

    static void print(Object o){
        try{
            out.write(String.valueOf(o));
            out.flush();
        }catch(Exception e){

        }
    }
}
class PrintBye implements Runnable{
    @Override
    public void run() {
        Out.print("close client, please wait");
        for(;;){
            try{
                Out.print(".");
                Thread.sleep(1500);
            }catch(Exception e){

            }
        }

    }
}
