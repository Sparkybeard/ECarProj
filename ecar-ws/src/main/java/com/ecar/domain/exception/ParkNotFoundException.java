package com.ecar.domain.exception;

public class ParkNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public ParkNotFoundException() {
	}

	public ParkNotFoundException(String message) {
		super(message);
	}
}
