var express = require('express');
var router = express.Router();

module.exports = (app, io) => {
    app.get('/', (req, res)=>{
        
    })

    app.post('/', (req, res)=>{
        var msg = req.body.message;
        console.log(msg);
        res.send("[NodeJS Server] : Server gets the message .");
    })
};