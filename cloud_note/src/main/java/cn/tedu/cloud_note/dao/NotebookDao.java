package cn.tedu.cloud_note.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.tedu.cloud_note.entity.Notebook;

@Repository
public interface NotebookDao {
	public List<Map<String, Object>> findNotebookByUserId(String id);
	public int countNotebookById(String notebookId);
	public int addNoteBook(Notebook notebook);
	public int findNotebookByuIdAndNoteBookName(@Param("name")String name,@Param("uId")String uId);
	public Map<String, Object> findNotebookbyNoteBookId(@Param("notebookId")String notebookId);
}
