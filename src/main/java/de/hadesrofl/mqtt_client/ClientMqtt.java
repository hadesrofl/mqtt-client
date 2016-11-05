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

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import de.hadesrofl.database.Database;

/**
 *
 *
 * <b>Project:</b> mqtt-client
 * <p>
 * <b>Packages:</b> de.hadesrofl.mqtt_client
 * </p>
 * <p>
 * <b>File:</b> ClientMqtt.java
 * </p>
 * <p>
 * <b>last update:</b> 04.11.2016
 * </p>
 * <p>
 * <b>Time:</b> 20:38:00
 * </p>
 * <b>Description:</b>
 * <p>
 * This class connects to a given server and topic, subscribes it and might
 * publish to it. The subscription is threaded so that it might not be
 * </p>
 *
 * @author Rene Kremer
 *         <p>
 *         Copyright (c) 2016 by Rene Kremer
 *         </p>
 *         <p>
 *         Licensed under the Apache License, Version 2.0
 *         </p>
 * @version 0.21
 */
public class ClientMqtt implements Runnable {
	/**
	 * Url of the broker
	 */
	private String brokerUrl = "tcp://";
	/**
	 * Topic to subscribe to
	 */
	private String topic = null;
	/**
	 * Username used for the broker
	 */
	private String username = null;
	/**
	 * Password used for the broker
	 */
	private String password = null;
	/**
	 * Client ID used for the connection
	 */
	private final String CLIENT_ID = UUID.randomUUID().toString();
	/**
	 * Database object to persist data to
	 */
	private Database db = null;
	/**
	 * Max amount of possible retries. Currently 10
	 */
	public final int MAX_RETRIES = 10;
	/**
	 * Listen to topic?
	 */
	private boolean subscriber = false;
	/**
	 * Retries to connect
	 */
	private int connectionRetries = 0;
	/**
	 * Client object for the connection
	 */
	private MqttClient myClient = null;
	/**
	 * Option of the connection
	 */
	private MqttConnectOptions connOpt;
	/**
	 * boolean to cancel thread if running
	 */
	private volatile Boolean close = false;

	/**
	 * Constructor with default port 1883
	 *
	 * @param url
	 *            is the url of the broker
	 * @param topic
	 *            is the topic to subscribe to
	 */
	public ClientMqtt(String url, String topic) {
		brokerUrl += url + ":1883";
		this.topic = topic;
	}

	/**
	 * Constructor
	 *
	 * @param url
	 *            is the url of the broker
	 * @param port
	 *            is the port of the broker
	 * @param topic
	 *            is the topic to subscribe to
	 */
	public ClientMqtt(String url, int port, String topic) {
		brokerUrl += url + ":" + port;
		this.topic = topic;
	}

	/**
	 * Constructor
	 *
	 * @param url
	 *            is the url of the broker
	 * @param port
	 *            is the port of the broker
	 * @param topic
	 *            is the topic to subscribe to
	 */
	public ClientMqtt(String url, int port, String topic, Database db) {
		brokerUrl += url + ":" + port;
		this.topic = topic;
		this.db = db;
	}

	/**
	 * (Boa)Constructor with default port 1883
	 *
	 * @param url
	 *            is the url to the mqtt broker
	 * @param topic
	 *            is the name of the topic
	 * @param username
	 *            is the username used for the broker
	 * @param password
	 *            is the password used for the broker
	 */
	public ClientMqtt(String url, String topic, String username, String password) {
		brokerUrl += url + ":1883";
		this.topic = topic;
		this.username = username;
		this.password = password;
	}

	/**
	 * Constructor
	 *
	 * @param url
	 *            is the url to the mqtt broker
	 * @param port
	 *            is the port of the broker
	 * @param topic
	 *            is the name of the topic
	 * @param username
	 *            is the username used for the broker
	 * @param password
	 *            is the password used for the broker
	 */
	public ClientMqtt(String url, int port, String topic, String username,
			String password) {
		brokerUrl += url + ":" + port;
		this.topic = topic;
		this.username = username;
		this.password = password;
	}

