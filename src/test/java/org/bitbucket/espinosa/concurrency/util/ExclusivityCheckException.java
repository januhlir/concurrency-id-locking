package org.bitbucket.espinosa.concurrency.util;

public class ExclusivityCheckException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExclusivityCheckException() {
		super();
	}

	public ExclusivityCheckException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExclusivityCheckException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExclusivityCheckException(String message) {
		super(message);
	}

	public ExclusivityCheckException(Throwable cause) {
		super(cause);
	}
}
