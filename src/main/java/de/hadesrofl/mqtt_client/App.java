package de.hadesrofl.mqtt_client;

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
 * <b>last update:</b> 19.10.2016
 * </p>
 * <p>
 * <b>Time:</b> 16:22:49
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
 * @version 0.1
 */
public class App {
	public static void main(String[] args) {
		String url = "tcp://localhost:1883";
		String topic = "test/topic";
		ClientMqtt client = new ClientMqtt(url, topic);
		Thread clientThread = new Thread(client);
		clientThread.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			System.err.println("Can't sleep, need to take painkillers");
		}
		client.publishMessage("Ground control to Major Tom", 1);
		try {
			client.cancel();
			clientThread.join();
			clientThread = null;
			client = null;
		} catch (InterruptedException e) {
			System.err.println("Error while joining client thread!");
		}
	}
}
