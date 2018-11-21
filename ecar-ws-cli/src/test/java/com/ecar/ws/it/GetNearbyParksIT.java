package com.ecar.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ecar.ws.CoordinatesView;
import com.ecar.ws.ParkView;

// List park is a class that tests the get parks operation that should deliver parks in an order from the nearest one to the farthest one
public class GetNearbyParksIT extends BaseIT {
	private final static int X1 = 1;
	private final static int Y1 = 1;
	private final static int X2 = 2;
	private final static int Y2 = 2;
	private final static int X3 = 3;
	private final static int Y3 = 3;
	private final static int CAPACITY = 20;
	private final static int RETURN_PRIZE = 0;
	private static CoordinatesView testCoords;

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() throws Exception {
		client.testClear();
		client.testInitPark(newParkView(parkBaseName + "1", X1, Y1, CAPACITY, RETURN_PRIZE));
		client.testInitPark(newParkView(parkBaseName + "2", X2, Y2, CAPACITY, RETURN_PRIZE));
		client.testInitPark(newParkView(parkBaseName + "3", X3, Y3, CAPACITY, RETURN_PRIZE));
		testCoords = new CoordinatesView();
		testCoords.setX(0);
		testCoords.setY(0);
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

	@Test
	public void listParksSingleTest() {
		List<ParkView> result = client.getNearbyParks(/* number of parks */ 1, testCoords);
		ParkView view = result.get(0);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(parkBaseName + "1", view.getId());
	}

	@Test
	public void listParksAllTest() {
		List<ParkView> result = client.getNearbyParks(/* number of parks */ 3, testCoords);
		ParkView view1 = result.get(0);
		ParkView view2 = result.get(1);
		ParkView view3 = result.get(2);
		assertNotNull(result);
		assertEquals(3, result.size());
		assertEquals(parkBaseName + "1", view1.getId());
		assertEquals(parkBaseName + "2", view2.getId());
		assertEquals(parkBaseName + "3", view3.getId());
	}

	@Test
	public void listParksNullTest() {
		List<ParkView> result = client.getNearbyParks(/* number of parks */ 1, null);
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@Test
	public void listParksZeroTest() {
		List<ParkView> result = client.getNearbyParks(/* number of parks */ 0, testCoords);
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@Test
	public void listParksFourTest() {
		List<ParkView> result = client.getNearbyParks(/* number of parks */ 4, testCoords);
		assertNotNull(result);
		assertEquals(3, result.size());
	}

}
