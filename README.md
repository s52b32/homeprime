# HomePrime

## Introduction
HomePrime consists of two Java Spring applications allowing control over REST API. Usage can be wide, but original usage was in smart home system.

- HomePrime Agent
- HomePrime Manager

### HomePrime Agent

System running agent application is system where actual sensors are connected to. This application can read/control/monitor all the connected sensors and expose their state over REST API towards client. Using REST API agent can be integrated to act in more complex system. What can be controlled:

- contact sensors (push switch, regular switch, magnetic switch, motion sensor)
- relays (NO/NC)
- temperature sensors (LM75A - I2C)

On top of the above (controllable actuators) agent system exposes system monitoring of the system running the application allowing client to act on certain conditions (power off, reboot, clean):

- System CPU temperature
- System Free/Used memory
- System disk space

Basic media capabilities are also possible on agent if agent system is connected to speaker:

- Text To Speech
- Stream sound from URL

### HomePrime Manager

Manager is Java based Spring application developed to provide lifecycle management capabilities over multiple agents. In case user has multiple agents manager application can perform operations over all of them (or selected) via REST API. Supported operations:

- create backup of all agents
- upload new agent software (image) to all agents
- update agent software
- reboot all agents
- terminate agent application on all agents


## Usage

### Prepare configuration

Before agent or manager application is started, user has to create initial configuration. Configuration is stored to configs directory. Configs directory is located in same place/directory where agent executable is (.jar). On application startup configuration files are validated. There are multiple configuration files:

- configs/configuration.json 
    - (Agent, Manager)
- configs/things.json
    - (Manager)
- configs/items/contacts.json
    - (Agent) 
- configs/items/relays.json
    - (Agent) 
- configs/items/temperature_sensors.json
    - (Agent)
- configs/items/sound.json
    - (Agent) 
- configs/items/tts.json
    - (Agent) 
    
#### configuration.json

Example of agent/manager configuration file.

```
{
    "uuid":"5b193c42-0e07-43c0-809d-baf2facee161",
    "name":"default",
    "agent":{
        "type":"Mock",
        "ip":"127.0.0.1",
        "port":8085,
        "loggerType":"Standard",
        "loggerFilePath": "/tmp/agent_log.log"
    },
    "management":{
        "ip":"127.0.0.1",
        "port":8080,
        "name": "client-name"
        "mode": "Pull",
        "appType":"OpenHAB"
    }
}
```

where:

- **uuid**: unique id of agent. Used for manager application to refer to certain agent
- **name**: user friendly name of agent.
- agent **type**: type of system running agent application. Mock and RaspberryPi are supported today. Mock is good for testing purposes.
- agent **ip**: IP address of agent machine. Not used internally.
- agent **port**: port for REST API that agent application will use.
- agent **loggerType**: type of logging. Possible File/Void/Standard. Standard means that logs will shown in standard console only. File instructs agent to store logs to file defined by loggerFilePath parameter. Void is that no logs are displayed.
- management **ip**: IP address of management application. Note that this is not HomePrime Manager related. This depends on appType.
- management **port**: port used with IP address.
- management **name**: user friendly name for management application
- management **mode**: Possible Pull/Push. Pull is approach when agent is queried by client to read states. Push is approach where agent pushes its state changes towards management application. In push case, agent runs listeners as thread and any state change is sent to management application. Push logic needs appType parameter to be set and supported. Today only OpenHAB system as management application is supported. Future implementation will have MQTT too.

#### things.json

Example of things.json configuration file is shown. It defines list of agents that manager application can control.

```
{
  "things": [
    {
      "uuid": "8fd17eb4-0ea6-49aa-a704-e0b8779b2764",
      "name": "agent_1",
      "agent": {
        "ip": "192.168.0.4",
        "port": 8081
      }
    },
    {
      "uuid": "8fd17eb4-0ea6-49aa-a704-e0b8779b2764",
      "name": "agent_N",
      "agent": {
        "ip": "192.168.0.2",
        "port": 8081
      }
    }
  ]
}
```

where:

- **uuid**: unique id of agent. Used for manager application to refer to certain agent in case some operation is only for certain agent.
- **name**: user friendly name of agent.
- agent **ip**: IP address of agent machine.
- agent **port**: port for REST API that agent application uses.

#### contacts.json

Example of items/contacts.json configuration file is shown. It defines list of contact sensors that agent application can monitor.

```
{
    "contacts": [
        {
            "id": 0,
            "pin": 22,
            "name": "Entry Door Light Switch",
            "contactType": "WallSwitch",
            "status": "Connected",
            "wiringType": "PullDown",
            "debounceTime": 300,
            "openhabId": "Light_Outdoor_Frontdoor_Manual"
        },
        {
            "id": 1,
            "pin": 23,
            "name": "Attic Light Switch",
            "contactType": "PushSwitch",
            "status": "Connected",
            "wiringType": "Off",
            "debounceTime": 300,
            "openhabId": "Light_Attic_Ceiling_Manual"
        }
       ]
}
```

where:

