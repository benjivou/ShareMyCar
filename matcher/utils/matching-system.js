
class MatchingSystem {
    constructor () {
        this.driverRequest = []
        this.passengerRequest = []
    }

    addDriverRequest(req) {
        this.driverRequest.push(req)
    }

    addPassengerRequest(req) {
        this.passengerRequest.push(req)
    }

    runMatching() {
        
    }
}

module.exports = MatchingSystem;
