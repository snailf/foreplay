//package cc.dingding.snail.forepaly.app.helper.umeng;
//
//import android.app.Activity;
//import android.content.Context;
//
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.controller.RequestType;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.sso.SinaSsoHandler;
//
///**
// * Created by koudejian on 14-8-4.
// */
//public class UmengHelper {
//
//    private Context mContext = null;
//    private UMSocialService mController;
//
//    public UmengHelper(Context context){
//        this.mContext = context;
//        initShareWeChat();
//    }
//    private void initShareWeChat() {
//
//        String title = "tile";
//        String sumary = "sumary";
//        String url = "hh";
//        String imageurl = "";
//
//        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//        String appID = UmengConfig.WECHATAPPID;
//        // 微信图文分享必须设置一个url
//        String contentUrl = "j";
//        // 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
//        mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
//
//        mController.getConfig().removePlatform(SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN, SHARE_MEDIA.TENCENT,
//                SHARE_MEDIA.EMAIL, SHARE_MEDIA.QZONE, SHARE_MEDIA.SMS);
//
//        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA);
//
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        mController.getConfig().supportWXPlatform(mContext, appID, contentUrl);
//        // 支持微信朋友圈
//        mController.getConfig().supportWXCirclePlatform(mContext, appID, contentUrl);
//        // 设置分享文字
//        mController.setShareContent("【" + title + "】" + sumary + url);
//        // 设置分享图片, 参数2为图片的地址
//        if (imageurl != null && imageurl.length() > 10)
//            mController.setShareMedia(new UMImage(mContext, imageurl));
//        mController.openShare((Activity)mContext, false);
////     // 设置分享到微信的内容, 图片类型
////        UMImage mUMImgBitmap = new UMImage(getActivity(),
////                        "http://www.umeng.com/images/pic/banner_module_social.png");
////        WeiXinShareContent weixinContent = new WeiXinShareContent(mUMImgBitmap);
////        weixinContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，微信");
////        mController.setShareMedia(weixinContent);
////
////        // 设置朋友圈分享的内容
////        CircleShareContent circleMedia = new CircleShareContent(new UMImage(getActivity(),
////                        "http://www.umeng.com/images/pic/social/chart_1.png"));
////        circleMedia.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，朋友圈");
////        mController.setShareMedia(circleMedia);
////
////
////        // 视频分享，并且设置了腾讯微博平台的文字内容
////        UMVideo umVedio = new UMVideo(
////                        "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
////        umVedio.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
////        umVedio.setTitle("友盟社会化组件视频");
////        TencentWbShareContent tencentContent = new TencentWbShareContent(umVedio);
////        // 设置分享到腾讯微博的文字内容
////        tencentContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，腾讯微博");
////        // 设置分享到腾讯微博的多媒体内容
////        mController.setShareMedia(tencentContent);
////
////        // ** 其他平台的分享内容.除了上文中已单独设置了分享内容的微信、朋友圈、腾讯微博平台，
////        // 剩下的其他平台的分享内容都为如下文字和UMImage  **
////        mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
////        // 设置分享图片，参数2为图片的url.
////        mController.setShareMedia(new UMImage(getActivity(),
////                                        "http://www.umeng.com/images/pic/banner_module_social.png"));
//
//    }
//}
