console.log('deploying server...');

//Load Dependencies
const http = require('http');
const fs = require('fs');
const dal = require('./dal');
let server = http.createServer(requestHandler);

let faviconData;
let headerData;
let footerData;

let errorData = 'Unable to complete request :(';

const emergencyTransmissionRequired = ['location', 'phone_number'];

loadStaticFiles();
server.listen(3002);

function requestHandler(req, res) {
    if(req.method == "GET") {
        handleGetRequest(req, res);
    } else if (req.method = "POST") {
        handlePostRequest(req, res);
    } else {
        replyUnsupportedMethod(res, "bad method: " + req.method);
    }
}

function handleGetRequest(req, res) {
    let url = req.url;
    if(url === '/') {

    }else if(url === '/favicon.ico') {
        replyFavicon(req, res);
    } else {
        let number = numberFromURL(url);
        replyWithNumberInformation(req, res, number);
    }
}

function handlePostRequest(req, res) {
        let body = '';
        req.on('data', (chunk) => {
            body += chunk;
        });

        req.on('end', () => {
            let data;
            try {
                data = JSON.parse(body);
            } catch (err) {
                console.log(err);
                
                replyUnableToParseData(res, err);
                return;
            }

            let url = req.url;
            if(url === '/emergencyTransmission') {
                console.log('got a call man');
                
                logCallRequest(req, res, data);
            } else {
                console.log('got something else');
                
                replyUnsupportedPostURL(res, 'bad url: ' + res.url);
            }
        });
}

function logCallRequest(req, res, data) {
    if(!isRequiredSet(data, emergencyTransmissionRequired)) {
        replyMissingParameters(res)
    }

    let numberOnlyDigits = '';
    let phoneNumber = data['phone_number'];
    for(let i=0; i<phoneNumber.length; i++) {
        if(isNumeric(phoneNumber.charAt(i))) {
            numberOnlyDigits += phoneNumber.charAt(i);
        }
    }
    dal.logCall(numberOnlyDigits, data['location']['latitude'], data['location']['longitude'], (err) => {
        if(err) {
            replyWithError(res, err);
        } else {
            replySuccessful(res, 'call logged');
        }
    });
}

function replyFavicon(req, res) {
    if(faviconData === undefined) {
        res.statusCode = 404;
        res.end('');
    } else {
        res.statusCode = 200;
        res.end(faviconData); 
    }
}

function replyWithNumberInformation(req, res, number) {
    dal.getRecentLog(number, (err, call_time, latitude, longitude) => {
        let middleData = '<div id="phoneNumber">Location data for: ' + formatPhoneNumber(number) + '</div>';
        let scriptData = "<script>var latitude=0; var longitude = 0;</script>"

        console.log(err + ' ' + call_time + ' ' + latitude + ' ' + longitude);
        
        if(err) {
            console.log('unable to load recent logs for %s', number);
            res.statusCode = 500;
            middleData = errorData;
        } else {
            res.statusCode = 200;
            if(call_time === undefined) {
                middleData += '<div id="noRecent">No recent record for that number.</div>'
            } else {
                middleData += '<table><tr><td>Time Called:</td><td>' + call_time.toString() + '</td></tr>' +
                              '<tr><td>Latitude:</td><td>' + latitude + '</td></tr>' +
                              '<tr><td>Longitude:</td><td>' + longitude + '</td></tr></table>';
                // middleData += '<div id="timeCalled">Time Called: ' + call_time.toString() + "</div>" +
                //              '<div id="latitude">Latitude: '  + latitude + '</div>' + 
                //              '<div id="longitude">Longitude: ' + longitude + '</div>';
                scriptData = '<script type="text/javascript"> var latitude = "' + latitude + '"; var longitude = "' + longitude + '"; </script>';
            }
        }
        replyWithData(req, res, middleData + scriptData);
    });
}

function replyAbleToLog(req, res, phoneNumber, latitude, longitude) {
    dal.logCall(phoneNumber, latitude, longitude, (err) => {
        let data;
        if(err) {
            res.statusCode = 500;
            data = {'success': false }
        } else {
            res.statusCode = 200;
            data = {'success': true }
        }
        res.end(JSON.stringify(data));
    });
}
function replyWithData(req, res, middleData) {
    res.write(headerData);
    res.write(middleData);
    res.write(footerData);
    res.end();
}

function numberFromURL(url) {
    let number = '';
    for(let i=0; i<url.length; i++) {
        if(isNumeric(url.charAt(i))) {
            number += url.charAt(i);
        }
    }

    return number;
}

//From jQuery source
function isNumeric(obj){
    return !isNaN(obj - parseFloat(obj));
}

function loadStaticFiles() {
    fs.readFile('./favicon.ico', (err, data) => {
        if(err) {
            console.log('unable to load favicon');
        } else {
            faviconData = data;
        }
    });

    fs.readFile('./header.html', (err, data) => {
        if(err) {
            console.log('unable to load: ' + 'header');
        } else {
            headerData = data;
        }
    });

    fs.readFile('./footer.html', (err, data) => {
        if(err) {
            console.log('unable to load: ' + 'foooter');
        } else {
            footerData = data;
        }
    });
}

function formatPhoneNumber(phoneNumber) {
    if(phoneNumber.length == 11) {
        phoneNumber = phoneNumber.substring(0, 1) + '-' + phoneNumber.substring(1, 4) + '-' + phoneNumber.substring(4, 7) + '-' + phoneNumber.substring(7)
    } else if(phoneNumber.length == 10) {
        phoneNumber = phoneNumber.substring(0, 3) + '-' + phoneNumber.substring(3, 6) + '-' + phoneNumber.substring(6);
    }
    return phoneNumber;
}
function replySuccessful(res, message) {
    res.statusCode = 200;
    let data = {};
    data['success'] = true;
    data['message'] = message;
    setHeaderJson(res);
    res.end(JSON.stringify(data));
}
function replyWithError(res, errorMsg, error, errorCode) {
    console.log('replying error ' + errorMsg);
    if (error == undefined) {
        error = '';
    }

    if(errorCode == undefined) {
        errorCode = 400;
    }

    var resData = {};
    resData['success'] = false;
    resData['errorMsg'] = errorMsg;
    resData['error'] = error;
    
    res.statusCode = errorCode;
    setHeaderJson(res);
    res.end(JSON.stringify(resData));
}

function replyUnableToParseData(res, error) {
    replyWithError(res, "unable to parse data", error);
}

function replyUnsupportedPostURL(res, error) {
    replyWithError(res, "unsupported post url", error);
}

function replyMissingInputs(res, error) {
    replyWithError(res, "missing inputs", error);
}

function replyUnsupportedMethod(res, error) {
    replyWithError(res, "unsupported method", error);
}

function isRequiredSet(data, required) {
	for(var i =0; i < required.length; i++) {
		if(data[required[i]] === undefined) {
			return false;
		}
	}
	return true; 
} 

function setHeaderJson(res) {
    res.setHeader('content-type', 'text/json');
}