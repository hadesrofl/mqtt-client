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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import de.hadesrofl.database.Database;
import de.hadesrofl.json.JsonReader;

/**
 *
 *
 * <b>Project:</b> mqtt-client
 * <p>
 * <b>Packages:</b> de.hadesrofl.mqtt_client
 * </p>
 * <p>
 * <b>File:</b> App.java
 * </p>
 * <p>
 * <b>last update:</b> 22.10.2016
 * </p>
 * <p>
 * <b>Time:</b> 09:47:00
 * </p>
 * <b>Description:</b>
 * <p>
 * This Class starts a simple Mqtt Connection to a given server and topic and
 * subscribes to that or publishes to it.
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
 *
 */
public class App {
	public static void main(String[] args) {
		String cf = "";
		if (args.length > 0) {
			cf = args[0];
		} else {
			cf = "config.json";
		}
		JSONObject broker = JsonReader.readFile(cf).getJSONObject("broker");
		JSONObject topics = broker.getJSONObject("topics");
		Database db = null;
		try {
			JSONObject database = JsonReader.readFile(cf).getJSONObject(
					"database");
			db = new Database(database.getString("dbHost"),
					database.getString("dbPort"), database.getString("dbName"),
					database.getString("dbUser"), database.getString("dbPass"));
		} catch (JSONException e) {
			System.err.println("No database mentioned in config file");
		}
		List<ClientMqtt> clients = new ArrayList<ClientMqtt>();
		List<Thread> clientThreads = new ArrayList<Thread>();
		for (String topic : topics.keySet()) {
			ClientMqtt client = null;
			if (db == null) {
				client = new ClientMqtt(broker.getString("address"),
						broker.getInt("port"), topics.getString(topic));
			} else {
				client = new ClientMqtt(broker.getString("address"),
						broker.getInt("port"), topics.getString(topic), db);
			}
			Thread clientThread = new Thread(client);
			client.setSubscriber(broker.getBoolean("subscribe"));
			clients.add(client);
			clientThreads.add(clientThread);
			clientThread.start();
		}
		// Need to wait a bit as the client needs to connect first
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			System.err.println("Thread can't sleep, need to take painkillers");
		}
		// client.publishMessage("Ground control to Major Tom", 1);
		// client.setSubscriber(false);
		// // Need to wait a bit before the client changes subscription
		// try {
		// Thread.sleep(5000);
		// } catch (InterruptedException e) {
		// System.err.println("Thread can't sleep, need to take painkillers");
		// }
		// client.publishMessage("Ground control to Major Tom - unscribed", 1);
		// try {
		// client.close();
		// clientThread.join();
		// clientThread = null;
		// client = null;
		// } catch (InterruptedException e) {
		// System.err.println("Error while joining client thread!");
		// }
	}
}
