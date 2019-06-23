package cloud_note_test;

import org.junit.Before;
import org.junit.Test;

import cn.tedu.cloud_note.dao.StarsDao;
import cn.tedu.cloud_note.service.NoteService;

public class Starstest extends cloud_note_test.Test{
	
	StarsDao dao;
	NoteService service;
	@Before
	public void initDao() {
		dao = ctx.getBean("starsDao",StarsDao.class);
		service = ctx.getBean("noteService",NoteService.class);
	}
	
	@Test
	public void testaddStars() {
		String userId = "48595f52-b22c-4485-9244-f4004255b972";
		boolean success = service.addStars(userId, 5);
		System.out.println(success);
	}
}
