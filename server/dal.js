module.exports = {
    getRecentLog : getRecentLog,
    logCall : logCall
}

const mysql = require('mysql');
const util = require('util');

let conn = mysql.createConnection({
    host : 'localhost',
    user : 'root',
    password : '',
    database : 'cobenesoka' 
});

conn.connect((err) => {
    if(err) {    
        console.log('unable to connect to database...');
    } else {
        console.log('connected to database');
    }
})

const logCallQuery = "SELECT log_received_call('%s', '%s', '%s')";
const getRecentLogQuery = "CALL get_recent_log('%s')";

function getRecentLog(phoneNumber, callback) {
    console.log(phoneNumber);
    
    let query = util.format(getRecentLogQuery, phoneNumber);
    
    conn.query(query, (err, rows, fields) => {
        if(err) {
            console.log('error');
            callback(true, undefined, undefined, undefined);
        } else {
            let row = rows[0][0];
            if(row == undefined) {
                callback(false, undefined, undefined, undefined);
                return;
            }
            if (row['time_received'] === null) {
                callback(false, undefined, undefined, undefined);
            } else {
                callback(false, row['time_received'], row['latitude'], row['longitude']);
            }
        }
    });
}

function logCall(phoneNumber, latitude, longitude, callback) {
    let query = util.format(logCallQuery, phoneNumber, latitude, longitude);

    conn.query(query, (err, rows, fields) => {
        if(err) {
            console.log('error logging number: %s', err);
            callback(true);
        } else {
            callback(false);
        }
    });
}