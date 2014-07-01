package com.dajie.push.netty.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wills on 5/4/14.
 */
abstract class IFilterChain {

    private List<IFilter> filterChain=new ArrayList<IFilter>();

    public void addFilter(IFilter filter){
        filterChain.add(filter);
    }

    public List<IFilter> getFilterChain(){
        return filterChain;
    }
}
