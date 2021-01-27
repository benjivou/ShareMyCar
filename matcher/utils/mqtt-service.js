const mqtt = require('mqtt');

class MqttService {
    constructor(matchingSystem) {
        this.mqttBrokerUrl = 'mqtt://localhost:1883'
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
                this.matcher.addDriverRequest(JSON.parse(data.toString()))
            } else if(topic === this.passengerTopic) {
                this.matcher.addPassengerRequest(JSON.parse(data.toString()))
            }

            this.matcher.runMatching().then(matches => {
                console.log('HERE', matches)
                matches.forEach(match => {
                    this.notifyMatch(match);
                })
            }).catch(err => {console.log('ERROR', err)})
            
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

    notifyMatch(match) {
        const driver = match.driver;
        const passenger = match.passenger;
        this.client.publish(passenger.id, JSON.stringify(driver))
        this.client.publish(driver.id, JSON.stringify(passenger))
    }
}

module.exports = MqttService;

