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
        socket.emit('dog');
    }
}

function sendCat() {
    if(socket === undefined) {
        console.log('error no connection');
    } else {
        socket.emit('cat lol');
    }
}

window.onload = setup;