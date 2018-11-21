package com.ecar.domain.exception;

public class UserAlreadyHasCarException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserAlreadyHasCarException() {
	}

	public UserAlreadyHasCarException(String message) {
		super(message);
	}
}
