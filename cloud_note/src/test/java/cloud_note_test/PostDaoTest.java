package cloud_note_test;

import org.junit.Before;
import org.junit.Test;

import cn.tedu.cloud_note.dao.PostDao;
import cn.tedu.cloud_note.entity.Post;

public class PostDaoTest extends cloud_note_test.Test{
	PostDao dao;
	@Before
	public void init() {
		dao = ctx.getBean("postDao",PostDao.class);
	}
	@Test
	public void PostdaoTest() {
		Post post = dao.findPostById(1);
		System.out.println(post);
	}
}
