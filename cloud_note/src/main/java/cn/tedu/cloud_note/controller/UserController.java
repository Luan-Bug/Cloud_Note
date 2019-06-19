package cn.tedu.cloud_note.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tedu.cloud_note.entity.User;
import cn.tedu.cloud_note.service.PasswordException;
import cn.tedu.cloud_note.service.UserNameException;
import cn.tedu.cloud_note.service.UserNotFoundException;
import cn.tedu.cloud_note.service.UserService;
import cn.tedu.cloud_note.uitl.JSONResult;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController{
	
	@Resource
	UserService userservice;
	
	@RequestMapping("/login.do")
	@ResponseBody
	public Object login (
			String name,String password,HttpSession session) {
			User user = userservice.Login(name, password);
			//System.out.println(new JSONResult(user));
			//登录成功时候, 将user信息保存到session
		    //用于在过滤器中检查登录情况
		    session.setAttribute("loginUser", user); 
			return new JSONResult(user);
	}
	
	@RequestMapping("/add.do")
	@ResponseBody
	public Object addUser(
			String name, String nick, String password, String confirm) {
		User user = userservice.regist(name, nick, password, confirm);
		return new JSONResult(user);
	}
	
	@RequestMapping("/heartbeat.do")
	@ResponseBody
	public Object heartbeat(){
	    Object ok = "ok";
	    return new JSONResult(ok);
	}
	
	/*
	 *处理注册时账号异常 
	 * 
	 */
	@ExceptionHandler(UserNameException.class)
	@ResponseBody
	public JSONResult handleUserNameException(Exception e) {
		return new JSONResult(4,e);
	}
	
	/*
	 *  处理账号异常
	 * 
	 */
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseBody
	public JSONResult handleUserNotFound(Exception e) {
		return new JSONResult(2,e);
		
	}
	/*
	 *  处理密码异常 
	 */
	@ExceptionHandler(PasswordException.class)
	@ResponseBody
	public JSONResult handlePassword(Exception e) {
		return new JSONResult(3, e);
	}
	
}
