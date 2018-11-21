package com.ecar.park.ws.cli;

/** 
 * Client application. 
 * 
 * Looks for parks using UDDI and arguments provided
 */
public class ParkClientApp {

	public static void main(String[] args) throws Exception {
		// Check arguments.
		if (args.length == 0) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + ParkClientApp.class.getName() + " wsURL OR uddiURL wsName");
			return;
		}
		String uddiURL = null;
		String wsName = null;
		String wsURL = null;
		if (args.length == 1) {
			wsURL = args[0];
		} else if (args.length >= 2) {
			uddiURL = args[0];
			wsName = args[1];
		}

		// Create client.
		ParkClient client = null;

		if (wsURL != null) {
			System.out.printf("Creating client for server at %s%n", wsURL);
			client = new ParkClient(wsURL);
		} else if (uddiURL != null) {
			System.out.printf("Creating client using UDDI at %s for server with name %s%n", uddiURL, wsName);
			client = new ParkClient(uddiURL, wsName);
		}

		// The following remote invocation is just a basic example.
		// The actual tests are made using JUnit.

		System.out.println("Invoke ping()...");
		String result = client.testPing("client");
		System.out.print("Result: ");
		System.out.println(result);
	}

}
