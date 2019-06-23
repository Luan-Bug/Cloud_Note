package cn.tedu.cloud_note.dao;

import cn.tedu.cloud_note.entity.Stars;

public interface StarsDao {
	Stars findStarsByuUserId(String userId);
	
	int insertStars(Stars stars);
	
	int updataStars(Stars stars);
}
