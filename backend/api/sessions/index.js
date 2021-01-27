const {Router} = require('express');
const RestOperator = require('../../utils/rest-operation');
const sessions = require('./sessions.json');
const bodyParser = require('body-parser');
const MqttCommunicator = require('../../utils/mqtt-communicator');


const router = new Router();
//Setup the RestOperation module
const rest_operator = new RestOperator('sessions', 'sessions/sessions.json');
const communicator = new MqttCommunicator();

//Creation of the routes
router.get('/', (req, res) => {
    res.status(200).json(sessions)
});
router.get('/:sessionId', (req, res) => {
    try {
        const s = rest_operator.getById(req.params.sessionId);
        res.status(200).json(s); 
    } catch(e) {
        if(e.name == 'ItemNotFoundError') {
            res.status(404).json(e.getMessage(`Cannot find session with id ${req.params.sessionId} in the existing sessions`));
        } else {
            res.status(500).json(e);
        }
    }
});
router.post('/', (req, res) => {
    try {
        const s = rest_operator.post(req.body);
        communicator.publishRequest(s);
        res.status(201).json(s);
    } catch(e) {
        res.status(500).json(e);
    }
});
router.put('/:sessionId', (req, res) => {
    try {
        const s = rest_operator.update(req.params.sessionId, req.body);
        res.status(200).json(s);
    } catch(e) {
        if(e.name == 'ItemNotFoundError') {
            res.status(404).json(e.getMessage(`Cannot find session with id ${req.params.sessionId} in the existing sessions`));
        } else {
            res.status(500).json(e);
        }
    }
});
router.delete('/:sessionId', (req, res) => {
    try {
        const s = rest_operator.deleteElement(req.params.sessionId);
        res.status(200).json(s);
    } catch(e) {
        if(e.name == 'ItemNotFoundError') {
            res.status(404).json(e.getMessage(`Cannot find session with id ${req.params.sessionId} in the existing sessions`));
        } else {
            res.status(500).json(e);
        }
    }
});
module.exports = router;