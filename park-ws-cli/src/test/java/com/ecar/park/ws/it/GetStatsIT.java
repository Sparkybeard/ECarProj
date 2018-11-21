package com.ecar.park.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.ecar.park.ws.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test suite
 */
public class GetStatsIT extends BaseIT {
	private final static int X = 5;
	private final static int Y = 5;
	private final static int CAPACITY = 20;
	private final static int RETURN_PRIZE = 0;
		
	// static members
	
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
	public void setUp() throws BadInitFault_Exception {
		client.testClear();
		Xy coords = new Xy();
		coords.setX(X);
		coords.setY(Y);
		client.testInit(coords, CAPACITY, RETURN_PRIZE);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void getStatsValidTest() {
		ParkStats view = client.getStats();
		
		assertNotNull(view);
		assertEquals(0, view.getCountRents());
		assertEquals(0, view.getCountReturns());
	}

	/**
	 * Test to assert that the Park correctly stores the number of rentals.
	 */
	@Test
	public void getStatsRentCarTest() throws BadInitFault_Exception, NoCarFault_Exception, NoSpaceFault_Exception {
		client.rentCar();

		ParkStats view = client.getStats();
		assertNotNull(view);

		assertEquals(1, view.getCountRents());
		assertEquals(0, view.getCountReturns());
	}

	/**
	 * Test to assert that the Park correctly stores the number of cars
	 * returned.
	 */
	@Test
	public void getInfoReturnCarTest() throws BadInitFault_Exception, NoCarFault_Exception, NoSpaceFault_Exception {
		client.rentCar();
		client.returnCar();

		ParkStats view = client.getStats();
		assertNotNull(view);

		assertEquals(1, view.getCountRents());
		assertEquals(1, view.getCountReturns());
	}

}
