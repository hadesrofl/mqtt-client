package de.hadesrofl.mqtt_client;

import org.json.JSONObject;

import de.hadesrofl.database.Database;
import de.hadesrofl.json.JsonReader;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Starts the test methods
	 */
	public void testApp() {
		testDatabase();
		testClient();
	}

	/**
	 * Tests the client with a give config file with or without database
	 */
	public void testClient() {
		String cfdb = "resources/config_test_db.json";
		String cf = "resources/config_test.json";
		String a[] = { cf };
		App.main(a);
		a[0] = cfdb;
		App.main(a);
	}

	/**
	 * Test the database connection with gibbish connection data and from the
	 * config file
	 */
	public void testDatabase() {
		testDatabase("hello", "world", "need", "db", "connection");
		JSONObject database = JsonReader.readFile(
				"resources/config_test_db.json").getJSONObject("database");
		testDatabase(database.getString("dbHost"),
				database.getString("dbPort"), database.getString("dbName"),
				database.getString("dbUser"), database.getString("dbPass"));
	}

	/**
	 * Creates a Database object and connects and disconncets from it
	 * 
	 * @param dbHost
	 *            is the host of the db
	 * @param dbPort
	 *            is the port of the db
	 * @param dbName
	 *            is the name of the db
	 * @param dbUser
	 *            is the user of the db
	 * @param dbPass
	 *            is the password for the db
	 */
	public void testDatabase(String dbHost, String dbPort, String dbName,
			String dbUser, String dbPass) {
		Database db = new Database(dbHost, dbPort, dbName, dbUser, dbPass);
		db.connect();
		db.disconnect();
	}
}
