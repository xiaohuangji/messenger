package com.dajie.message.mcp.defaulthandler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("defaultHandler")
@RequestMapping("/api")
public class DefaultHandler{

	@RequestMapping("/**")
    @ResponseBody
	public int defaultHandler(){
        return 0;
	}
}
