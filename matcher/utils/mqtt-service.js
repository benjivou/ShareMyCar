const mqtt = require('mqtt');

class MqttService {
    constructor(matchingSystem) {
        this.mqttBrokerUrl = 'mqtt://<ip>:1883'
        this.clientId = 'ShareMyCarMacther'
        this.client = mqtt.connect(this.mqttBrokerUrl, {clientId: this.clientId})
        this.driverTopic = 'share-my-car-driver'
        this.passengerTopic = 'share-my-car-passenger'
        this.matcher = matchingSystem

        this.client.on('connect', () => {
            console.log('CONNECTED')
            this._subscribeToInterrestingTopics()
        })

        this.client.on('message', (topic, data) => {
            console.log('Got message')
            if(topic === this.driverTopic) {
                console.log('RECEIVED : '+ data)
                this.matcher.addDriverRequest(JSON.parse(data.toString()))
            } else if(topic === this.passengerTopic) {
                this.matcher.addPassengerRequest(JSON.parse(data.toString()))
            }
        })
    }

    _subscribeToInterrestingTopics() {
        const topics = [this.driverTopic, this.passengerTopic]
        topics.forEach(topic => {
            this.client.subscribe(topic, (err) => {
                if (err) {
                    console.log(`An error occurred while subscribing to topic : ${topic}.`)
                }
            })
        })
    }
}

module.exports = MqttService;

