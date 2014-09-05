package cc.dingding.snail.forepaly.app.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.activitys.LoginActivity;
import cc.dingding.snail.forepaly.app.cache.SharedCache;
import cc.dingding.snail.forepaly.app.config.JsonConfig;
import cc.dingding.snail.forepaly.app.config.UrlConfig;
import cc.dingding.snail.forepaly.app.models.CaseModel;
import cc.dingding.snail.forepaly.app.network.PostDataTask;
import cc.dingding.snail.forepaly.app.network.PostParameter;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.views.CustomDialog;
import cc.dingding.snail.forepaly.app.views.FavoriteView;

/**
 * Created by koudejian on 14-8-6.
 */
public class DetailFooterController extends BaseController {

    private View mParentView = null;

    private View mIvFavorite = null;
    private View mIvDownload = null;
    private View mIvShare = null;

    private FavoriteView mFavoriteView = null;

    private CustomDialog mCustomDialog = null;


    private CaseModel mCaseModel = null;

    private boolean mIsAvailable = true;        //控制footer显示消失
    // 首先在您的Activity中添加如下成员变量
    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    public DetailFooterController(Context context, View view) {
        super(context, view);
        init();
    }

    @Override
    void init() {
        mParentView = mView.findViewById(R.id.footer_bar);

        mIvFavorite = mView.findViewById(R.id.favorite_rl);
        mIvDownload = mView.findViewById(R.id.download_rl);
        mIvShare = mView.findViewById(R.id.share_rl);


        mCaseModel = SharedCache.gCurrentCase;

        mFavoriteView = (FavoriteView) mView.findViewById(R.id.favorite);
        mFavoriteView.setFrovite(mCaseModel.getIsfavorite());
        mFavoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = MainApplication.gUser.getUid();
                if("0".equals(uid)){//login
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }else{
                    if(mCaseModel != null){
                        setFavorite(mCaseModel.getId(), mFavoriteView);
                    }
                }
            }
        });

        mIvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("test", "on download");
            }
        });

        mIvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shared();
            }
        });
    }

    /**
     * 动画渐隐
     */
    public void show(){
        if(!mIsAvailable){
            return;
        }
        mIsAvailable = false;
        mParentView.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hide();
            }
        }, 3000);
    }
    private void hide(){
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_xml);
        mParentView.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mParentView.setVisibility(View.GONE);
                mIsAvailable = true;
            }
        }, 1000);
    }


    /**
     * 设置收藏，取消收藏
     * @param cid
     * @param favoriteView
     */
    private void setFavorite(String cid, final FavoriteView favoriteView){
        if(DeviceUtils.isNetworkConnected(mContext)){
            // post 参数
            PostParameter param = new PostParameter();
            param.add("uid", MainApplication.gUser.getUid());
            param.add("cid", cid);

            String url = (favoriteView.getValue()) ? UrlConfig.USER_REMOVE_FAVORITE : UrlConfig.USER_SET_FAVORITE;
            PostDataTask postDataTask = new PostDataTask(url, param) {
                @Override
                public void dealWithResult(String request) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(request);
                        String status = jsonObject.getString(JsonConfig.KEY_STATUS);
                        if("0".equals(status)){//success
                            popMessage("操作成功！");
                            favoriteView.setFrovite(!favoriteView.getValue());
                        }else{
                            popMessage("操作失败！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mCustomDialog.cancel();
                }
            };
            if(mCustomDialog == null){
                mCustomDialog = new CustomDialog(mContext, CustomDialog.DIALOG_THEME_WAIT_NOT_CANCEL);
            }
            mCustomDialog.show();
            postDataTask.execute();
        }else{
            popMessage("暂无网络链接!");
        }
    }

    public void popMessage(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    private void shared(){
        String contentUrl = "http://www.umeng.com/social";
        // 设置分享内容
        mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
        // 设置分享图片, 参数2为图片的url地址
        mController.setShareMedia(new UMImage(mContext, "http:\\/\\/foreplay.oss-cn-hangzhou.aliyuncs.com\\/e37a4b705caa43b7f9e2554c85a46907.png"));


        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appID = "wx967daebe835fbeac";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler((Activity) mContext, appID);
        wxHandler.addToSocialSDK();
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler((Activity) mContext, appID);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) mContext, "100424468", "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.addToSocialSDK();

        //设置新浪SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

//        mController.getConfig().removePlatform( SHARE_MEDIA.RENREN , SHARE_MEDIA.EMAIL,  SHARE_MEDIA.SMS);

//        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA, SHARE_MEDIA.DOUBAN);

//        mController.getConfig().supportWXPlatform(this, appID, contentUrl);
//        // 支持微信朋友圈
//        mController.getConfig().supportWXCirclePlatform(this, appID, contentUrl);

        // 设置分享图片，参数2为本地图片的资源引用
        //mController.setShareMedia(new UMImage(getActivity(), R.drawable.icon));
        // 设置分享图片，参数2为本地图片的路径(绝对路径)
        //mController.setShareMedia(new UMImage(getActivity(), BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));


        mController.openShare((Activity)mContext, false);
    }
}
