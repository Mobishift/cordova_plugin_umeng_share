#!/usr/bin/env node
'use strict';

module.exports = function(context){
    if(context.opts.cordova.platforms.indexOf('ios') === -1){
        console.info('IOS platform has not been added.');
        return;
    }

    console.info('setting umeng share plugin ...');
    var path = context.requireCordovaModule('path'),
        fs = context.requireCordovaModule('fs'),
        projectRoot = context.opts.projectRoot,
        config = new ConfigParser(path.join(projectRoot, 'config.xml')),
        appName = config.name() || 'CordovaApp';

    var targetFile = path.join(projectRoot, 'platforms', 'ios', appName, 'Classes', 'AppDelegate.m');

    var content = fs.readFileSync(targetFile, {encoding: 'utf8'});
    if(content.indexOf('UMSocialSnsService.h') === -1){
        content = content.replace('#import <Cordova/CDVPlugin.h>', '#import <Cordova/CDVPlugin.h>\n#import "UMSocialSnsService.h"');

        content = content.substring(0, content.lastIndexOf('@end'));
        content += '\n- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url{\n    return [UMSocialSnsService handleOpenURL:url];\n}\n\n@end';

        fs.writeFileSync(targetFile, content);
    }

};
