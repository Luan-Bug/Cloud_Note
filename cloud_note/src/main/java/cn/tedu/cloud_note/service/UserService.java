package cn.tedu.cloud_note.service;

import cn.tedu.cloud_note.entity.User;
/**
 * 
  *   业务持久层接口
 * @author LCD
 *
 */
public interface UserService {
	/**
	  *  登录功能
	 * @param name 用户名
	 * @param password 密码
	 * @return 登陆成功返回用户信息
	 * @throws UserNotFoundException 用户不存在
	 * @throws PasswordException 密码错误
	 */
	User Login(String name,String password) 
		throws UserNotFoundException,PasswordException;
	public User regist(
			String name, String nick, String password, String confirm) 
			throws UserNameException;
}
