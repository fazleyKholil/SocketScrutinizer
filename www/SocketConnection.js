var exec = require('cordova/exec');

function SocketConnection() {
    console.log("SocketConnection.js: is created");
}

SocketConnection.prototype.scrutinize = function (action, ip, callback) {
    var args = [ip];
    exec(function (result) {
        callback(result);
    }, function (result) {
        alert(JSON.stringify(result));
    }, "SocketConnection", action, args);
};

var SocketConnection = new SocketConnection();
module.exports = SocketConnection;
