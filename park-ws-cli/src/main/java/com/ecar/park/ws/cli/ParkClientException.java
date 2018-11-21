package com.ecar.park.ws.cli;

/** 
 * 
 * Exception to be thrown when something is wrong with the client. 
 * 
 */
public class ParkClientException extends Exception {

	private static final long serialVersionUID = 1L;

	public ParkClientException() {
		super();
	}

	public ParkClientException(String message) {
		super(message);
	}

	public ParkClientException(Throwable cause) {
		super(cause);
	}

	public ParkClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
