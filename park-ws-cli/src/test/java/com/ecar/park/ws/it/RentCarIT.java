package com.ecar.park.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.ecar.park.ws.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test suite
 */
public class RentCarIT extends BaseIT {
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

	// main tests
	// assertEquals(expected, actual);

	/** Try to rent a car , get one verify, one rented (less). */
	@Test
	public void rentOneTest() throws NoCarFault_Exception, BadInitFault_Exception {
		client.rentCar();

		ParkInfo view = client.getInfo();
		assertNotNull(view);
		assertEquals(CAPACITY - 1, view.getAvailableCars());
	}
	
	@Test
	public void rentCarAllTest() throws NoCarFault_Exception, BadInitFault_Exception {
		for(int i = 0; i < CAPACITY; i++)
			client.rentCar();

		ParkInfo view = client.getInfo();
		assertNotNull(view);
		assertEquals(0, view.getAvailableCars());
	}
	
	/** Try to get a car but no cars available. */
	@Test(expected = NoCarFault_Exception.class)
	public void rentCarNoCarTest() throws NoCarFault_Exception, BadInitFault_Exception {
		for(int i = 0; i <= CAPACITY; i++)
			client.rentCar();
	}

	

}
