package cn.tedu.cloud_note.service.Impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import cn.tedu.cloud_note.dao.NoteDao;
import cn.tedu.cloud_note.dao.NotebookDao;
import cn.tedu.cloud_note.dao.UserDao;
import cn.tedu.cloud_note.entity.Note;
import cn.tedu.cloud_note.entity.User;
import cn.tedu.cloud_note.service.BookError;
import cn.tedu.cloud_note.service.NoteService;
import cn.tedu.cloud_note.service.UserNotFoundException;

@Service("noteService")
public class NoteServiceImpl implements NoteService {
	
	@Resource
	NoteDao notedao;
	@Resource
	UserDao userDao;
	@Resource
	NotebookDao notebookDao;
	
	public List<Map<String, Object>> findNoteByNoteBookId(String id) {
		if(id==null) {
			throw new BookError("未知笔记本");
		} 
		
		List<Map<String, Object>> notelist = 
				notedao.findNoteByNoteBookId(id);
		return notelist;
		
	}

	public Note findNoteBodyByNoteId(String id) {
		
		Note notebody = 
				notedao.findNoteBodyByNoteId(id);
		return notebody;
	
	}

	public Note addNote(String userId, String notebookId, String title) 
						throws 
						UserNotFoundException, 
						BookError{
		if(userId==null||userId.trim().isEmpty()){
			throw new UserNotFoundException("ID空");
		}
		User user=userDao.FindUserById(userId);
		if(user==null){
			throw new UserNotFoundException("木有人");
		}
		if(notebookId==null||notebookId.trim().isEmpty()){
			throw new BookError("ID空");
		}
		int n=notebookDao.countNotebookById(notebookId);
		if(n!=1){
			throw new BookError("没有笔记本");
		}
		if(title==null || title.trim().isEmpty()){
			title="葵花宝典";
		}
		String id = UUID.randomUUID().toString();
		String statusId = "1";
		String typeId = "0";
		String body = "";
		long time=System.currentTimeMillis();
		Note note = new Note(id, notebookId,
			userId, statusId, typeId, title, 
			body, time, time);
		n = notedao.addNote(note);
		if(n!=1){
			throw new BookError("保存失败");
		}
		return note;
	}

	public int updateNote(String noteId, String title, 
	        String body) throws BookError{
		
		if(noteId==null || noteId.trim().isEmpty()){
	        throw new BookError("ID不能空");
	    }
		Note note = notedao.findNoteBodyByNoteId(noteId);
		if(note==null) {
			throw new BookError("没有这个笔记吧？？");
		}
		if(title!=null) {
			note.setTitle(title);
		}
		if(body!=null) {
			note.setBody(body);
		}
		long time = System.currentTimeMillis();
		note.setLastModifyTime(time);
		int cont = notedao.updateNoteByNote(note);
		return cont;
	}

	
	public int moveNote(String noteId, String notebookId) throws BookError {
		Note note = new Note();
		note.setId(noteId);
		note.setNotebookId(notebookId);
		int cont = notedao.updateNoteByNote(note);
		return cont;
	}

	public boolean deleteNote(String noteId) throws BookError {
		 if(noteId==null || noteId.trim().isEmpty()){
		        throw new BookError("ID不能空");
		    }
		    Note note = notedao.findNoteByNoteId(noteId);
		    if(note==null){
		        throw new BookError("没有对应的笔记");
		    } 
	    Note data = new Note();
	    data.setId(noteId);
	    data.setStatusId("0");
	    data.setLastModifyTime(System.currentTimeMillis());

	    int n = notedao.updateNoteByNote(data);
	    return n==1;   
	}

	@SuppressWarnings("null")
	public List<Note> findNoteByuserId(String userId) throws BookError {
		if(userId==null && userId.trim().isEmpty()) {
			throw new BookError("ID空");
		}
		User user = userDao.FindUserById(userId);
		if(user==null) {
			throw new BookError("没有这个人");
		}
		List<Note> noteslist = 
				notedao.findNoteByuserId(userId);
		return noteslist;
	}
	
	public int deleteNotebynoteId(String noteId) {
		if (noteId==null) {
			throw new BookError("ID空");
		}
		Note note = notedao.findNoteByNoteId(noteId);
		System.out.println(note);
		if(note==null) {
			throw new BookError("笔记不存在");
		}
		int cont = notedao.deleteNoteByNoteId(noteId);
		return cont;
	}

	public int reovkeNote(String noteId,String nId) {
		if(noteId==null){
			throw new BookError("笔记ID跑丢了");
		}
		Note note = notedao.findNoteBodyByNoteId(noteId);
		if(note==null) {
			throw new BookError("没有这个笔记，出了个小bug");
		}
		String statusId = "1";
		note.setNotebookId(nId);
		note.setStatusId(statusId);
		int cont = notedao.updateNoteByNote(note);
		return cont;
	}
}
