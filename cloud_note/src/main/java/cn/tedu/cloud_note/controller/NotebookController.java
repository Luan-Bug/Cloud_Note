package cn.tedu.cloud_note.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tedu.cloud_note.service.NotebookService;
import cn.tedu.cloud_note.service.UserNotFoundException;
import cn.tedu.cloud_note.uitl.JSONResult;



@Controller
@RequestMapping("/notebook")
public class NotebookController {

	@Resource
	protected NotebookService notebookservice;
	
	@RequestMapping("/list.do")
	@ResponseBody
	public JSONResult list(String userid) {
		
		List<Map<String, Object>> list = 
				notebookservice.findNotebookByUserId(userid);
		
		return new JSONResult(list);
	}
	
	@RequestMapping("/add.do")
	@ResponseBody
	public JSONResult add(@RequestParam("name")String name,String userId) {
		Map<String, Object> notebook = 
				notebookservice.addNotebook(name, userId);
		return new JSONResult(notebook);
	}
	
	@RequestMapping("/page.do")
	@ResponseBody
	public JSONResult page(String userId,int page) {
		List<Map<String, Object>> list = notebookservice.findNotebookByUserId(userId, page);
		return new JSONResult(list);
	}
	
	/**
	 *	 笔记本列表异常 
	 * 
	 */
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseBody
	public JSONResult handlerNotebookListException(Exception e) {
		System.out.println(e);
		return new  JSONResult(2,e);
	}
	
}
