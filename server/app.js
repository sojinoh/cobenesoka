console.log('Deploying server...');

//Load Dependencies
let http = require('http');
let express = require('express');

let app = express();

app.use('/', express.static(__dirname + "/public"));

let server = app.listen(3000, function() {
    let host = server.address().address;
    let port = server.address().port;

    console.log('App listening at http://%s:%s', host, port);
});
