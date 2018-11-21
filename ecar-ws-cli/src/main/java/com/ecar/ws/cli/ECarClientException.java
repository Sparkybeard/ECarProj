package com.ecar.ws.cli;

public class ECarClientException extends Exception {

	private static final long serialVersionUID = 1L;

	public ECarClientException() {
	}

	public ECarClientException(String message) {
		super(message);
	}

	public ECarClientException(Throwable cause) {
		super(cause);
	}

	public ECarClientException(String message, Throwable cause) {
		super(message, cause);
	}

}