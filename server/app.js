console.log('Deploying server...');

//Load Dependencies
let http = require('http');
let express = require('express');
let socketIO = require('socket.io');

//Setup server and IO objects
let app = express();
let server = http.createServer(app);
let io = socketIO(server);

app.use('/', express.static(__dirname + "/public"));
io.on('connection', function(socket) {
    console.log('connection created');
    console.log(socket.id);
    // console.log(socket.conn);
    // console.log(socket.request);

    socket.on('dog', function(message) {
        console.log('dog: %s', message);
    });

    socket.on('message', function(message) {
        console.log(message);
    })
})
server.listen(3000);

// let server = app.listen(3000, function() {
//     let host = server.address().address;
//     let port = server.address().port;

//     console.log('App listening at http://%s:%s', host, port);
// });
