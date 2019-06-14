package cn.tedu.cloud_note.service;

import java.util.List;
import java.util.Map;


public interface NotebookService {
	public List<Map<String, Object>> findNotebookByUserId(String id)
				throws UserNotFoundException;
	public Map<String, Object> addNotebook(String name,String userId) 
				throws BookError;
}
