const mqtt = require('mqtt')

class MqttCommunicator {

    constructor() {
        this.mqttBrokerUrl = 'mqtt://192.168.1.148:3000'
        this.clientId = 'ShareMyCarBackend'
        this.client = mqtt.connect(this.mqttBrokerUrl, {clientId: this.clientId})
        this.driverTopic = 'share-my-car-driver'
        this.passengerTopic = 'share-my-car-passenger'
    }

    publishRequest(data) {
        const topic = data.requester.driver ? this.driverTopic : this.passengerTopic;
        this.client.publish(topic, data)
    }

}

module.exports = MqttCommunicator
