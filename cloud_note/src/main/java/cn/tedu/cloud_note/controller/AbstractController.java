package cn.tedu.cloud_note.controller;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tedu.cloud_note.uitl.JSONResult;

public abstract class AbstractController {

	
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public JSONResult handleExcaption(Exception e) {
		e.printStackTrace();
		return new JSONResult(e);
	}

}