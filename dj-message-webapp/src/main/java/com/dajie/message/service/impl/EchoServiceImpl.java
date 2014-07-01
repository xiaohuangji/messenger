package com.dajie.message.service.impl;

import org.springframework.stereotype.Component;

import com.dajie.message.service.IEchoService;

@Component("echoService")
public class EchoServiceImpl implements IEchoService{

	@Override
	public String echo() {
		return "hello world";
	}

	@Override
	public String echoWithParams(String echoString) {
		return echoString;
	}

	@Override
	public int echoIntTest(int code) {
		return code;
	}

}
