package cn.tedu.cloud_note.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tedu.cloud_note.entity.Note;
import cn.tedu.cloud_note.service.BookError;
import cn.tedu.cloud_note.service.NoteService;
import cn.tedu.cloud_note.uitl.JSONResult;

@Controller
@RequestMapping("/note")
public class NoteController extends AbstractController {
	
	
	@Resource
	NoteService noteservice;
	
	@RequestMapping("/list.do")
	@ResponseBody
	public Object ListNote(String notebookId) {
		List<Map<String, Object>> list = noteservice.findNoteByNoteBookId(notebookId);
		return new JSONResult(list);
	}
	
	@RequestMapping("/load.do")
	@ResponseBody
	public Object NoteBody(String noteId) {
		Note note =
				noteservice.findNoteBodyByNoteId(noteId);
		return new JSONResult(note);
	}
	
	@RequestMapping("/add.do")
	@ResponseBody
	public Object add(String userId, String notebookId, String title) {
		Note note = noteservice.addNote(userId, notebookId,title);
		return new JSONResult(note);
	}

	
	@RequestMapping("/update.do")
	@ResponseBody
	public Object update(String noteId,String title,String body) {
		int success = noteservice.updateNote(noteId, title, body);
		return new JSONResult(success);
	}
	
	
	@RequestMapping("/move.do")
	@ResponseBody
	public Object moveNote(String noteId,String notebookId) {
		int success = noteservice.moveNote(noteId, notebookId);
		return new JSONResult(success);
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object deleteNote(String noteId) {
		boolean success = noteservice.deleteNote(noteId);
		return new JSONResult(success);
	}
	/**
	 *	≤È—Ø± º«“Ï≥£ 
	 * 
	 */
	@ExceptionHandler(BookError.class)
	@ResponseBody
	public JSONResult handlerNoteException(Exception e) {
		System.out.println(e);
		return new JSONResult(5,e);
	}
	
	
	
}
