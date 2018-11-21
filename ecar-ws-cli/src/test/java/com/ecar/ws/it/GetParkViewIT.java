package com.ecar.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ecar.ws.InvalidParkFault_Exception;
import com.ecar.ws.ParkView;

/*
 * This class should return info about the park
 */
public class GetParkViewIT extends BaseIT {
	private final static int X1 = 5;
	private final static int Y1 = 5;
	private final static int X2 = 5;
	private final static int Y2 = 5;
	private final static int X3 = 5;
	private final static int Y3 = 5;
	private final static int CAPACITY = 20;
	private final static int RETURN_PRIZE = 0;

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() throws Exception {
		client.testClear();
		client.testInitPark(newParkView(parkBaseName + "1", X1, Y1, CAPACITY, RETURN_PRIZE));
		client.testInitPark(newParkView(parkBaseName + "2", X2, Y2, CAPACITY, RETURN_PRIZE));
		client.testInitPark(newParkView(parkBaseName + "3", X3, Y3, CAPACITY, RETURN_PRIZE));
	}

	@AfterClass
	public static void oneTimeTearDown() {
	}

	// members

	// initialization and clean-up for each test
	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	// tests

	@Test
	public void getInfoParkSingleValidTest() throws Exception {
		ParkView view = client.getParkView(parkBaseName + "1");

		assertNotNull(view);
		assertEquals(CAPACITY, view.getAvailableCars());
		assertEquals(CAPACITY, view.getCapacity());
		assertEquals(X1, view.getCoords().getX().intValue());
		assertEquals(Y1, view.getCoords().getY().intValue());
		assertEquals(0, view.getFreeSpaces());
		assertEquals(0, view.getCountRents());
		assertEquals(0, view.getCountReturns());
		assertEquals(parkBaseName + "1", view.getId());
	}

	@Test
	public void getInfoParkAllValidTest() throws Exception {
		ParkView view1 = client.getParkView(parkBaseName + "1");
		ParkView view2 = client.getParkView(parkBaseName + "2");
		ParkView view3 = client.getParkView(parkBaseName + "3");

		assertEquals(X1, view1.getCoords().getX().intValue());
		assertEquals(Y1, view1.getCoords().getY().intValue());
		assertEquals(X2, view2.getCoords().getX().intValue());
		assertEquals(Y2, view2.getCoords().getY().intValue());
		assertEquals(X3, view3.getCoords().getX().intValue());
		assertEquals(Y3, view3.getCoords().getY().intValue());

	}

	@Test(expected = InvalidParkFault_Exception.class)
	public void getInfoParkUnknownTest() throws Exception {
		client.getParkView("Unknown");
	}

	@Test(expected = InvalidParkFault_Exception.class)
	public void getInfoParkNullTest() throws Exception {
		client.getParkView(null);
	}

}
