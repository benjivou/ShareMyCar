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

const hostname = "localhost"
const port = 8080

// Activation du server
const server = app.listen(port,  () => {
    console.log(`Server listening on port ${hostname}:${port}`)
})

app.get('/', (req, res) => {
    res.redirect('/api')
});