- **id**: id of contact that can be used on REST API to read state.
- **pin**: id of GPIO pin contact is wired to.
- **name**: user friendly name of contact.
- **contactType**: Possible WallSwitch/PushSwitch/MagneticSwitch/MotionSwitch/Generic.
- **status**: only contacts with Connected state will be monitored.
- **wiringType**: pull resistance GPIO is connected with contact. Underlaying system can use this value to utilize internal pull resistors. This is used if RaspberryPi is agent system type. If contact has physical pull resistance, keep value to Off.
- **debounceTime**: debounce time in milliseconds for contact.
- **openhabId**: optional field needed in case of Push management appType with OpenHAB. Represents contact id that management application can use to identify contact.

#### relays.json

Example of items/relays.json configuration file is shown. It defines list of relays that agent application can control.

```
{
        "relays": [
            {
                "id": 1,
                "pin": 2,
                "name": "Entry Door Wall Light",
                "relayType": "NO",
                "payloadType": "Generic",
                "openhabId": "Light_Outdoor_Frontdoor"
            },
            {
                "id": 2,
                "pin": 3,
                "name": "Attic Ceiling Light",
                "relayType": "NO",
                "payloadType": "Generic",
                "openhabId": "Light_Attic_Ceiling"
            }
        ]
}
```

where:

- **id**: id of relay that can be used on REST API to read/set state.
- **pin**: id of GPIO pin relay is wired to.
- **name**: user friendly name of relay.
- **relayType**: Possible NO/NC. Based on this read/set value will act internally. I.e. if relay pin state is false it would naturally say that it is disabled but in case of NC relay it means it is active.
- **payloadType**: Possible Generic/Light/WallHeater. Not used internally. Keep as Generic.
- **openhabId**: optional field needed in case of Push management appType with OpenHAB. Represents relay id that management application can use to identify relay.

#### temperature_sensors.json

Example of items/temperature_sensors.json configuration file is shown. It defines list of temperature sensors that agent application can control.

```
{
    "temperatureSensors": [
        {
            "id": 0,
            "i2cBus": "1",
            "i2cAddress": "0x48",
            "name": "Attic Temperature Sensor",
            "sensorType": "LM75",
            "sensorValueType": "Celsius",
            "openhabId": "Temperature_Attic"
        }
    ]
}
```

where:

- **id**: id of temperature sensor that can be used on REST API to read state.
- **i2cBus**: id of I2C bus.
- **i2cAddress**: address of temperature sensor on I2C bus.
- **name**: user friendly name of sensor.
- **sensorType**: Possible LM75/LM75A. Only LM75 is tested.
- **sensorValueType**: Possible Celsius/Farenheit. Not used internally prepared for client so it knows what value is provided.
- **openhabId**: optional field needed in case of Push management appType with OpenHAB. Represents relay id that management application can use to identify relay.

#### sound.json

Example of items/sound.json configuration file is shown. It defines list of sound stream URLs and speakers agent is connected to.

```
{
    "streams": [
        {
            "id": 0,
            "name": "Dalmacija",
            "url": "http://shoutcast.pondi.hr:8000/listen.pls"
        },
        {
            "id": 1,
            "name": "Split",
            "url": "https://radio.hrt.hr/stream/14"
        }
    ],
    "speakers": [
        {
            "id": 0,
            "name": "Mini Speaker",
            "outputType": "_3_5mm",
            "channelId": "PCM",
            "isActive": true
        }
    ]
}
```

where:

- streams **id**: id of stream used via REST API to start stream on.
- streams **name**: user friendly stream name.
- streams **url**: url from where sound is streamed.
- speakers **id**: id of speaker.
- speakers **name**: user friendly speaker name.
- speakers **outputType**: Possible _3_5mm/Bluetooth. Currently only _3_5mm is implemented.
- speakers **channelId**: id of speaker channel id on agent system. It is possible to have more of these.
- speakers **isActive**: says if speaker is enabled on agent system or not. Only if speaker is active can be used.

#### tts.json

Example of items/tts.json configuration file is shown. It defines TTS related info.

```
{
    "tts_configs": [
        {
            "id": 0,
            "name": "default",
            "ttsType": "Offline",
            "executor": "/home/pi/speak",
            "enabled": false
        }
    ]
}

```

where:

- **id**: id of TTS system.
- **name**: user friendly TTS name.
- **ttsType**: Possible Offline/Online. Offline TTS system means that speech synthesis is full offline based on application running on agent system. Online type requires Internet connectivity to produce speech from text.
- **executor**: script used to translate text to speech.
- **enabled**: defines if TTS is enabled on agent.

### Run HomePrime Agent

Current revision requires that all the above configuration files exist to start agent. Main file for agent execution is configuration.json that has to be produced in configs directory. All other JSON files should exist but can be empty (empty lists).

```
$ java -jar homeprime-agent-R7A06.jar

```

### Run HomePrime Manager

Main file for manager execution is configuration.json that has to be produced in configs directory along with things.json file.

```
$ java -jar homeprime-manager-R7A06.jar

```
