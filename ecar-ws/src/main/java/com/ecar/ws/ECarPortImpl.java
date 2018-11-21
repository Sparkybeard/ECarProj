package com.ecar.ws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.jws.WebService;

import com.ecar.domain.ECarManager;
import com.ecar.domain.ParkComparator;
import com.ecar.domain.User;
import com.ecar.domain.exception.BadInitException;
import com.ecar.domain.exception.InsufficientCreditsException;
import com.ecar.domain.exception.InvalidEmailException;
import com.ecar.domain.exception.ParkNotFoundException;
import com.ecar.domain.exception.UserAlreadyExistsException;
import com.ecar.domain.exception.UserAlreadyHasCarException;
import com.ecar.domain.exception.UserHasNoCarException;
import com.ecar.domain.exception.UserNotFoundException;
import com.ecar.park.ws.NoCarFault_Exception;
import com.ecar.park.ws.NoSpaceFault_Exception;
import com.ecar.park.ws.ParkInfo;
import com.ecar.park.ws.ParkStats;
import com.ecar.park.ws.cli.ParkClient;
import com.ecar.park.ws.cli.ParkClientException;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

@WebService(
		endpointInterface = "com.ecar.ws.ECarPortType",
        wsdlLocation = "ECarService.wsdl",
        name ="ECarWebService",
        portName = "ECarPort",
        targetNamespace="http://ws.ecar.com/",
        serviceName = "ECarService"
)
public class ECarPortImpl implements ECarPortType {
	
	// end point manager
	private ECarEndpointManager endpointManager;

	public ECarPortImpl(ECarEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
		
	Calendar calendar = Calendar.getInstance();
	
		
	}

