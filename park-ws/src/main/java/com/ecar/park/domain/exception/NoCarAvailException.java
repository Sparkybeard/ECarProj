package com.ecar.park.domain.exception;

/** Exception used to signal that no car is currently available in a park. */
public class NoCarAvailException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoCarAvailException() {
	}

	public NoCarAvailException(String message) {
		super(message);
	}
}
