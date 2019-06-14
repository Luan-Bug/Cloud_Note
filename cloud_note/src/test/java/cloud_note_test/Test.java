package cloud_note_test;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.tedu.cloud_note.dao.UserDao;
import cn.tedu.cloud_note.entity.User;
import cn.tedu.cloud_note.service.UserNameException;
import cn.tedu.cloud_note.service.UserService;


public class Test {
	
	ClassPathXmlApplicationContext ctx;
	@Before 
	public void initCtx(){
		ctx = new ClassPathXmlApplicationContext(
				"conf/spring-mvc.xml",
				"conf/spring-mybatis.xml");
	}
	@After
	public void closeCtx(){
		ctx.close();
	}
	
	
	@org.junit.Test
	public void test1() {
		String name = "demo";
		UserDao dao = ctx.getBean(
			"userDao", UserDao.class);
		User user = dao.FindUserByName(name);
		System.out.println(user); 
	}
	
	@org.junit.Test
	public void test2() {
		String name = "demo";
		String pass = "123456";
		UserService userservice = ctx.getBean("userService", UserService.class);
		User u = userservice.Login(name, pass);
		System.out.println(u);
	}
	
	@org.junit.Test
	public void test3() {
		User user = new User();
		String id = UUID.randomUUID().toString();
		System.out.println(id);
		String name = "admin";
		String password = "123456";
		String salt = "≥‘∑π¡À√¥";
		password = DigestUtils.md5Hex(salt+password);
		user.setId(id);
		user.setName(name);
		user.setPassword(password);
		UserDao dao = ctx.getBean("userDao",UserDao.class);
		dao.AddUser(user);
	}
	
	@org.junit.Test
	public void test4() throws UserNameException {
		UserService userservice = ctx.getBean("userService", UserService.class);
		User user = userservice.regist("6666", "", "123456", "123456");
		System.out.println(user);
	}
}
