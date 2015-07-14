/********* UMengShare.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import "UMSocial.h"
#import "UMSocialWechatHandler.h"
#import "UMSocialSinaSSOHandler.h"

static BOOL isInit = false;

@interface UMengShare : CDVPlugin <UMSocialUIDelegate>{
  // Member variables go here.
}

- (void)share:(CDVInvokedUrlCommand*)command;
@end

@implementation UMengShare

- (void)share:(CDVInvokedUrlCommand*)command
{
    [self initShare];
    CDVPluginResult* pluginResult = nil;
    NSString* echo = [command.arguments objectAtIndex:0];
    NSData* shareData = [echo dataUsingEncoding:NSUTF8StringEncoding];
    NSDictionary* shareModel = [NSJSONSerialization JSONObjectWithData:shareData options:NSJSONReadingMutableLeaves error:nil];

    CDVViewController* viewController = (CDVViewController*)self.viewController;
    NSString* wechatAppId = [viewController.settings objectForKey:@"wechatappid"];
    NSString* wechatAppSecret = [viewController.settings objectForKey:@"wechatappsecret"];

    NSMutableArray* shareNames = [NSMutableArray array];
    if(wechatAppId != nil && wechatAppSecret != nil){
        [UMSocialWechatHandler setWXAppId:wechatAppId appSecret:wechatAppSecret url:[shareModel objectForKey:@"url"]];
        [shareNames addObject:UMShareToWechatSession];
        [shareNames addObject:UMShareToWechatTimeline];
    }

    NSString* imageUrl = [shareModel objectForKey:@"image"];
    UIImage* image = nil;
    if(imageUrl != nil){
        image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:imageUrl]]];

    }
    [UMSocialSnsService presentSnsIconSheetView:viewController
                                         appKey:[viewController.settings objectForKey:@"umengappkey"]
                                      shareText:[shareModel objectForKey:@"title"]
                                     shareImage:image
                                shareToSnsNames:shareNames
                                       delegate:self];


    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)initShare{
    if(!isInit){
        CDVViewController* viewController = (CDVViewController*)self.viewController;
        [UMSocialData setAppKey:[viewController.settings objectForKey:@"umengappkey"]];

        [UMSocialSinaSSOHandler openNewSinaSSOWithRedirectURL:@"http://sns.whalecloud.com/sina2/callback"];
    }
}

- (void)didFinishGetUMSocialDataInViewController:(UMSocialResponseEntity *)response{
    if(response.responseCode == UMSResponseCodeSuccess){
         NSLog(@"share to sns name is %@",[[response.data allKeys] objectAtIndex:0]);
    }
}

@end
