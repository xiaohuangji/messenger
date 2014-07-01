package com.dajie.push.netty.filter;

import com.dajie.message.constants.GoudaConstant;
import com.dajie.message.model.message.GoudaMessageBuilder;
import com.dajie.message.model.message.UserBlock;
import com.dajie.message.model.system.PrivacySetting;
import com.dajie.push.mqtt.MQTTTopicUtils;
import com.dajie.push.mqtt.message.AbstractMessage;
import com.dajie.push.mqtt.message.PublishMessage;
import com.dajie.push.netty.channel.NettyChannel;
import com.dajie.push.storage.dao.BlackListDAO;
import com.dajie.push.storage.dao.FriendsDAO;
import com.dajie.push.storage.dao.PrivacySettingDAO;
import com.dajie.push.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by wills on 5/4/14.
 * 黑名单以及聊天开关过滤
 * 第一期直接通过dao操作。
 * 后期可以考虑调用dubbo-service，service中增加redis缓存。
 */
public class BlackListFilter implements IFilter {

    private static final Logger LOGGER= LoggerFactory.getLogger(BlackListFilter.class);

    private BlackListDAO blackListDAO;

    private PrivacySettingDAO privacySettingDAO;

    private FriendsDAO friendsDAO;

    public void setFriendsDAO(FriendsDAO friendsDAO) {
        this.friendsDAO = friendsDAO;
    }

    public void setPrivacySettingDAO(PrivacySettingDAO privacySettingDAO) {
        this.privacySettingDAO = privacySettingDAO;
    }

    public void setBlackListDAO(BlackListDAO blackListDAO) {
        this.blackListDAO = blackListDAO;
    }

    private boolean isFriend(int userId,int friendId){
        return friendsDAO.isFriend(userId, friendId) > 0 ? true : false;
    }

    private boolean isBlocked(int userId,int userId2){
        Integer chatFlag=privacySettingDAO.getChatNotificationSetting(userId);
        if(chatFlag!=null){
            if(chatFlag.equals(PrivacySetting.CHAT_EVERYONE)){
                //do nothing

            }else if(chatFlag.equals(PrivacySetting.CHAT_FRIEND)){
                //只针对接受朋友聊天
                if(!isFriend(userId,userId2)){
                    LOGGER.info("this user has limit friend chat:"+userId);
                    return true;
                }
            }else if(chatFlag.equals(PrivacySetting.CHAT_DISABLE)){
                //禁用了聊天
                LOGGER.info("this user has disable the chat:"+userId);
                return true;
            }
        }
        return blackListDAO.isBlocked(userId,userId2) > 0 ? true : false;
    }

    @Override
    public boolean filter(NettyChannel channel, String destUserId, String payload) {
        String sourceUserId=channel.getUserId();
        if(sourceUserId==null){
            //source为空时表示没有订阅。说明是服务端发送。直接通过。
            return  true;
        }
        if(isBlocked(Integer.valueOf(destUserId),Integer.valueOf(sourceUserId))){
            LOGGER.debug("blackList filter,userId:"+destUserId);
            //推送拒绝消息
            PublishMessage publishMessage=new PublishMessage();
            GoudaMessageBuilder builder=new GoudaMessageBuilder().
                                from(Integer.valueOf(destUserId)).
                                to(Integer.valueOf(sourceUserId)).
                                content(new UserBlock());
            byte[] bytes= JsonUtil.toJson(builder.getMessage()).getBytes();
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();

            publishMessage.setPayload(byteBuffer);
            publishMessage.setTopicName(MQTTTopicUtils.genTopic(GoudaConstant.GOUDA_APPID,sourceUserId));
            publishMessage.setQos(AbstractMessage.QOSType.MOST_ONE);

            channel.getChannel().writeAndFlush(publishMessage);
            return false;
        }
        return true;
    }
}
