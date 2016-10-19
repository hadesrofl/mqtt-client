package de.hadesrofl.mqtt_client;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

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
 * <b>last update:</b> 19.10.2016
 * </p>
 * <p>
 * <b>Time:</b> 16:25:18
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
 * @version 0.1
 */
public class ClientMqtt implements Runnable {
	/**
	 * Url of the broker
	 */
	String BROKER_URL = null;
	/**
	 * Topic to subscribe to
	 */
	String M2MIO_TOPIC = null;
	/**
	 * Username used for the broker
	 */
	String M2MIO_USERNAME = null;
	/**
	 * Password used for the broker
	 */
	String M2MIO_PASSWORD = null;
	/**
	 * Client ID used for the connection
	 */
	String CLIENT_ID = UUID.randomUUID().toString();
	/**
	 * Client object for the connection
	 */
	MqttClient myClient = null;
	/**
	 * boolean to cancel thread if running
	 */
	volatile Boolean cancel = false;

	/**
	 * Constructor
	 * 
	 * @param url
	 *            is the url of the broker
	 * @param topic
	 *            is the topic to subscribe to
	 */
	public ClientMqtt(String url, String topic) {
		BROKER_URL = url;
		M2MIO_TOPIC = topic;
	}

	/**
	 * (Boa)Constructor
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
		BROKER_URL = url;
		M2MIO_TOPIC = topic;
		M2MIO_USERNAME = username;
		M2MIO_PASSWORD = password;
	}

	/**
	 * Runs the client and connects and subscribes to the server and topic.
	 * Disconnects if {@link #cancel() cancel} is called
	 */
	public synchronized void run() {
		// setup MQTT Client
		MqttConnectOptions connOpt = new MqttConnectOptions();

		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(30);
		if (M2MIO_USERNAME != null) {
			connOpt.setUserName(M2MIO_USERNAME);
		}
		if (M2MIO_PASSWORD != null) {
			connOpt.setPassword(M2MIO_PASSWORD.toCharArray());
		}

		// Connect to Broker
		try {
			myClient = new MqttClient(BROKER_URL, CLIENT_ID);
			myClient.setCallback(new MessageListener());
			myClient.connect(connOpt);
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("Connected to " + BROKER_URL);

		// subscribe to topic if subscriber
		try {
			int subQoS = 0;
			myClient.subscribe(M2MIO_TOPIC, subQoS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (!cancel)
			;
		// disconnect
		try {
			// wait to ensure subscribed messages are delivered
			Thread.sleep(5000);
			if (myClient != null) {
				myClient.disconnect();
			}
		} catch (Exception e) {
			System.err.println("Error while disconnecting from server!");
		}
	}

	/**
	 * Publishes a message to the topic
	 * 
	 * @param message
	 *            is the message as String
	 * @param qos
	 *            is the quality as service as mentioned by (see <a
	 *            href="https://www.eclipse.org/paho/files/javadoc/index.html"
	 *            >Paho JavaDoc</a>)
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
			e.printStackTrace();
		}
	}

	/**
	 * Cancels the thread
	 */
	public void cancel() {
		this.cancel = true;
	}

	/**
	 * Gets the topic to which this client is subscribed to
	 * 
	 * @return the topic as MqttTopic object
	 */
	public MqttTopic getTopic() {
		return myClient.getTopic(M2MIO_TOPIC);
	}
}
