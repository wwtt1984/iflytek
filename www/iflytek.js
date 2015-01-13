﻿var IflyTek = function () {};
/**
 * 设置提示值
 * @param user
 * @param pwd
 * @returns {*}
 */
IflyTek.prototype.IflyTekInit = function (appId,callback) {
    return cordova.exec(callback, null,"IflyTekPlugin","Init",[appId]);
};

IflyTek.prototype.IflyTekStartListen = function (ip,callback) {
    return cordova.exec(callback, null,"IflyTekPlugin","StartListen",[ip]);
};

IflyTek.prototype.IflyTekStopListen = function (user,pwd,callback) {
    return cordova.exec(callback, null,"IflyTekPlugin","StopListen",[user,pwd]);
};


module.exports = (new IflyTek());
