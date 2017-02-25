function setup() {
    $('#header').click(youClickedDog);
}

function youClickedDog() {
    alert('YOU CLICKED ON THE DOG!!!');
}

window.onload = setup;