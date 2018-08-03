var express = require('express');
var http = require('http');
var path = require('path');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
//DATABASE
var session = require('express-session');//用戶和伺服器之間的對話資訊
var MongoStore = require('connect-mongo')(session);
//
var app = express();
var router = require('./routes/index');
var port = process.env.PORT || 12345;

app.set('port', port);
//前端模板引擎設定
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');
//
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
//路由
var io = require('socket.io').listen(app.listen(port));
router(app, io);
//
console.log('Express server listening on port ' + app.get('port'));
//
module.exports = app;

