/********* UMengShare.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import "UMSocial.h"

static BOOL isInit = false;

@interface UMengShare : CDVPlugin {
  // Member variables go here.
}

- (void)share:(CDVInvokedUrlCommand*)command;
@end

@implementation UMengShare

- (void)share:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* echo = [command.arguments objectAtIndex:0];

    if (echo != nil && [echo length] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:echo];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)initShare{
    if(!isInit){
        
    }
}

@end
