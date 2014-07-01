package com.dajie.message.model.message;

/**
 * Created by wills on 5/14/14.
 */
public class GoudaMessageBuilder {

    private GoudaMessage message;

    public GoudaMessageBuilder() {
        this.message=new GoudaMessage();
        message.setTime(System.currentTimeMillis());
    }

    public GoudaMessageBuilder from(int from){
        message.setFrom(from);
        return this;
    }
    public GoudaMessageBuilder to(int to){
        message.setTo(to);
        return this;
    }

    public int getFrom(){
        return message.getFrom();
    }

    public int getTo(){
        return message.getTo();
    }

    public GoudaMessageBuilder content(AbstractContent content){
        message.setContent(content);
        if(content instanceof ChatContent){
            message.setMsgType(GoudaMessage.CHAT);
            if(content instanceof Text){
                message.setContentType(ChatContent.TEXT);
            }else if(content instanceof Image){
                message.setContentType(ChatContent.IMAGE);
            }else if(content instanceof Link){
                message.setContentType(ChatContent.LINK);
            }else if(content instanceof Location){
                message.setContentType(ChatContent.LOCATION);
            }else if(content instanceof Sound){
                message.setContentType(ChatContent.SOUND);
            }
        }else if(content instanceof SystemContent){
            message.setMsgType(GoudaMessage.SYSTEM);
            if(content instanceof CardRequest){
                message.setContentType(SystemContent.CARDREQUEST);
            }else if(content instanceof CardAgree){
                message.setContentType(SystemContent.CARDAGREE);
            }else if(content instanceof UserBlock){
                message.setContentType(SystemContent.USERBLOCK);
            }else if(content instanceof LikeLabel){
                message.setContentType(SystemContent.LIKELABEL);
            }else if(content instanceof UnFriend){
                message.setContentType(SystemContent.UNFRIEND);
            }else if(content instanceof UserUpdate){
                message.setContentType(SystemContent.USERUPDATE);
            }
        }
        return this;
    }


    public GoudaMessage getMessage(){
        return message;
    }

}
