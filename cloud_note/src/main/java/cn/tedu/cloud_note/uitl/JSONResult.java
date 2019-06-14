package cn.tedu.cloud_note.uitl;

import java.io.Serializable;

public class JSONResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	
	public final static int SUCCESS = 0;
	public final static int ERROR = 1;
	
	//返回错误或成功的状态码
	private int state;
	
	//返回错误消息
	private String message;
	
	//验证成功返回的数据
	Object data;
	public JSONResult(int state,Throwable e){
		this.state = state;
		message = e.getMessage();
	}
	
	public JSONResult(Throwable e){
		message = e.getMessage();
	}
	public JSONResult(Object data) {
		state = SUCCESS;
		this.data = data;
	}

	public int getstate() {
		return state;
	}

	public void setstate(int state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String toString() {
		return "ExceptionResult [state=" + state + ", message=" + message + ", data=" + data + "]";
	}
	
}
