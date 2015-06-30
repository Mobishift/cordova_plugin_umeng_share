#!/usr/bin/env node
'use strict';

module.exports = function(context){
    if(context.opts.cordova.platforms.indexOf('android') === -1){
        console.info('Android platform has not been added.');
        return;
    }

    console.info('setting umeng share plugin ...');

    var path = context.requireCordovaModule('path'),
        fs = context.requireCordovaModule('fs'),
        projectRoot = context.opts.projectRoot,
        ConfigParser = context.requireCordovaModule('cordova-lib').configparser,
        config = new ConfigParser(path.join(projectRoot, 'config.xml'));


    var libsDir = path.join(projectRoot, 'platforms', 'android', 'libs');
    var files = fs.readdirSync(libsDir);

    for(var i = 0; i < files.length; i++){
        if(files.indexOf('libammsdk') >= 0){
            var file = path.join(libsDir, files[i]);
            fs.unlinkSync(file);
        }
    }
};
