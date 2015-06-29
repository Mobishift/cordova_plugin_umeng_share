package com.mobishift.plugins.umengshare;

import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class UMengShare extends CordovaPlugin {
    private static final String SHARE = "share";
    private static boolean isInit = false;
    private static boolean shareWechat = false;
    private UMSocialService controller;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        boolean result = false;
        controller = UMServiceFactory.getUMSocialService("com.umeng.share");
        init();
        if (action.equals(SHARE)) {
            JSONObject json = new JSONObject(args.getString(0));
            share(json.getString("title"), json.getString("content"), json.getString("image"), json.getString("url"));
//            String content = args.getString(0);
//            String imageUrl = args.getString(1);
//            String url = args.getString(2);
//            share(content, imageUrl, url);


            result = true;
        }
        return result;
    }

    private void share(String title, String content, String image, String url){
        if(shareWechat){
            WeiXinShareContent weiXinShareContent = new WeiXinShareContent();
            weiXinShareContent.setShareContent(content);
            weiXinShareContent.setTitle(title);
            weiXinShareContent.setShareImage(new UMImage(this.cordova.getActivity(), image));
            weiXinShareContent.setTargetUrl(url);

            controller.setShareMedia(weiXinShareContent);
        }

        controller.setShareContent(content);
        controller.setShareImage(new UMImage(this.cordova.getActivity(), image));


        controller.openShare(this.cordova.getActivity(), false);
    }


    private void init(){
        if (!isInit) {
            String appId = webView.getPreferences().getString("wechatappid", "");
            String appSecret = webView.getPreferences().getString("wechatappsecret", "");
            if (!appId.equals("") && !appId.equals("")) {
                UMWXHandler umwxHandler = new UMWXHandler(this.cordova.getActivity(), appId, appSecret);
                umwxHandler.setToCircle(true);
                umwxHandler.addToSocialSDK();

                umwxHandler = new UMWXHandler(this.cordova.getActivity(), appId, appSecret);
                umwxHandler.addToSocialSDK();
                shareWechat = true;
            }else{
                shareWechat = false;
                controller.getConfig().removePlatform(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
            }
            controller.getConfig().removePlatform(SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
            isInit = true;
        }
    }

}
