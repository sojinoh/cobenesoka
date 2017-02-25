function setup() {
    $('#header').click(youClickedDog);
    $('#btnConnect').click(testConnect);
    $('#btnSendDog').click(sendDog);
    $('#btnSendCat').click(sendCat);
}

let socket;
function youClickedDog() {
    alert('YOU CLICKED ON THE DOG!!!');
}

function testConnect() {
    console.log('establishing socket connection');
    socket = io.connect();
    socket.emit('dog');
}

function sendDog() {
    if(socket === undefined) {
        console.log('error no connection');
    } else {
        socket.emit('dog', 'fart');
    }
}

function sendCat() {
    if(socket === undefined) {
        console.log('error no connection');
    } else {
        socket.send('cat lol');
    }
}

window.onload = setup;