package cloud_note_test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cn.tedu.cloud_note.dao.NotebookDao;
import cn.tedu.cloud_note.entity.Notebook;
import cn.tedu.cloud_note.service.NotebookService;

public class notebooktest extends cloud_note_test.Test {
	NotebookDao dao ;
	NotebookService service;
	@Before
	public void initDao() {
		dao = ctx.getBean("notebookDao",NotebookDao.class);
		service = ctx.getBean("notebookservice",NotebookService.class);
	}
	@Test 
	public void testFindbooksByidDao() {
		String id = "03590914-a934-4da9-ba4d-b41799f917d1";
		List<Map<String, Object>> list = dao.findNotebookByUserId(id);
		for(Map<String, Object> map :list) {
			System.out.println(map);
		}
	}
	
	@Test
	public void FinfbookByidServicetest() {
		String id = "48595f52-b22c-4485-9244-f4004255b972";
		List<Map<String, Object>> list = service.findNotebookByUserId(id);
		for(Map<String, Object> map :list) {
			System.out.println(map);
		}
	}
	
	@Test
	public void addNotebookDaotest() {
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		Notebook notebook = new Notebook("adaaasd", "dao测试2", "", "", "",timestamp);
		int count = dao.addNoteBook(notebook);
		System.out.println("已经添加了"+count+"条数据");
	}

	
	@Test
	public void countNotebookByIdDaotest() {
		String notebookId = "6d763ac9-dca3-42d7-a2a7-a08053095c08";
		int i = dao.countNotebookById(notebookId);
		System.out.println(i);
	}
	
	@Test
	public void findNotebookByuIdAndNoteBookNameDaotest() {
		String name = "Demo";
		String uId = "39295a3d-cc9b-42b4-b206-a2e7fab7e77c";
		int list = dao.findNotebookByuIdAndNoteBookName(name, uId);
		System.out.println(list);
	}
	
	@Test
	public void addNotebookServicetest() {
		String name = "啦啦啦";
		String uId = "39295a3d-cc9b-42b4-b206-a2e7fab7e77c";
		Map<String, Object> notebook = service.addNotebook(name, uId);
		System.out.println(notebook);
	}
	
	@Test
	public void findnotebookbyNoteBookIdDaotest() {
		String notebookid = "baae29ac-7eaf-40eb-81d8-f4c7f7cc76ea";
		Map<String, Object> notebook = dao.findNotebookbyNoteBookId(notebookid);
		System.out.println(notebook);
	}
	
	@Test
	public void FinfbookBypageSizeServicetest() {
		String id = "48595f52-b22c-4485-9244-f4004255b972";
		List<Map<String, Object>> list = service.findNotebookByUserId(id, 2);
		for(Map<String, Object> map :list) {
			System.out.println(map);
		}
	}
	
	
}

