const express = require('express');
const fs = require('fs')
const bodyParser = require('body-parser')

const app = express()

const jsonParser = bodyParser.json()

app.use(jsonParser)

const hostname = "localhost"
const port = 8080

const server = app.listen(port, hostname, () => {
    console.log(`Server listening on port ${hostname}:${port}`)
})