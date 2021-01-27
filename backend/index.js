// Import
const express = require('express');
const fs = require('fs')
const bodyParser = require('body-parser')
const api = require('./api');

// Création du server
const app = express()

// Configuration du server
const jsonParser = bodyParser.json()
app.use(jsonParser)
app.use('/api', api)

const hostname = "192.168.1.70"
const port = 8080

// Activation du server
const server = app.listen(port, hostname, () => {
    console.log(`Server listening on port ${hostname}:${port}`)
})

app.get('/', (req, res) => {
    res.redirect('/api')
});