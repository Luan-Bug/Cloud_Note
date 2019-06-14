package cn.tedu.cloud_note.service;

public class BookError extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BookError() {
		// TODO Auto-generated constructor stub
	}

	public BookError(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BookError(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public BookError(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BookError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