	/**
	 * Runs the client and connects and subscribes to the server and topic.
	 * Disconnects if {@link #close() cancel} is called
	 */
	public synchronized void run() {
		// setup MQTT Client and connect to broker
		try {
			myClient = new MqttClient(brokerUrl, CLIENT_ID);
			if (db == null) {
				myClient.setCallback(new MessageListener(this));
			} else {
				myClient.setCallback(new MySqlListener(this, db));
			}
		} catch (MqttException e) {
			System.out.println("Could'nt create a new MqttClient!");
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		connect();

		boolean subscribed = false;
		while (!close) {
			if (subscriber && !subscribed) {
				try {
					int subQoS = 0;
					myClient.subscribe(topic, subQoS);
					subscribed = true;
				} catch (Exception e) {
					System.out.println("Can't subscribe to " + topic);
					System.err.println(e.getMessage());
				}
			} else if (!subscriber && subscribed) {
				try {
					myClient.unsubscribe(topic);
				} catch (MqttException e) {
					System.out.println("Can't unscribe from " + topic);
					System.err.println(e.getMessage());
				}
			}
		}
		disconnect();
	}

	/**
	 * Publishes a message to the topic 
	 * 
	 * @param message
	 *            is the message as String
	 * @param qos
	 *            is the quality as service as mentioned by (see <a href=
	 *            "https://www.eclipse.org/paho/files/javadoc/index.html" >Paho
	 *            JavaDoc</a>)
	 */
	public void publishMessage(String message, int qos) {
		MqttMessage post = new MqttMessage(message.getBytes());
		post.setQos(qos);
		post.setRetained(false);
		MqttDeliveryToken token = null;
		try {
			// publish message to broker
			token = this.getTopic().publish(post);
			// Wait until the message has been delivered to the broker
			token.waitForCompletion();
			Thread.sleep(100);
		} catch (Exception e) {
			System.out.println("Couldn't publish the message!");
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Sets a flag for subscribing to topic
	 *
	 * @param b
	 *            is the flag
	 */
	public void setSubscriber(boolean b) {
		subscriber = b;
	}

	/**
	 * Cancels the thread
	 */
	public void close() {
		this.close = true;
	}

	/**
	 * Gets the topic to which this client is subscribed to
	 *
	 * @return the topic as MqttTopic object
	 */
	public MqttTopic getTopic() {
		return myClient.getTopic(topic);
	}

	/**
	 * Connects to the broker. If it can't connect, the client will wait one
	 * second and reconnect up to {@link MAX_RETRIES} times. After unsuccessful
	 * MAX_RETRIES the program terminates
	 *
	 */
	public void connect() {
		connOpt = new MqttConnectOptions();
		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(30);
		if (username != null) {
			connOpt.setUserName(username);
		}
		if (password != null) {
			connOpt.setPassword(password.toCharArray());
		}
		for (connectionRetries = 0; connectionRetries < MAX_RETRIES; connectionRetries++) {
			// Connect to Broker
			try {
				myClient.connect(connOpt);
				System.out.println("Connected to " + brokerUrl);
				break;
			} catch (MqttException e) {
				System.out.println("Couln't connect!");
				System.err.println(e.getMessage());
			}
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				System.out
//						.println("Thread can't sleep, need to take painkillers");
//				System.err.println(e.getMessage());
//			}
		}
		if (connectionRetries >= MAX_RETRIES) {
			System.exit(-1);
		}
		if (subscriber)
			try {
				myClient.subscribe(topic);
			} catch (MqttException e) {
				System.out.println("Error while subscribing to " + topic);
				System.err.println(e.getMessage());
			}
		System.out.println("Connected to " + brokerUrl);
	}

	/**
	 * Disconnects from the broker
	 */
	public void disconnect() {
		// disconnect
		try {
			// wait to ensure subscribed messages are delivered
			Thread.sleep(5000);
			if (myClient != null) {
				myClient.disconnect();
				db.disconnect();
			}
		} catch (Exception e) {
			System.out.println("Error while disconnecting from server!");
			System.err.println(e.getMessage());
		}
	}
}
