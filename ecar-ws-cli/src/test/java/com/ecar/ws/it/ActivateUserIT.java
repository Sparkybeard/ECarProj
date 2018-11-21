package com.ecar.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ecar.ws.EmailAlreadyExistsFault_Exception;
import com.ecar.ws.InvalidEmailFault_Exception;

/*
 * Class tests if the user creation has succeeded or not
 */
public class ActivateUserIT extends BaseIT {
	private static final int USER_POINTS = 10;

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
	}

	@After
	public void tearDown() {
	}

	@Test
	public void createUserValidTest() throws Exception {
		client.activateUser(VALID_USER);
		assertEquals(USER_POINTS, client.getCredit(VALID_USER));
	}

	@Test
	public void createUserValidTest2() throws Exception {
		String email = new String("sd.teste@tecnico");
		client.activateUser(email);
		assertEquals(USER_POINTS, client.getCredit(email));
	}

	@Test
	public void createUserValidTest3() throws Exception {
		String email = new String("sd@tecnico");
		client.activateUser(email);
		assertEquals(USER_POINTS, client.getCredit(email));
	}

	/*
	 * Tries to create to users with the same name, which should throw an exception
	 */
	@Test(expected = EmailAlreadyExistsFault_Exception.class)
	public void createUserDuplicateTest() throws Exception {
		client.activateUser(VALID_USER);
		client.activateUser(VALID_USER);
	}

	/*
	 * Tries to create a user with an invalid email Should throw
	 * InvalidEmail_Exception
	 */
	@Test(expected = InvalidEmailFault_Exception.class)
	public void createUserInvalidEmailTest1() throws Exception {
		String email = new String("@tecnico.ulisboa");
		client.activateUser(email);
	}

	/*
	 * Tries to create a user with an invalid email Should throw
	 * InvalidEmail_Exception
	 */
	@Test(expected = InvalidEmailFault_Exception.class)
	public void createUserInvalidEmailTest2() throws Exception {
		String email = new String("teste");
		client.activateUser(email);
	}

	/*
	 * Tries to create a user with an invalid email Should throw
	 * InvalidEmail_Exception
	 */
	@Test(expected = InvalidEmailFault_Exception.class)
	public void createUserInvalidEmailTest3() throws Exception {
		String email = new String("teste@tecnico.");
		client.activateUser(email);
	}

	/*
	 * Tries to create a user with an invalid email Should throw
	 * InvalidEmail_Exception
	 */
	@Test(expected = InvalidEmailFault_Exception.class)
	public void createUserInvalidEmailTest4() throws Exception {
		String email = new String("sd.@tecnico");
		client.activateUser(email);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void createUserInvalidEmailTest5() throws Exception {
		client.activateUser(null);
	}

}
