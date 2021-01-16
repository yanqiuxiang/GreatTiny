package com.yqx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
public class Test {
	
	
	@RequestMapping(value = "/test", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String test(int pageNumber) throws Exception {
		
		JSONArray js = new JSONArray();
		for(int i=0;i<9;i++) {
			JSONObject jo = new JSONObject(); 
			jo.put("src", "http://qn2.wkmblog.com/FrBnGzapQmFwvb-PhspYaxkXE7_T?imageView2/1/w/160/h/90");
			jo.put("space", "我不知道在等待着什么,就好像不知道有什么在等待着我<"+i+">");
			jo.put("space1", "等待是什么<"+i+">");
			js.add(jo);
		}
		
		return js.toString();
	}

}
