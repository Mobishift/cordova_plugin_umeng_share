package com.mobishift.plugins.umengshare;

import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class echoes a string called from JavaScript.
 */
public class UMengShare extends CordovaPlugin {
    private static final String SHARE = "share";
    private static final String WECHAT_FIREND = "wechat";
    private static final String WECHAT_TIMELINE = "timeline";
    private static final String SINA = "sina";

    private static boolean isInit = false;
    private static boolean shareWechat = false;
    private static boolean shareSina = false;
    private UMSocialService controller;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        boolean result = false;
        controller = UMServiceFactory.getUMSocialService("com.umeng.share");
        init();
        if (action.equals(SHARE)) {
            JSONObject json = new JSONObject(args.getString(0));
            setSharePlatform(json);
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

            CircleShareContent circleShareContent = new CircleShareContent();
            circleShareContent.setShareContent(content);
            circleShareContent.setTitle(title);
            circleShareContent.setShareImage(new UMImage(this.cordova.getActivity(), image));
            circleShareContent.setTargetUrl(url);
            controller.setShareMedia(circleShareContent);
        }

        controller.setShareContent(content);
        controller.setShareImage(new UMImage(this.cordova.getActivity(), image));


        controller.openShare(this.cordova.getActivity(), false);
    }

    private void setSharePlatform(JSONObject jsonObject) {
        if (jsonObject.has("platforms")) {
            ArrayList<String> platforms = new ArrayList<String>();
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("platforms");
                for (int i = 0; i < jsonArray.length(); i++) {
                    platforms.add(jsonArray.getString(i));
                }
            } catch (JSONException ex) {
                Log.e("UMengShare", ex.getMessage());
            }
            if(shareWechat){
                if(platforms.contains(WECHAT_FIREND)){
                    controller.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN);
                }else{
                    controller.getConfig().removePlatform(SHARE_MEDIA.WEIXIN);
                }
                if(platforms.contains(WECHAT_TIMELINE)){
                    controller.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN_CIRCLE);
                }else{
                    controller.getConfig().removePlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                }
            }
            if(shareSina && platforms.contains(SINA)){
                controller.getConfig().setPlatforms(SHARE_MEDIA.SINA);
            }else{
                controller.getConfig().removePlatform(SHARE_MEDIA.SINA);
            }
        }else{
            if(shareWechat){
                controller.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
            }else{
                controller.getConfig().removePlatform(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
            }
            if(shareSina){
                controller.getConfig().setPlatforms(SHARE_MEDIA.SINA);
            }else{
                controller.getConfig().removePlatform(SHARE_MEDIA.SINA);
            }
        }
    }

    private void init(){
        if (!isInit) {
            shareWechat = webView.getPreferences().getBoolean("sharewechat", false);
            String appId = webView.getPreferences().getString("wechatappid", "");
            String appSecret = webView.getPreferences().getString("wechatappsecret", "");
            if (shareWechat && !appId.equals("") && !appId.equals("")) {
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

            shareSina = webView.getPreferences().getBoolean("sharesina", false);
            if(shareSina){
                //TODO:set share sina platform
            }else{
                controller.getConfig().removePlatform(SHARE_MEDIA.SINA);
            }
            controller.getConfig().removePlatform(SHARE_MEDIA.TENCENT);
            isInit = true;
        }
    }

}
