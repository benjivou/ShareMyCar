// Import
const express = require('express');
const fs = require('fs')
const bodyParser = require('body-parser')
const api = require('./api');
const cors = require('cors');
const morgan = require('morgan');

// Création du server
const app = express()

// Configuration du server
const jsonParser = bodyParser.json()
app.use(jsonParser)
app.use(cors())
app.use(morgan('[:date[iso]] :method :url :status :response-time ms - :res[content-length]'))
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