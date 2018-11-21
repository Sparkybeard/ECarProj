package com.ecar.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ecar.ws.CarNotRentedFault_Exception;
import com.ecar.ws.InvalidUserFault_Exception;
import com.ecar.ws.ParkFullFault_Exception;

/**
 * Class that exercises the return of a car
 *
 */
public class ReturnCarIT extends BaseIT {

	private static final int USER_POINTS = 10;
	private static final int PARK_REWARD = 10;
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
		client.testInitPark(newParkView(PARK_1, /* x */5, /* y */5, /* capacity */20, /* reward */PARK_REWARD));
		client.activateUser(VALID_USER);
	}

	@After
	public void tearDown() {
	}

	// tests

	@Test
	public void returnCarOkTest() throws Exception {
		client.rentCar(PARK_1, VALID_USER);
		client.returnCar(PARK_1, VALID_USER);
		int credit = client.getCredit(VALID_USER);
		assertEquals(USER_POINTS - 1 + PARK_REWARD, credit);
	}

	@Test(expected = CarNotRentedFault_Exception.class)
	public void returnCarNocarRentedTest() throws Exception {
		final String USER_2 = "sd.teste2@tecnico.ulisboa";
		client.activateUser(USER_2);

		/* Rent with another user first to avoid Fullpark Exception */
		client.rentCar(PARK_1, USER_2);
		client.returnCar(PARK_1, VALID_USER);
	}

	@Test(expected = InvalidUserFault_Exception.class)
	public void returnCarUserNotExistsTest() throws Exception {
		final String USER_2 = "sd.teste2@tecnico.ulisboa";
		final String USER_3 = "sd.teste3@tecnico.ulisboa";

		client.activateUser(USER_2);

		/* Rent with another user first to avoid exception */
		client.rentCar(PARK_1, USER_2);

		client.returnCar(PARK_1, USER_3);
	}

	@Test(expected = ParkFullFault_Exception.class)
	public void returncarNoSlotAvailTest() throws Exception {
		final String park_2 = parkBaseName + "2";
		client.testInitPark(newParkView(park_2, /* x */5, /* y */5, /* capacity */20, /* reward */PARK_REWARD));

		client.rentCar(PARK_1, VALID_USER);
		client.returnCar(park_2, VALID_USER);

	}

}
