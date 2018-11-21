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
public class ReturnCarIT extends BaseIT {
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

	/** Try to return a car but no space available. */
	@Test(expected = NoSpaceFault_Exception.class)
	public void returnCarNoSpaceTest() throws NoSpaceFault_Exception, BadInitFault_Exception {
		client.returnCar();
	}

	@Test
	public void returnCarOneTest() throws NoSpaceFault_Exception, BadInitFault_Exception, NoCarFault_Exception {
		client.rentCar();
		client.returnCar();

		ParkInfo view = client.getInfo();
		assertNotNull(view);

		assertEquals(CAPACITY, view.getAvailableCars());
	}

}
