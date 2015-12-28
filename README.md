cordova-plugin-socket-scrutinizer

=========

A small library providing utility methods to `create` a socket connection , `listen` to a given port and `receive` all data to that given port.

## Constraint
*The data sent to that given port must be of type string and the plugin will return it as a JSON string.It must be parse back to JSON.
*Support only android.

## Installation
  cordova plugin add cordova-plugin-socket-scrutinizer

## Usage
In the index.html include :
```html
 <script src="SocketConnection.js"></script>
```
below the cordova.js script.


```javascript
var port = 3000; // desired port to listen to

SocketConnection.scrutinize("listen", port, function (data) {
    //onReceive data callback
        var dataFromSocket = JSON.parse(data.response);
        console.log(data.response);
};

```

## Release History

* 0.0.0 Initial release
