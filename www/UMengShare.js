'use strict';
var exec = require('cordova/exec');

var noop = function(){};

var execPlugin = function(action, arg0, success, error) {
    exec(success, error, 'UMengShare', action, [arg0]);
};

exports.share = function(shareData, successCallback, errorCallback){
    successCallback = successCallback || noop;
    errorCallback = errorCallback || noop;
    if(!shareData){
        throw 'please input share data';
    }
    execPlugin('share', JSON.stringify(shareData), successCallback, errorCallback);
};
