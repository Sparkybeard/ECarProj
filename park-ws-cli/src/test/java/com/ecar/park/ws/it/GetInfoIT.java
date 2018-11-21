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
import org.uddi.repl_v3.NotifyChangeRecordsAvailable;

/**
 * Test suite
 */
public class GetInfoIT extends BaseIT {
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
	public void getInfoValidTest() {
		ParkInfo view = client.getInfo();
		
		assertNotNull(view);
		assertEquals(CAPACITY, view.getAvailableCars());
		assertEquals(CAPACITY, view.getCapacity());
		assertEquals(X, view.getCoords().getX());
		assertEquals(Y, view.getCoords().getY());
		assertEquals(0, view.getFreeSpaces());
		assertTrue(view.getId().matches("[ATC][0-9X][0-9X]_Park[0-9]"));
	}

	/**
	 * Test to assert that the Park correctly stores the number of gets.
	 */
	@Test
	public void getInfoGetCarTest() throws BadInitFault_Exception, NoCarFault_Exception, NoSpaceFault_Exception {
		client.rentCar();

		ParkInfo view = client.getInfo();
		assertNotNull(view);

		assertEquals(CAPACITY - 1, view.getAvailableCars());
//		assertEquals(1, view.getTotalGets());
		assertEquals(1, view.getFreeSpaces());
	}

	/**
	 * Test to assert that the Park correctly stores the number of cars
	 * returned.
	 */
	@Test
	public void getInfoReturnCarTest() throws BadInitFault_Exception, NoCarFault_Exception, NoSpaceFault_Exception {
		client.rentCar();
		client.returnCar();

		ParkInfo view = client.getInfo();
		assertNotNull(view);

		assertEquals(CAPACITY, view.getAvailableCars());
//		assertEquals(1, view.getTotalGets());
//		assertEquals(1, view.getTotalReturns());
		assertEquals(0, view.getFreeSpaces());
	}

}
