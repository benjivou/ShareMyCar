const mqtt = require('mqtt')

class MqttCommunicator {

    constructor() {
        this.mqttBrokerUrl = 'mqtt://192.168.1.148:1883'
        this.clientId = 'ShareMyCarBackend'
        this.client = mqtt.connect(this.mqttBrokerUrl, {clientId: this.clientId})
        this.driverTopic = 'share-my-car-driver'
        this.passengerTopic = 'share-my-car-passenger'
    }

    publishRequest(data) {
        const topic = data.requester === 'driver' ? this.driverTopic : this.passengerTopic;
        console.log(topic)
        this.client.publish(topic, JSON.stringify(data))
    }

}

module.exports = MqttCommunicator
