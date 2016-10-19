package de.hadesrofl.mqtt_client;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * 
 * 
 * <b>Project:</b> mqtt-client
 * <p>
 * <b>Packages:</b> de.hadesrofl.mqtt_client
 * </p>
 * <p>
 * <b>File:</b> MessageListener.java
 * </p>
 * <p>
 * <b>last update:</b> 19.10.2016
 * </p>
 * <p>
 * <b>Time:</b> 16:57:46
 * </p>
 * <b>Description:</b>
 * <p>
 * This class listens to a subscribed topic and handles published and delivered
 * messages
 * </p>
 * 
 * @author Rene Kremer
 *         <p>
 *         Copyright (c) 2016 by Rene Kremer
 *         </p>
 * @version 0.1
 */
public class MessageListener implements MqttCallback {
	/**
	 * 
	 * connectionLost This callback is invoked upon losing the MQTT connection.
	 * 
	 * @param t
	 *            is a throwable object containing the error
	 * 
	 */
	public void connectionLost(Throwable t) {
		System.err.println("Connection lost: \n" + t.getStackTrace());
	}

	/**
	 * 
	 * This function is invoked if a message is delivered
	 * 
	 * @param token
	 *            is the delivered token of the message
	 * 
	 */
	public void deliveryComplete(IMqttDeliveryToken token) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("Message sent: " + token.getMessage().toString()
					+ "in Topics: ");
		} catch (MqttException e) {
			System.err.println("Message doesn't exist!");
		}
		for (int i = 0; i < token.getTopics().length; i++) {
			sb.append(token.getTopics()[i]);
		}
		System.out.println(sb.toString());
	}

	/**
	 * 
	 * This function is invoked if a message arrived
	 * 
	 * @param topic
	 *            is the name of the topic
	 * @param message
	 *            is the message arrived in the topic and at this client
	 * 
	 */
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
		System.out.println("Topic: " + topic + "\tMessage: "
				+ message.toString());

	}
}
