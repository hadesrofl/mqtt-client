/**
 * Copyright 2016   Rene Kremer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.hadesrofl.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 *
 *
 * <b>Project:</b> mqtt-client
 * <p>
 * <b>Packages:</b> de.hadesrofl.database
 * </p>
 * <p>
 * <b>File:</b> Database.java
 * </p>
 * <p>
 * <b>last update:</b> 30.10.2016
 * </p>
 * <p>
 * <b>Time:</b> 08:56:39
 * </p>
 * <b>Description:</b>
 * <p>
 * Small Database class to represent the db in this app
 * </p>
 *
 * @author Rene Kremer
 *         <p>
 *         Copyright (c) 2016 by Rene Kremer
 *         </p>
 *         <p>
 *         Licensed under the Apache License, Version 2.0
 *         </p>
 * @version 0.2
 */
public class Database {
	/**
	 * address of the host of the db
	 */
	private String dbHost;
	/**
	 * port of the db
	 */
	private String dbPort;
	/**
	 * db name
	 */
	private String dbName;
	/**
	 * db user name
	 */
	private String dbUser;
	/**
	 * db user password
	 */
	private String dbPass;
	/**
	 * db connection object
	 */
	private Connection con = null;

	/**
	 * Constructor
	 *
	 * @param dbHost
	 *            is the address to the db
	 * @param dbPort
	 *            is the port of the db
	 * @param dbName
	 *            is the name of the db
	 * @param dbUser
	 *            is the user of the db
	 * @param dbPass
	 *            is the password of the user
	 */
	public Database(String dbHost, String dbPort, String dbName, String dbUser, String dbPass) {
		this.dbHost = dbHost;
		this.dbPort = dbPort;
		this.dbName = dbName;
		this.dbUser = dbUser;
		this.dbPass = dbPass;
	}

	/**
	 * Connects to the db
	 *
	 * @return true if successful, otherwise false
	 */
	public boolean connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?" + "user="
					+ dbUser + "&" + "password=" + dbPass);
			return true;
		} catch (ClassNotFoundException e) {
			System.out.println("DB driver not found!");
			return false;
		} catch (SQLException e) {
			System.out.println("Connection not possible");
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			return false;
		}
	}

	/**
	 * Closes the db connection
	 *
	 * @return true on success, otherwise false
	 */
	public boolean disconnect() {
		try {
			con.close();
			return true;
		} catch (SQLException e) {
			System.out.println();
			System.err.println(e.getMessage());
			return false;
		}
	}

	/**
	 * Creates a prepared statement out of a given query
	 *
	 * @param query
	 *            is the query for the statement
	 * @return a prepared statement object or null if creating a statement
	 *         failed
	 */
	public PreparedStatement getStatement(String query) {
		try {
			return con.prepareStatement(query);
		} catch (SQLException e) {
			System.out.println("Failed creating a prepared statement!");
			System.err.println(e.getMessage());
		}
		return null;
	}

	/**
	 * Executes a statement and returns the updated rows
	 *
	 * @param preparedStatement
	 *            is the statement to execute
	 * @return the number of updated rows or -1 on an error
	 */
	public int executeStatement(PreparedStatement preparedStatement) {
		if (preparedStatement != null) {
			try {
				return preparedStatement.executeUpdate();
			} catch (SQLException e) {
				System.out.println("Couldn't execute the query!");
				System.err.println(e.getMessage());
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					System.out.println("Error closing the statement!");
					System.err.println(e.getMessage());
				}
			}
		}
		return -1;
	}
}
