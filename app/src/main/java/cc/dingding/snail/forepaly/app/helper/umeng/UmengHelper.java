package cc.dingding.snail.forepaly.app.helper.umeng;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;

import cc.dingding.snail.forepaly.app.models.CaseModel;

/**
* Created by koudejian on 14-8-4.
*/
public class UmengHelper {

    private UMSocialService mController;
    private static UmengHelper INSTANCE = null;
    private UmengHelper(){
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    }

    public static UmengHelper getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UmengHelper();
        }
        return INSTANCE;
    }
    public UMSocialService getController(){
        return mController;
    }

    public void share(Context context, CaseModel caseModel, Bitmap bitmap){
        /*
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appID = "wx967daebe835fbeac";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mContext, appID);
        wxHandler.addToSocialSDK();
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mContext, appID);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        //*/
        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context, "1102468636", "2DU4bA3A5ibkLFBJ");
        qqSsoHandler.addToSocialSDK();
        //设置新浪SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        //设置腾讯微博SSO handler
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

        mController.getConfig().removePlatform( SHARE_MEDIA.RENREN , SHARE_MEDIA.EMAIL,  SHARE_MEDIA.SMS);

//        mController.getConfig().supportAppPlatform(mContext, SHARE_MEDIA.WEIXIN, appID, true);
//        mController.getConfig().supportAppPlatform(mContext, SHARE_MEDIA.WEIXIN_CIRCLE, appID, true);
//        mController.getConfig().supportWXPlatform(this, appID, contentUrl);
//        //支持微信朋友圈
//        mController.getConfig().supportWXCirclePlatform(this, appID, contentUrl);
        //设置分享内容
        mController.setShareContent(caseModel.getName());
        mController.setShareMedia(new UMImage(context, bitmap));

        mController.openShare((Activity)context, false);
    }
}
