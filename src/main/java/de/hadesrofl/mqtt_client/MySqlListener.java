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

package de.hadesrofl.mqtt_client;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import de.hadesrofl.database.Database;

/**
 *
 *
 * <b>Project:</b> mqtt-client
 * <p>
 * <b>Packages:</b> de.hadesrofl.mqtt_client
 * </p>
 * <p>
 * <b>File:</b> MySqlListener.java
 * </p>
 * <p>
 * <b>last update:</b> 30.10.2016
 * </p>
 * <p>
 * <b>Time:</b> 09:04:36
 * </p>
 * <b>Description:</b>
 * <p>
 * Listener for MySql. On an arrived message it publishes to a given db into the
 * table with the part of the topic name before the "/" as the last part of the
 * topic name is used as a room description. Used for an example with a DHT22
 * Temperature and Humidity Sensor to track some room data
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
public class MySqlListener extends MessageListener {
	/**
	 * the database to insert into
	 */
	private Database db;

	/**
	 * Constructor
	 *
	 * @param client
	 *            is the mqtt client this listener belongs to
	 * @param db
	 *            is the database to insert into
	 */
	public MySqlListener(ClientMqtt client, Database db) {
		super(client);
		this.db = db;
		db.connect();
	}

	/**
	 * Adds a message (sensor data in this case) to a given table name (first
	 * part of the topic name). The second part of the topic name is used as a
	 * room description e.g. "living-room"
	 *
	 * @param topic
	 *            is the name of the topic as used in MQTT protocol
	 * @param message
	 *            is the message delivered via MQTT protocol
	 */
	public void messageArrived(String topic, MqttMessage message) {
		String split[] = topic.split("/");
		String tableName = split[0];
		String room = split[1];
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO " + tableName + "(Room, Value, Date)");
		sql.append("VALUES (?,?,?)");
		PreparedStatement statement = db.getStatement(sql.toString());
		if (statement != null) {
			try {
				statement.setString(1, room);
				statement.setFloat(2, Float.parseFloat(message.toString()));
				statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				db.executeStatement(statement);
			} catch (SQLException e) {
				System.out.println("Couldn't set prepared values into statement!");
				System.err.println(e.getMessage());
			}
		}
	}
}
