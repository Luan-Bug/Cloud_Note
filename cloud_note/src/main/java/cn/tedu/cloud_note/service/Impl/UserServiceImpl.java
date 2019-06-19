package cn.tedu.cloud_note.service.Impl;

import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.tedu.cloud_note.dao.UserDao;
import cn.tedu.cloud_note.entity.User;
import cn.tedu.cloud_note.service.PasswordException;
import cn.tedu.cloud_note.service.UserNameException;
import cn.tedu.cloud_note.service.UserNotFoundException;
import cn.tedu.cloud_note.service.UserService;


@Service("userService")
public class UserServiceImpl implements UserService{
	
	@Resource(name="userDao")
	private UserDao userDao;
	
	@Value("#{db.salt}")
	private String salt;
	
	//实现登录方法
	public User Login(String name, String password) 
			throws UserNotFoundException, PasswordException {
		if(password == null || 
				password.trim().isEmpty()){
			throw new PasswordException("密码为空");
		}
		if(name == null ||
				name.trim().isEmpty()) {
			throw new UserNotFoundException("用户名为空");
		}
		User u = userDao.FindUserByName(name);
		if(u == null) {
			throw new UserNotFoundException("用户名错误");
		}
		String pwd = DigestUtils.md5Hex(salt+password);
		if(pwd.equals(u.getPassword())) {
			return u;
		}
		throw new PasswordException("密码错误");
	}
	
	//实现注册方法
	public User regist(
			String name, String nick, String password, String confirm) 
					throws UserNameException {
		//检查name不重复，且不为空
		if(name == null || name.trim().isEmpty()) {
			throw new UserNameException("账号为空");
		}
		User one = userDao.FindUserByName(name);
		if(one != null) {
			throw new UserNameException("账号已经注册");
		}
		//检查密码
		if(password == null || password.trim().isEmpty()) {
			throw new PasswordException("密码为空");
		}
		if(!password.equals(confirm)) {
			throw new PasswordException("两次密码不一致");
		}
		//检查nick
		if(nick==null || nick.trim().isEmpty()) {
			nick = name;
		}
		String id = UUID.randomUUID().toString();
		String token = "";
		password = DigestUtils.md5Hex(salt+password);
		User user = new User(id, name, password, token, nick); 
		int n = userDao.AddUser(user);
		if(n!=1) {
			throw new RuntimeException("添加失败");
		}
		return user;
	}


}
