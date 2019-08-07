package cn.tedu.cloud_note.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.tedu.cloud_note.entity.Note;

public interface NoteDao {
	List<Map<String, Object>> findNoteByNoteBookId(String id);
	Note findNoteBodyByNoteId(String id);
	int addNote(Note note);
	int updateNoteByNote(Note note);
	Note findNoteByNoteId(String noteId);
	List<Note> findNoteByuserId(String statusId);
	int deleteNoteByNoteId(String... noteId);
	List<Map<String, Object>> findNotes(
			@Param("userId") String userId,
			@Param("notebookId") String notebookId,
			@Param("statusId") String statusId
			);
	int deleteNotes(@Param("ids") String... ids);
}
