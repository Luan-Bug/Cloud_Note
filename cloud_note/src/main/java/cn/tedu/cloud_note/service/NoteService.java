package cn.tedu.cloud_note.service;

import java.util.List;
import java.util.Map;

import cn.tedu.cloud_note.entity.Note;

public interface NoteService {
	List<Map<String, Object>> findNoteByNoteBookId(String id) 
			throws BookError;
	Note findNoteBodyByNoteId(String id);
	Note addNote(String userId,String notebookId,String noetName);
	int updateNote(String noteId, String title, String body) 
			throws BookError;
	int moveNote(String noteId,String notebookId) 
			throws BookError;
	boolean deleteNote(String noteId)
		    throws BookError;
	List<Note> findNoteByuserId(String userId)
			throws BookError;
	int deleteNotebynoteId(String... noteId)
			throws BookError;
	int reovkeNote(String noteId,String nId)
			throws BookError;
	boolean addStars(String userId,int stars)
			throws BookError;
}
