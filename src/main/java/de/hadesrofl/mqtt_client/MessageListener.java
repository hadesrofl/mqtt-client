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
 * <b>last update:</b> 22.10.2016
 * </p>
 * <p>
 * <b>Time:</b> 09:47:00
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
 *         <p>Licensed under the Apache License, Version 2.0</p>
 * @version 0.1
 */
public class MessageListener implements MqttCallback {
	/**
	 * Client using this listener
	 */
	private ClientMqtt myClient;

	/**
	 * Constructor
	 *
	 * @param client
	 *            is the client using this listener
	 */
	public MessageListener(ClientMqtt client) {
		this.myClient = client;
	}

	/**
	 *
	 * connectionLost This callback is invoked upon losing the MQTT connection.
	 *
	 * @param t
	 *            is a throwable object containing the error
	 *
	 */
	public void connectionLost(Throwable t) {
		System.err.println("Connection lost: \nTrying to reconnect..." + t.getStackTrace());
		myClient.connect();
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
		try {
			if (token.getMessage() != null) {
				String ret;
				StringBuilder sb = new StringBuilder();
				sb.append("Delivered: " + token.getMessage());
				if (token.getTopics() != null) {
					sb.append(" to Topic: ");
					for (String s : token.getTopics()) {
						sb.append(s + ", ");
					}
					ret = sb.toString().substring(0, sb.toString().length() - 1);
				} else {
					ret = sb.toString();
				}
				System.out.println(ret);
			}
		} catch (MqttException e) {
			System.err.println("No message in delivery included!");
		}
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
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("Arrived Message at Topic: " + topic + "\tMessage: " + message.toString());

	}
}
