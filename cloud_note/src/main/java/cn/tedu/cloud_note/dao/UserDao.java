package cn.tedu.cloud_note.dao;

import org.springframework.stereotype.Repository;

import cn.tedu.cloud_note.entity.User;

@Repository("userDao")
public interface UserDao {
	public User FindUserByName(String name);
	public int AddUser(User user);
	public User FindUserById(String id);
}
