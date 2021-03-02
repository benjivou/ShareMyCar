const mqtt = require('mqtt');

class MqttService {
    constructor(matchingSystem) {
        this.mqttBrokerUrl = 'mqtt://broker.emqx.io:1883'
        this.clientId = 'ShareMyCarMacther'
        this.client = mqtt.connect(this.mqttBrokerUrl, {clientId: this.clientId})
        this.driverTopic = 'share-my-car-driver'
        this.passengerTopic = 'share-my-car-passenger'
        this.positionTopic = 'positions'
        this.matchTopic = 'matches'
        this.matcher = matchingSystem

        this.client.on('connect', () => {
            console.log('CONNECTED')
            this._subscribeToInterrestingTopics()
        })

        this.client.on('message', (topic, data) => {
            console.log('Got message')
            console.log(topic)
            if(topic === this.matchTopic) {
                this._handleMatchWill(data.toString());
            } else {
                if(topic === this.driverTopic) {
                    this.matcher.addDriverRequest(JSON.parse(data.toString()))
                } else if(topic === this.passengerTopic) {
                    this.matcher.addPassengerRequest(JSON.parse(data.toString()))
                } else if(topic === this.positionTopic) {
                    const splitData = data.toString().split('/')
                    const userId = splitData[0].trim();
                    const position = JSON.parse(splitData[1].trim());
                    this.matcher.updatePosition(userId, position);
                    
                    this.matcher.runMatching().then(matches => {
                        matches.forEach(match => {
                            this.notifyMatch(match);
                        })
                    }).catch(err => {console.log('ERROR', err)})
                }
    
                
            }
            
        })
    }

    _subscribeToInterrestingTopics() {
        const topics = [this.driverTopic, this.passengerTopic, this.positionTopic, this.matchTopic]
        topics.forEach(topic => {
            this.client.subscribe(topic, (err) => {
                if (err) {
                    console.log(`An error occurred while subscribing to topic : ${topic}.`)
                }
            })
        })
    }0

    _handleMatchWill(data) {
        const splitData = data.split('/');
        const agreement = splitData[0].trim();
        const match = JSON.parse(splitData[1].trim());
        let status;
        if(agreement === 'accept') {
            status = this.matcher.acceptMatch(match);
        } else if(agreement === 'refuse') {
            status = this.matcher.refuseMatch(match);
        }

        if(status && status !== null) {
            const m = this.matcher.getMatchByUserId(match)
            this.notifyMatchStatus(status, m.driver.user.id, m.passenger.user.id);
        }
    }

    notifyMatch(match) {
        const driver = match.driver.user;
        const passenger = match.passenger.user;
        this.matcher.addMatch(match)
        this.client.publish(`${passenger.id}`, JSON.stringify(driver));
        this.client.publish(`${driver.id}`, JSON.stringify(passenger));
    }

    notifyMatchStatus(status, driverId, passengerId) {
        this.client.publish(passengerId, status);
        this.client.publish(driverId, status);
        if(status === 'accept') {
            console.log('SENDING TOPICS');
            console.log('PASSENGER', passengerId)
            console.log('DRIVER', driverId)
            const topic1 = Date.now();
            const topic2 = topic1 + 1
            this.client.publish(`${passengerId}`, JSON.stringify({pub: topic1, sub: topic2}));
            this.client.publish(`${driverId}`, JSON.stringify({pub: topic2, sub: topic1}));
        }
    }

}

module.exports = MqttService;

