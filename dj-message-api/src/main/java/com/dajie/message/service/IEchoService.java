package com.dajie.message.service;

import com.dajie.message.annotation.mcp.NoTicket;
import com.dajie.message.annotation.rest.RestBean;

@RestBean
public interface IEchoService {

	@NoTicket
	public String echo();
	
	public String echoWithParams(String echoString);
	
	public int echoIntTest(int code);
	
}
