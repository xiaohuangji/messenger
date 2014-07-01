package com.dajie.message.mcp.service;

import com.dajie.message.mcp.model.UserPassport;

/**
 * passport 服务，就是t票服务
 * @author xinquan.guan
 *
 */

public interface IPassportService {
	/**
	 * 通过ticket，换取UserPassport
	 * @param ticket
	 * @return
	 */
	 public UserPassport getPassportByTicket(String ticket);
	 
	 /**
	  * 通过userPassport中的userId，创建passport
	  * @param userPassport
	  * @return
	  */
	 public String createTicket(UserPassport userPassport);
	 
	 
	 /**
	  * 创建一个passport
	  * @param userPassport
	  * @return
	  */
	 public UserPassport createPassport(UserPassport userPassport);
	 
	 /**
	  * 
	  * @param userId
	  */
	 public void removeTicket(int userId);
	 
	 /**
	  * 
	  * @param sig 获取sig对应的值
	  * @return
	  */
	 public Integer getSig(String sig);
	 
	 /**
	  * 设置sig的值
	  * @param sig
	  * @param value
	  */
	 public void addSig(String sig,Integer value);
	  
}
