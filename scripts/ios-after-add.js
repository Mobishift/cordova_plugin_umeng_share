#!/usr/bin/env node
'use strict';

exports.module = function(context){
    // console.info('init umeng share plugin ...');

    var path = context.requireCordovaModule('path'),
        fs = context.requireCordovaModule('fs'),
        projectRoot = context.opts.projectRoot,
        ConfigParser = context.requireCordovaModule('cordova-lib').configparser,
        config = new ConfigParser(path.join(projectRoot, 'config.xml'));

    var pluginXml = path.join(projectRoot, 'plugins', 'com.mobishift.plugins.umengshare', 'plugin.xml');
    var content = fs.file.readFileSync(pluginXml, {encoding: 'utf8'});
    if(!config.getPreference('sharewechat') && content.indexOf('WX_APP_ID') >= 0){
        console.info('remove wechat share');
        content = content.replace('<config-file target="\*/\*-Info.plist" parent="CFBundleURLTypes">\s*<array>\s*<dict>\s*<key>CFBundleURLName</key>\s*<string>weixin</string>\s*<key>CFBundleURLSchemes</key>\s*<array>\s*<string>WX_APP_ID</string>\s*</array>\s*</dict>\s*</array>\s*</config-file>', '');
    }

    if(!config.getPreference('sharesina') && content.indexOf('SINA_APP_KEY') >= 0){
        console.info('remove sina share');
        content = content.replace('<config-file target="\*/\*-Info.plist" parent="CFBundleURLTypes">\s*<array>\s*<dict>\s*<key>CFBundleURLName</key>\s*<string>umeng_sina</string>\s*<key>CFBundleURLSchemes</key>\s*<array>\s*<string>SINA_APP_KEY</string>\s*</array>\s*</dict>\s*</array>\s*</config-file>', '');
    }

    fs.file.writeFileSync(pluginXml, content);
};
