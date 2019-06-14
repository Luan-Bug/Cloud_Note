package cn.tedu.cloud_note.dao;

import java.util.List;
import java.util.Map;

import cn.tedu.cloud_note.entity.Note;

public interface NoteDao {
	List<Map<String, Object>> findNoteByNoteBookId(String id);
	Note findNoteBodyByNoteId(String id);
	int addNote(Note note);
	int updateNoteByNote(Note note);
	Note findNoteByNoteId(String noteId);
}
