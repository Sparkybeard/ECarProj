package com.ecar.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ecar.ws.InsufficientCreditFault_Exception;
import com.ecar.ws.InvalidParkFault_Exception;
import com.ecar.ws.NoCarAvailableFault_Exception;
import com.ecar.ws.UserAlreadyHasCarFault_Exception;

/*
 * Class that tests renting a car
 */
public class RentCarIT extends BaseIT {
	private static final int USER_POINTS = 10;
	private static final String PARK_1 = parkBaseName + "1";

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() {
	}

	@AfterClass
	public static void oneTimeTearDown() {
	}

	// members

	// initialization and clean-up for each test
	@Before
	public void setUp() throws Exception {
		client.testClear();
		client.testInit(USER_POINTS);
		client.testInitPark(newParkView(PARK_1, /* x */5, /* y */5, /* capacity */20, /* reward */0));
		client.activateUser(VALID_USER);
	}

	@After
	public void tearDown() {
	}

	// tests

	/*
	 * Valid user rents car Should not raise any Exception
	 */
	@Test
	public void rentcarValidTest() throws Exception {
		client.rentCar(PARK_1, VALID_USER);
		assertEquals(USER_POINTS - 1, client.getCredit(VALID_USER));
	}

	/*
	 * Class that exercises the fact that a User cannot rent a new car if he already
	 * has one
	 */
	@Test(expected = UserAlreadyHasCarFault_Exception.class)
	public void rentcarAlreadyHascarTest() throws Exception {
		client.rentCar(PARK_1, VALID_USER);
		client.rentCar(PARK_1, VALID_USER);

	}

	/*
	 * User tries to rent a car from an invalid park
	 */
	@Test(expected = InvalidParkFault_Exception.class)
	public void rentCarInvalidparkTest() throws Exception {
		client.rentCar("Invalid park", VALID_USER);
	}

	/*
	 * User tries to rent a car from an invalid park
	 */
	@Test(expected = InvalidParkFault_Exception.class)
	public void rentCarNullparkTest() throws Exception {
		client.rentCar(null, VALID_USER);
	}

	/*
	 * User tries to rent car but there is no car available in the park Expected to
	 * throw exception
	 */
	@Test(expected = NoCarAvailableFault_Exception.class)
	public void rentCarNoCarAvailTest() throws Exception {
		client.testInitPark(newParkView(parkBaseName + "1", /* x */5, /* y */5, /* capacity */0, /* reward */10));
		client.rentCar(PARK_1, VALID_USER);
	}

	/*
	 * User tries to rent a car but has no credit
	 */
	@Test(expected = InsufficientCreditFault_Exception.class)
	public void rentCarNoCreditTest() throws Exception {
		client.testClear();
		client.testInit(0);
		client.activateUser(VALID_USER);
		client.rentCar(PARK_1, VALID_USER);

	}

}
