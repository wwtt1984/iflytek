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

IflyTek.prototype.IflyTekStartListen = function (callback) {
    return cordova.exec(callback, null,"IflyTekPlugin","StartListen",null);
};

IflyTek.prototype.IflyTekStopListen = function (user,pwd,callback) {
    return cordova.exec(callback, null,"IflyTekPlugin","StopListen",[user,pwd]);
};


module.exports = (new IflyTek());
