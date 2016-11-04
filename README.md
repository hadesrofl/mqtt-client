# MQTT-Client
## A simple mqtt client written in Java based on the Paho Lib
-------------------------------------------------------------

This is a simple implementation for a MQTT client. This client has a small config file (as described below) and runs a thread for every topic. 

For my purpose it is set to subscribe and doesn't need to publish anything as it subscribes to topics, 
where sensor data is published and this client (running on a RPi) saves the values into a database. 
Therefore it has a special listener for the jdbc connection and mysql queries.

## Example for the config file

```
{	
    "broker": {		
        "address": "localhost",		
        "port": "1883",		
        "topics": {			
            "t1": "temperature/living-room",			
            "t2": "humidity/living-room"		
        },
        "subscribe": "true"	},	
    "database":{		
        "dbHost": "localhost",		
        "dbPort": "3306",		
        "dbName": "TableName",		
        "dbUser": "Username",		
        "dbPass": "Password"	
    }
}
```


