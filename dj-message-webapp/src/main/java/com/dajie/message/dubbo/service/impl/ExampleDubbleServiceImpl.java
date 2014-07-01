package com.dajie.message.dubbo.service.impl;

import org.springframework.stereotype.Component;

import com.dajie.message.dubbo.service.ExampleDubbleService;

@Component("exampleDubbleService")
public class ExampleDubbleServiceImpl implements ExampleDubbleService{

	@Override
	public String sayHello(String input) {
		return "hello world "+input;
	}

}