	@Override
	public UserView activateUser(String email) throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
		try {
			User user = ECarManager.getInstance().createUser(email);
			
			//Create and populate userView
			UserView userView = new UserView();
			userView.setUserEmail(user.getEmail());
			userView.setCredit(user.getCredit());
			userView.setHasCar(user.getHasCar());
			return userView;
		} catch (UserAlreadyExistsException e) {
			throwEmailAlreadyExists("Email already exists: " + email);
		} catch (InvalidEmailException e) {
			throwInvalidEmail("Invalid email: " + email);
		}
		return null;
	}

	@Override
	public ParkView getParkView(String parkId) throws InvalidParkFault_Exception {
		if(parkId == null || parkId.trim().isEmpty())
			throwInvalidPark("Park IDs can not be empty!");
		
		ParkClient parkCli;
		try {
			parkCli = ECarManager.getInstance().getPark(parkId);
			ParkInfo parkInfo = parkCli.getInfo();
			ParkStats parkStats = parkCli.getStats(); 
			return newParkView(parkInfo, parkStats);
		} catch (ParkNotFoundException e) {
			throwInvalidPark("No Park found with ID: " + parkId);
			return null;
		}
		
	}

	@Override
	public List<ParkView> getNearbyParks(Integer maxNrParks, CoordinatesView userCoords) {

		List<ParkView> parkViews = new ArrayList<ParkView>();
		Collection<String> parks = ECarManager.getInstance().getParks();
		String uddiUrl = ECarManager.getInstance().getUddiURL();
		ParkClient sc = null;
		ParkInfo parkInfo = null;
		ParkStats parkStats = null;
		
		if(maxNrParks <= 0 || userCoords == null)
			return parkViews;
		
		for (String s : parks) {
			try {
				sc = new ParkClient(uddiUrl, s);
				parkInfo = sc.getInfo();
				parkStats = sc.getStats();
				parkViews.add(newParkView(parkInfo, parkStats));
			} catch(ParkClientException e) {
				continue;
			}
		}
		Collections.sort(parkViews, new ParkComparator(userCoords));
		
		if(maxNrParks > parkViews.size())
			return parkViews;
		else
			return parkViews.subList(0, maxNrParks);
	}

	@Override
	public CarView rentCar(String parkId, String userEmail)
			throws InsufficientCreditFault_Exception, InvalidParkFault_Exception, InvalidUserFault_Exception,
			NoCarAvailableFault_Exception, UserAlreadyHasCarFault_Exception {
		
		try {
			ECarManager.getInstance().rentCar(parkId, userEmail);
			// let the operation return null as there is no car view to return

		} catch (UserNotFoundException e) {
			throwInvalidUser("User not found: " + userEmail);
		} catch (InsufficientCreditsException e) {
			throwInsufficientCredit("User has insufficient credits: " + userEmail);
		} catch (UserAlreadyHasCarException e) {
			throwUserAlreadyHasCar("User already has ecar: " + userEmail);
		} catch (ParkNotFoundException e) {
			throwInvalidPark("Park not found: " + parkId);
		} catch (NoCarFault_Exception e) {
			throwNoCarAvailable("Park has no ECar available: " + parkId);
		}
		return null;
	}


	@Override
	public void returnCar(String parkId, String userEmail) throws CarNotRentedFault_Exception,
			InvalidParkFault_Exception, InvalidUserFault_Exception, ParkFullFault_Exception {
		try {
			ECarManager.getInstance().returnCar(parkId, userEmail);
		} catch (UserNotFoundException e) {
			throwInvalidUser("User not found: " + userEmail);
		} catch (UserHasNoCarException e) {
			throwCarNotRented("User has NO ecar: " + userEmail);
		} catch (ParkNotFoundException e) {
			throwInvalidPark("Park not found: " + parkId);
		} catch (NoSpaceFault_Exception e) {
			throwParkFull("Park has NO docks available: " + parkId);
		}
	}

	@Override
	public int getCredit(String email) throws InvalidUserFault_Exception {
		try {
			User user = ECarManager.getInstance().getUser(email);	
			return user.getCredit();
		} catch (UserNotFoundException e) {
			throwInvalidUser("User not found: " + email);
		}
		return 0;
	}
	
	// Auxiliary operations --------------------------------------------------
	
	@Override
	public String testPing(String inputMessage) {
		final String EOL = String.format("%n");
		StringBuilder sb = new StringBuilder();

		sb.append("Hello ");
		if (inputMessage == null || inputMessage.length()==0)
			inputMessage = "friend";
		sb.append(inputMessage);
		sb.append(" from ");
		sb.append(endpointManager.getWsName());
		sb.append("!");
		sb.append(EOL);
		
		Collection<String> parkUrls = null;
		try {
			UDDINaming uddiNaming = endpointManager.getUddiNaming();
			parkUrls = uddiNaming.list(ECarManager.getInstance().getParkTemplateName() + "%");
			sb.append("Found ");
			sb.append(parkUrls.size());
			sb.append(" parks on UDDI.");
			sb.append(EOL);
		} catch(UDDINamingException e) {
			sb.append("Failed to contact the UDDI server:");
			sb.append(EOL);
			sb.append(e.getMessage());
			sb.append(" (");
			sb.append(e.getClass().getName());
			sb.append(")");
			sb.append(EOL);
			return sb.toString();
		}

		for(String parkUrl : parkUrls) {
			sb.append("Ping result for park at ");
			sb.append(parkUrl);
			sb.append(":");
			sb.append(EOL);
			try {
				ParkClient client = new ParkClient(parkUrl);
				String supplierPingResult = client.testPing(endpointManager.getWsName());
				sb.append(supplierPingResult);
			} catch(Exception e) {
				sb.append(e.getMessage());
				sb.append(" (");
				sb.append(e.getClass().getName());
				sb.append(")");
			}
			sb.append(EOL);
		}
		
		return sb.toString();
	}

	@Override
	public void testClear() {
		//Reset ECar
		ECarManager.getInstance().reset();

		//Reset All Parks
		Collection<String> parks = ECarManager.getInstance().getParks();
		String uddiUrl = ECarManager.getInstance().getUddiURL();
		ParkClient sc = null;

		for (String s : parks) {
			try {
				sc = new ParkClient(uddiUrl, s);
				sc.testClear();
			} catch(ParkClientException e) {
				continue;
			}
		}
	}

	@Override
	public void testInitPark(ParkView pv) throws InitParkFault_Exception {
		try {
			ECarManager.getInstance().testInitPark(pv.getId(),pv.getCoords().getX(),pv.getCoords().getY(),pv.getCapacity(),pv.getReturnBonus());
		} catch (BadInitException e) {
			throwInitParkFault("Bad init values");
		} catch (ParkNotFoundException e) {
			throwInitParkFault("No Park found with ID: " + pv.getId());
		}
	}

	@Override
	public void testInit(int userInitialPoints) throws InitFault_Exception {
		try {
			ECarManager.getInstance().init(userInitialPoints);
		} catch (BadInitException e) {
			throwInitFault("Bad init values: " + userInitialPoints);
		}
	}

	@Override
	public void imAlive() {
		//

	}


	// View helpers ----------------------------------------------------------
	
	private ParkView newParkView(ParkInfo pi, ParkStats ps) {
		ParkView retSv = new ParkView();
		CoordinatesView coordinates = new CoordinatesView();
		coordinates.setX(pi.getCoords().getX());
		coordinates.setY(pi.getCoords().getY());
		
		retSv.setCapacity(pi.getCapacity());
		retSv.setCoords(coordinates);
		retSv.setAvailableCars(pi.getAvailableCars());
		retSv.setFreeSpaces(pi.getFreeSpaces());
		retSv.setId(pi.getId());

		retSv.setCountRents(ps.getCountRents());
		retSv.setCountReturns(ps.getCountReturns());
		return retSv;
	}
	
	// Exception helpers -----------------------------------------------------
	
	private void throwInvalidEmail(final String message) throws InvalidEmailFault_Exception {
		InvalidEmailFault faultInfo = new InvalidEmailFault();
		faultInfo.setMessage(message);
		throw new InvalidEmailFault_Exception(message, faultInfo);
	}
	
	private void throwEmailAlreadyExists(final String message) throws EmailAlreadyExistsFault_Exception {
		EmailAlreadyExistsFault faultInfo = new EmailAlreadyExistsFault();
		faultInfo.setMessage(message);
		throw new EmailAlreadyExistsFault_Exception(message, faultInfo);
	}
	
	private void throwInvalidPark(final String message) throws InvalidParkFault_Exception {
		InvalidParkFault faultInfo = new InvalidParkFault();
		faultInfo.setMessage(message);
		throw new InvalidParkFault_Exception(message, faultInfo);
	}
	
	private void throwInvalidUser(final String message) throws InvalidUserFault_Exception {
		InvalidUserFault faultInfo = new InvalidUserFault();
		faultInfo.setMessage(message);
		throw new InvalidUserFault_Exception(message, faultInfo);
	}
	
	private void throwInsufficientCredit(final String message) throws InsufficientCreditFault_Exception {
		InsufficientCreditFault faultInfo = new InsufficientCreditFault();
		faultInfo.setMessage(message);
		throw new InsufficientCreditFault_Exception(message, faultInfo);
	}
	
	private void throwUserAlreadyHasCar(final String message) throws UserAlreadyHasCarFault_Exception {
		UserAlreadyHasCarFault faultInfo = new UserAlreadyHasCarFault();
		faultInfo.setMessage(message);
		throw new UserAlreadyHasCarFault_Exception(message, faultInfo);
	}
	
	private void throwNoCarAvailable(final String message) throws NoCarAvailableFault_Exception {
		NoCarAvailableFault faultInfo = new NoCarAvailableFault();
		faultInfo.setMessage(message);
		throw new NoCarAvailableFault_Exception(message, faultInfo);
	}
	
	private void throwCarNotRented(final String message) throws CarNotRentedFault_Exception {
		CarNotRentedFault faultInfo = new CarNotRentedFault();
		faultInfo.setMessage(message);
		throw new CarNotRentedFault_Exception(message, faultInfo);
	}
	
	private void throwParkFull(final String message) throws ParkFullFault_Exception {
		ParkFullFault faultInfo = new ParkFullFault();
		faultInfo.setMessage(message);
		throw new ParkFullFault_Exception(message, faultInfo);
	}

	private void throwInitFault(final String message) throws InitFault_Exception {
		InitFault faultInfo = new InitFault();
		faultInfo.setMessage(message);
		throw new InitFault_Exception(message, faultInfo);
	}

	private void throwInitParkFault(final String message) throws InitParkFault_Exception {
		InitParkFault faultInfo = new InitParkFault();
		faultInfo.setMessage(message);
		throw new InitParkFault_Exception(message, faultInfo);
	}


}
