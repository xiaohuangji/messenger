package com.dajie.messageadmin.utils;

import com.dajie.messageadmin.model.PageObject;

/**
 * Created by wills on 5/8/14.
 */
public class PageUtil {

    public static PageObject buildPage(int pageSize,int pageNum,int showPage,int totalItem,String link){
        PageObject object=new PageObject();

        int totalPage=((totalItem-1)/pageSize)+1;
        int firstPage=((pageNum-1)/showPage)*showPage+1;
        int lastPage=firstPage+showPage-1;

        object.setCurPage(pageNum);
        object.setTotalPage(totalPage);
        object.setFirstPage(firstPage);
        object.setLastPage(lastPage);
        object.setLink(link);
        return object;
    }
}
