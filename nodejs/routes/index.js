var express = require('express');
var router = express.Router();
DB = require('../models/database.js');

module.exports = (app, io) => {
    app.get('/', (req, res)=>{
        
    })

    app.post('/', (req, res)=>{
        var msg = req.body.message;
        console.log(msg);
        res.send("[NodeJS Server] : Server gets the message .");
    })

    app.post('/showRoute', (req, res)=>{
        var table = req.body.table;
        var target = new DB({
            table: table
        });
        console.log(target);
        target.showAll(target, (err, result)=>{
            res.send("[NodeJS Server] : " + JSON.stringify(result));
        })
    })
};