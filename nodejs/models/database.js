var mysql = require('mysql');
var conn = mysql.createConnection({
    host: "localhost",
    user: "jackLorentz",
    password: "jack843971",
    database: "mountain_guide"
});

console.log("MySQL server is connected !");

function DB(db){
    this.table = db.table;
};

module.exports = DB;

DB.prototype.showAll = (target, callback)=>{
    var sql = "SELECT * FROM " + target.table;
    conn.query(sql, (err, result, fields)=>{
        if(err) throw err;
        console.log(result);
        callback(err, result);            
    });
}


