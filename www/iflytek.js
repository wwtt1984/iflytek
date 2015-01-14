var cordova = require('cordova'),
    exec = require('cordova/exec');

function handlers() {
  return IflyTek.channels.speakstatus.numHandlers;
}

var IFlyTek = function() {
    
    this.channels = {
      speakstatus:cordova.addWindowEventHandler("speakstatus"),
    };
    for (var key in this.channels) {
        this.channels[key].onHasSubscribersChange = IFlyTek.onHasSubscribersChange;
    }
};

IFlyTek.onHasSubscribersChange = function() {
	
      exec(IflyTek._status, null, "IflyTekPlugin", "StartListen", []);
};

var IflyTek = function () {};
/**
 * 设置提示值
 * @param user
 * @param pwd
 * @returns {*}
 */
IflyTek.prototype.IflyTekInit = function (appId,callback) {
    return cordova.exec(callback, null,"IflyTekPlugin","Init",[appId]);
};

/*
IflyTek.prototype.IflyTekStartListen = function (callback) {
    return cordova.exec(IflyTek._status, null,"IflyTekPlugin","StartListen",[]);
};
*/

IflyTek.prototype.IflyTekStopListen = function (user,pwd,callback) {
    return cordova.exec(callback, null,"IflyTekPlugin","StopListen",[user,pwd]);
};

IflyTek.prototype._status = function (info) {

    if (info) {
		cordova.fireWindowEvent("speakstatus", info);
		IflyTek.info = info;
    }
};

module.exports = (new IflyTek());
