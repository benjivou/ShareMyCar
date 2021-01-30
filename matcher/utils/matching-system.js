const geolib = require('geolib');

class MatchingSystem {
    constructor () {
        this.driverRequest = []
        this.passengerRequest = []
        this.isProcessing = false;
        this.refusedMatches = []
        this.acceptedMatches = []
    }

    addDriverRequest(req) {
        this.driverRequest.push(req)
    }

    addPassengerRequest(req) {
        this.passengerRequest.push(req)
    }

    findMatchStatus(match) {
        const status = 'N/A'
        this.acceptedMatches.forEach((match) => {
            if(acceptedMatch.driver.id === match.driver.id && acceptedMatch.passenger.id === match.passenger.id) {
                status = 'accepted';
            }
        })
        this.refusedMatches.forEach((match) => {
            if(acceptedMatch.driver.id === match.driver.id && acceptedMatch.passenger.id === match.passenger.id) {
                status = 'refused';
            }
        })

        return status;
    }

    acceptMatch(acceptedMatch) {
        let status = null;
        const matchStatus = findMatchStatus(acceptedMatch)
        switch(matchStatus) {
            case 'accepted':
                status = 'accept'
                break;
            case 'refused':
                status = 'refuse'
                break;
            case 'N/A':
            default:
                this.acceptedMatches.push(acceptedMatch)
                break;
        }
        return status;
    }

    refuseMatch(refusedMatch) {
        let status = null;
        const matchStatus = findMatchStatus(refusedMatch)
        switch(matchStatus) {
            case 'accepted':
            case 'refused':
                status = 'refuse'
                break;
            case 'N/A':
            default:
                this.refusedMatches.push(refusedMatch)
                break;
        }
        return status;
    }

    updatePosition(id, position) {
        this.driverRequest.forEach(req => {
            if(req.user.id === id)
                req.position = position
        })
        this.passengerRequest.forEach(req => {
            if(req.user.id === id)
                req.position = position
        })
    }

    runMatching() {
        return new Promise((resolve, reject) => {
            const matches = [];
            if(this.driverRequest.length !== 0 && this.passengerRequest.length !== 0) {
                this.isProcessing = true;
                // Créer une copie des listes des requetes puis vide les listes pour ne pas que 2 promesses traitent les même requetes
                const tmpDriver = this.driverRequest;
                const tmpPassenger = this.passengerRequest;
                this.driverRequest = []
                this.passengerRequest = [] 

                // matching strategy's code 
                tmpDriver.forEach((val, i) => {
                    const d = tmpDriver[i]
                    tmpPassenger.forEach((val, i) => {
                        const p = tmpPassenger[i]
                        const maxDist = d.maxDist * 1000
                        if(maxDist >= geolib.getDistance(d.position, p.position))
                            matches.push({
                                'driver': d,
                                'passenger': p
                            })
                    })
                })

                // Suppress request that have matched
                matches.forEach(match => {
                    tmpDriver.splice(tmpDriver.indexOf(match.driver), 1);
                    tmpPassenger.splice(tmpPassenger.indexOf(match.passenger), 1);
                })

                // Rajout des requetes qui n'ont pas matché dans la liste des requetes à traiter
                tmpDriver.forEach(driverReq => {this.addDriverRequest(driverReq)})
                tmpPassenger.forEach(passengerReq  => {this.addPassengerRequest(passengerReq)})

                this.isProcessing = false;
            }
            resolve(matches)
        })
    }
}

module.exports = MatchingSystem;
