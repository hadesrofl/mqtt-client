[![build status](https://gitlab.com/hadesrofl/mqtt-client/badges/master/build.svg)](https://gitlab.com/hadesrofl/mqtt-client/commits/master)
[![coverage report](https://gitlab.com/hadesrofl/mqtt-client/badges/master/coverage.svg)](https://gitlab.com/hadesrofl/mqtt-client/commits/master)

# MQTT-Client
## A simple mqtt client written in Java based on the Paho Lib
-------------------------------------------------------------

This is a simple implementation for a MQTT client. This client has a small config file (as described below) and runs a thread for every topic. 

For my purpose it is set to subscribe and doesn't need to publish anything as it subscribes to topics, 
where sensor data is published and this client (running on a RPi) saves the values into a database. 
Therefore it has a special listener for the jdbc connection and mysql queries.

## Getting Started

To start this app it is essential to prepare a config.json file and pass as argument to the app or just put it into the same directory as the app. 
An example of a config file is given below. Right now there is no possibility to set the client to publish mode via the config file and 
it is necessary to add this function to the client. It is possible to send messages though, but I doesn't need it right now. 
It might be a bit special to do via the config and might need some minor changes to the client depending on the person and the situation the client is used.

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