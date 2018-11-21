package com.ecar.park.ws;

import javax.jws.WebService;

import com.ecar.park.domain.Coordinates;
import com.ecar.park.domain.Park;
import com.ecar.park.domain.exception.BadInitException;
import com.ecar.park.domain.exception.NoCarAvailException;
import com.ecar.park.domain.exception.NoSlotAvailException;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.ecar.park.ws.ParkPortType",
            wsdlLocation = "ParkService.wsdl",
            name ="ParkWebService",
            portName = "ParkPort",
            targetNamespace="http://ws.park.ecar.com/",
            serviceName = "ParkService"
)
public class ParkPortImpl implements ParkPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private ParkEndpointManager endpointManager;

	/** Constructor receives a reference to the endpoint manager. */
	public ParkPortImpl(ParkEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}
	
	// Main operations -------------------------------------------------------
	
	/** Retrieve information about park. */
	@Override
	public ParkInfo getInfo() {
		// Access the domain root where the master data is stored.
		Park park = Park.getInstance();
		// Create a view (copy) to store the data in the response.
		// Acquire object lock to perform all gets together.
		synchronized(park) {
			return buildParkInfo(park);
		}
	}
	
	@Override
	public ParkStats getStats() {
		// Access the domain root where the master data is stored.
		Park park = Park.getInstance();
		// Create a view (copy) to store the data in the response.
		// Acquire object lock to perform all gets together.
		synchronized(park) {
			return buildParkStats(park);
		}
	}

	/** Return car to the park. */
	@Override
	public int returnCar() throws NoSpaceFault_Exception {
		Park park = Park.getInstance();
		int bonus = 0;
		try {
			bonus = park.returnCar();
		} catch(NoSlotAvailException e) {
			throwNoSlotFault("No slot available at this park!");
		}
		return bonus;
	}

	/** Take a car from the park. */
	@Override
	public void rentCar() throws NoCarFault_Exception {
		Park park = Park.getInstance();
		try {
			park.getCar();;
		} catch (NoCarAvailException e) {
			throwNoCarFault("No car available at this park!");
		}
	}


	// Test Control operations -----------------------------------------------

	/** Diagnostic operation to check if service is running. */
	@Override
	public String testPing(String inputMessage) {
		// If no input is received, return a default name.
		if (inputMessage == null || inputMessage.trim().length() == 0)
			inputMessage = "friend";

		// If the park does not have a name, return a default.
		String wsName = endpointManager.getWsName();
		if (wsName == null || wsName.trim().length() == 0)
			wsName = "Park";

		// Build a string with a message to return.
		StringBuilder builder = new StringBuilder();
		builder.append("Hello ").append(inputMessage);
		builder.append(" from ").append(wsName);
		return builder.toString();
	}

	/** Return all park variables to default values. */
	@Override
	public void testClear() {
		Park.getInstance().reset();
	}

	/** Set park variables with specific values. */
	@Override
	public void testInit(Xy xy, int capacity, int returnBonus) throws BadInitFault_Exception {
		try {
			Park.getInstance().init(xy.getX(), xy.getY(), capacity, returnBonus);
		} catch (BadInitException e) {
			throwBadInit("Invalid initialization values!");
		}
	}


	// View helpers ----------------------------------------------------------

	/** Helper to convert a domain object to a view. */
	private ParkInfo buildParkInfo(Park park) {
		ParkInfo info = new ParkInfo();
		info.setId(park.getId());
		info.setCoords(buildCoordinatesView(park.getCoordinates()));
		info.setCapacity(park.getMaxCapacity());
		info.setFreeSpaces(park.getFreeDocks());
		info.setAvailableCars(park.getAvailableCars());
		return info;
	}

	/** Helper to convert a domain object to a view. */
	private ParkStats buildParkStats(Park park) {
		ParkStats stats = new ParkStats();
		stats.setId(park.getId());
		stats.setCountRents(park.getTotalGets());
		stats.setCountReturns(park.getTotalReturns());
		return stats;
	}
	
	/** Helper to convert a domain object to a view. */
	private Xy buildCoordinatesView(Coordinates coordinates) {
		Xy view = new Xy();
		view.setX(coordinates.getX());
		view.setY(coordinates.getY());
		return view;
	}
	
	// Exception helpers -----------------------------------------------------

	/** Helper to throw an exception. */
	private void throwNoCarFault(final String message) throws NoCarFault_Exception {
		NoCarFault faultInfo = new NoCarFault();
		faultInfo.message = message;
		throw new NoCarFault_Exception(message, faultInfo);
	}
	
	/** Helper to throw an exception. */
	private void throwNoSlotFault(final String message) throws NoSpaceFault_Exception {
		NoSpaceFault faultInfo = new NoSpaceFault();
		faultInfo.message = message;
		throw new NoSpaceFault_Exception(message, faultInfo);
	}

	/** Helper to throw a new BadInit exception. */
	private void throwBadInit(final String message) throws BadInitFault_Exception {
		BadInitFault faultInfo = new BadInitFault();
		faultInfo.message = message;
		throw new BadInitFault_Exception(message, faultInfo);
	}

}
