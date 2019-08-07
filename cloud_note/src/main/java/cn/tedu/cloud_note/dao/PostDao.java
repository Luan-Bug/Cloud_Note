package cn.tedu.cloud_note.dao;

import cn.tedu.cloud_note.entity.Post;

public interface PostDao {
	Post findPostById(int id);
}
