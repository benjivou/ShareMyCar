
class MatchingSystem {
    constructor () {
        this.driverRequest = []
        this.passengerRequest = []
        this.isProcessing = false;
    }

    addDriverRequest(req) {
        this.driverRequest.push(req)
    }

    addPassengerRequest(req) {
        this.passengerRequest.push(req)
    }

    runMatching() {
        return new Promise((resolve, reject) => {
            const matches = [];
            if(tmpDriver.length === 0 && tmpPassenger.length === 0) resolve(matches)
            this.isProcessing = true;
            // Créer une copie des listes des requetes puis vide les listes pour ne pas que 2 promesses traitent les même requetes
            const tmpDriver = this.driverRequest;
            const tmpPassenger = this.passengerRequest;
            this.driverRequest = []
            this.passengerRequest = [] 

            // TODO - Add matching code strategy
            let looper;
            looper = tmpDriver.length > tmpPassenger.length ? tmpDriver : tmpPassenger;

            looper.forEach((val, i) => {
                matches.push({
                    'driver': tmpDriver[i],
                    'passenger': tmpPassenger[i]
                })
            })

            // Suppression des requetes qui ont matché
            matches.forEach(match => {
                tmpDriver.splice(tmpDriver.indexOf(match.driver), 1);
                tmpPassenger.splice(tmpPassenger.indexOf(match.passenger), 1);
            })

            // Rajout des requetes qui n'ont pas matché dans la liste des requetes à traiter
            tmpDriver.forEach(driverReq => {this.addDriverRequest(driverReq)})
            tmpPassenger.forEach(passengerReq  => {this.addPassengerRequest(passengerReq)})

            this.isProcessing = false;
            resolve(matches)
        })
    }
}

module.exports = MatchingSystem;
