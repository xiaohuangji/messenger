package com.dajie.push.netty.filter;

import com.dajie.push.netty.channel.NettyChannel;
import com.dajie.push.spring.SpringBeanFactory;

import java.util.List;

/**
 * Created by wills on 5/4/14.
 */
public class PublishFilter extends IFilterChain implements IFilter {

    public PublishFilter() {
        /**
         * messageFilter--->blackListFilter--->iospushFilter
         * messageFilter 消息不合法拦截,特殊消息拦截
         * blackListFilter 黑名单拦截
         * iosPushFilter 前面未被拦截的消息,发push
         */
        this.addFilter(SpringBeanFactory.getBean(PayloadCheckFilter.class));
        this.addFilter(SpringBeanFactory.getBean(BlackListFilter.class));
        this.addFilter(SpringBeanFactory.getBean(PushFilter.class));
    }

    @Override
    public boolean filter(NettyChannel nettyChannel, String destUserId, String payload) {
        List<IFilter> filters=this.getFilterChain();
        for(IFilter filter:filters){
            if(filter.filter(nettyChannel,destUserId,payload)){
                continue;
            }else{
                return false;
            }
        }
        return true;
    }


}
