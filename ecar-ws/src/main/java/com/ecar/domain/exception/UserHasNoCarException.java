package com.ecar.domain.exception;

public class UserHasNoCarException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserHasNoCarException() {
	}

	public UserHasNoCarException(String message) {
		super(message);
	}
}
