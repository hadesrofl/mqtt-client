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

package de.hadesrofl.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;

/**
 *
 *
 * <b>Project:</b> mqtt-client
 * <p>
 * <b>Packages:</b> de.hadesrofl.mqtt_client
 * </p>
 * <p>
 * <b>File:</b> JsonReader.java
 * </p>
 * <p>
 * <b>last update:</b> 04.11.2016
 * </p>
 * <p>
 * <b>Time:</b> 20:38:25
 * </p>
 * <b>Description:</b>
 * <p>
 * Reader for json files
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
public class JsonReader {
	/**
	 * Reads a json file and returns it as JSONObject
	 *
	 * @param file
	 *            is the file to read
	 * @return an json object
	 */
	public static JSONObject readFile(String file) {
		if (file != null && file.compareTo("") != 0)
			return readFile(new File(file));
		else
			return null;
	}

	/**
	 * Reads a json file and returns it as JSONObject
	 *
	 * @param file
	 *            is the file to read
	 * @return an json object
	 */
	public static JSONObject readFile(File f) {
		String jsonData = "";
		BufferedReader br = null;
		try {
			String line;
			br = new BufferedReader(new FileReader(f));
			while ((line = br.readLine()) != null) {
				jsonData += line + "\n";
			}
		} catch (IOException e) {
			System.err.println("Can't read json file!");
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				System.err.println("Can't close buffered reader!");
			}
		}
		JSONObject obj = null;
		if (jsonData.compareTo("") != 0)
			obj = new JSONObject(jsonData);
		return obj;
	}
}
