package com.ecar.ws.it;

import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.ecar.ws.CoordinatesView;
import com.ecar.ws.ParkView;
import com.ecar.ws.cli.ECarClient;

/*
 * Base class of tests
 * Loads the properties in the file
 */
public class BaseIT {

	private static final String TEST_PROP_FILE = "/test.properties";
	protected static Properties testProps;

	protected static ECarClient client;
	protected static String parkBaseName;
	protected static final String VALID_USER = "sd.test@tecnico.ulisboa";

	@BeforeClass
	public static void oneTimeSetup() throws Exception {
		testProps = new Properties();
		try {
			testProps.load(BaseIT.class.getResourceAsStream(TEST_PROP_FILE));
			System.out.println("Loaded test properties:");
			System.out.println(testProps);
		} catch (IOException e) {
			final String msg = String.format("Could not load properties file {}", TEST_PROP_FILE);
			System.out.println(msg);
			throw e;
		}

		final String uddiEnabled = testProps.getProperty("uddi.enabled");
		final String verboseEnabled = testProps.getProperty("verbose.enabled");

		final String uddiURL = testProps.getProperty("uddi.url");
		final String wsName = testProps.getProperty("ws.name");
		final String wsURL = testProps.getProperty("ws.url");

		parkBaseName = testProps.getProperty("park.ws.name");

		if ("true".equalsIgnoreCase(uddiEnabled)) {
			client = new ECarClient(uddiURL, wsName);
		} else {
			client = new ECarClient(wsURL);
		}
		client.setVerbose("true".equalsIgnoreCase(verboseEnabled));
	}

	@AfterClass
	public static void cleanup() {
	}

	protected static ParkView newParkView(String id, int x, int y, int capacity, int bonus) {
		ParkView park = new ParkView();
		park.setId(id);

		CoordinatesView coords = new CoordinatesView();
		coords.setX(x);
		coords.setY(y);
		park.setCoords(coords);

		park.setCapacity(capacity);
		park.setReturnBonus(bonus);

		return park;
	}

}
