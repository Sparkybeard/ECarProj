package com.ecar.domain;

import java.util.ArrayList;
import java.util.Collection;

import com.ecar.domain.exception.BadInitException;
import com.ecar.domain.exception.InsufficientCreditsException;
import com.ecar.domain.exception.InvalidEmailException;
import com.ecar.domain.exception.ParkNotFoundException;
import com.ecar.domain.exception.UserAlreadyExistsException;
import com.ecar.domain.exception.UserAlreadyHasCarException;
import com.ecar.domain.exception.UserHasNoCarException;
import com.ecar.domain.exception.UserNotFoundException;
import com.ecar.park.ws.BadInitFault_Exception;
import com.ecar.park.ws.NoCarFault_Exception;
import com.ecar.park.ws.NoSpaceFault_Exception;
import com.ecar.park.ws.ParkInfo;
import com.ecar.park.ws.Xy;
import com.ecar.park.ws.cli.ParkClient;
import com.ecar.park.ws.cli.ParkClientException;
import com.ecar.ws.NoCarAvailableFault_Exception;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;

/**
 * Manager class
 *
 */
public class ECarManager {
	/**
	 * UDDI server URL
	 */
	private String uddiURL = null;

	/**
	 * Park name
	 */
	private String parkName = null;

	// Singleton -------------------------------------------------------------

	private ECarManager() {
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final ECarManager INSTANCE = new ECarManager();
	}

	public static synchronized ECarManager getInstance() {
		return SingletonHolder.INSTANCE;
	}

	// Logic --------------------------------------------------------------

	public User createUser(String email) throws UserAlreadyExistsException, InvalidEmailException {
		return UsersManager.getInstance().RegisterNewUser(email);
	}

	public User getUser(String email) throws UserNotFoundException {
		return UsersManager.getInstance().getUser(email);
	}

	public void rentCar(String parkId, String email) throws UserNotFoundException, InsufficientCreditsException,
			UserAlreadyHasCarException, ParkNotFoundException, NoCarAvailableFault_Exception, NoCarFault_Exception {
		User user = getUser(email);
		synchronized (user) {
			// validate user can rent
			user.validateCanRent();

			// validate park can rent
			ParkClient parkCli = getPark(parkId);
			parkCli.rentCar();

			// apply rent action to user
			user.effectiveRent();
		}
	}

	public void returnCar(String parkId, String email)
			throws UserNotFoundException, NoSpaceFault_Exception, UserHasNoCarException, ParkNotFoundException {
		User user = getUser(email);
		synchronized (user) {
			// validate user can rent
			user.validateCanReturn();

			// validate park can rent
			ParkClient parkCli = getPark(parkId);
			int prize = parkCli.returnCar();

			// apply rent action to user
			user.effectiveReturn(prize);
		}
	}

	public ParkClient getPark(String parkId) throws ParkNotFoundException {

		Collection<String> parks = this.getParks();
		String uddiUrl = ECarManager.getInstance().getUddiURL();

		for (String s : parks) {
			try {
				ParkClient sc = new ParkClient(uddiUrl, s);
				ParkInfo pi = sc.getInfo();
				String idToCompare = pi.getId();
				if (idToCompare.equals(parkId)) {
					return sc;
				}
			} catch (ParkClientException e) {
				continue;
			}
		}

		throw new ParkNotFoundException();
	}

	// UDDI ------------------------------------------------------------------

	public void initUddiURL(String uddiURL) {
		setUddiURL(uddiURL);
	}

	public void initParkTemplateName(String name) {
		setParkName(name);
	}

	public String getUddiURL() {
		return uddiURL;
	}

	private void setUddiURL(String url) {
		uddiURL = url;
	}

	private void setParkName(String name) {
		parkName = name;
	}

	public String getParkTemplateName() {
		return parkName;
	}

	/**
	 * Get list of parks for a given query
	 * 
	 * @return List of parks
	 */
	public Collection<String> getParks() {
		Collection<UDDIRecord> records = null;
		Collection<String> parks = new ArrayList<String>();
		try {
			UDDINaming uddi = new UDDINaming(uddiURL);
			// join park name with wild-card character to match all parks
			records = uddi.listRecords(parkName + "%");
			for (UDDIRecord u : records)
				parks.add(u.getOrgName());
		} catch (UDDINamingException e) {
		}
		return parks;
	}

	public void reset() {
		UsersManager.getInstance().reset();
	}

	public void init(int userInitialPoints) throws BadInitException {
		if (userInitialPoints < 0) {
			throw new BadInitException();
		}
		UsersManager.getInstance().init(userInitialPoints);
	}

	/**
	 * 
	 * Inits a park with a determined ID, coordinates, capacity and returnPrize
	 * 
	 * @param parkId
	 * @param x
	 * @param y
	 * @param capacity
	 * @param returnPrize
	 * @throws BadInitException
	 * @throws ParkNotFoundException
	 */
	public void testInitPark(String parkId, int x, int y, int capacity, int returnPrize)
			throws BadInitException, ParkNotFoundException {
		// validate park can rent
		ParkClient parkCli;
		try {
			parkCli = getPark(parkId);
			Xy xy = new Xy();
			xy.setX(x);
			xy.setY(y);
			parkCli.testInit(xy, capacity, returnPrize);
		} catch (BadInitFault_Exception e) {
			throw new BadInitException(e.getMessage());
		}

	}
}
