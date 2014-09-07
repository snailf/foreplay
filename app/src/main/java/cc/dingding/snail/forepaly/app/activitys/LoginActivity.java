package cc.dingding.snail.forepaly.app.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMSsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.config.JsonConfig;
import cc.dingding.snail.forepaly.app.config.UrlConfig;
import cc.dingding.snail.forepaly.app.models.UserModel;
import cc.dingding.snail.forepaly.app.network.PostDataTask;
import cc.dingding.snail.forepaly.app.network.PostParameter;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.views.CustomDialog;

/**
 * Created by snail on 8/1/14.
 */
public class LoginActivity extends BaseActivity {
    private final String TAG = "LoginActivity";
    private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    private TextView mTvQQ = null;
    private TextView mTvSina = null;
    private TextView mTvDouban = null;

    private Context mContext = null;

    private CustomDialog mCustomDialog = null;

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mController == null){
                return;
            }
            SHARE_MEDIA share_media = null;
            String type = "";
            if(v == mTvDouban){
                share_media = SHARE_MEDIA.DOUBAN;
                type = "3";
            }else if(v == mTvSina){
                share_media = SHARE_MEDIA.SINA;
                type = "2";
            }else{
                share_media = SHARE_MEDIA.TENCENT;
                type = "1";
            }
            final String finalType = type;
            mController.doOauthVerify(mContext, share_media, new SocializeListeners.UMAuthListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {
                    Log.e("test", "onStart");
                }
                @Override
                public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
                    Log.e("test", "onComplete");
                    final String openid = bundle.getString("uid");
                    mController.getPlatformInfo(mContext, share_media, new SocializeListeners.UMDataListener() {
                        @Override
                        public void onStart() {
                            Log.e("test", "获取平台数据开始...");
                        }
                        @Override
                        public void onComplete(int status, Map<String, Object> info) {
                            if(status == 200 && info != null){
                                final String avatar = info.get("profile_image_url").toString();
                                final String nick = info.get("screen_name").toString();
                                regist2login(openid, nick, avatar, finalType);
                            }else{
                                Log.e("TestData","发生错误："+status);
                            }
                        }
                    });
                }
                @Override
                public void onError(SocializeException e, SHARE_MEDIA share_media) {
                    Log.e("test", "onError");
                }
                @Override
                public void onCancel(SHARE_MEDIA share_media) {
                    Log.e("test", "onCancel");
                }
            });

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        mController.getConfig().setPlatforms(SHARE_MEDIA.TENCENT, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.SINA);

        /**
         * 登录button
         */
        mTvDouban = (TextView) findViewById(R.id.douban);
        mTvDouban.setOnClickListener(mListener);
        mTvSina = (TextView) findViewById(R.id.weibo);
        mTvSina.setOnClickListener(mListener);
        mTvQQ = (TextView) findViewById(R.id.qq);
        mTvQQ.setOnClickListener(mListener);

    }

    /**
     * 回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void regist2login(final String openid, final String nick, final String avatar, String type){
        if(DeviceUtils.isNetworkConnected(mContext)){
            // post 参数
            PostParameter param = new PostParameter();
            param.add("openid", openid);
            param.add("type", type);
            param.add("avatar", avatar);
            param.add("nick", nick);
            PostDataTask postDataTask = new PostDataTask(UrlConfig.USER_LOGIN_URL, param) {
                @Override
                public void dealWithResult(String request) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(request);
                        String status = jsonObject.getString(JsonConfig.KEY_STATUS);
                        if("0".equals(status)){//success
                            String data = jsonObject.getString(JsonConfig.KEY_DATA);
                            JSONObject obj = new JSONObject(data);
                            String uid = obj.getString(JsonConfig.KEY_LOGIN_ID);
                            MainApplication.login(new UserModel(openid, uid, nick, avatar));
                            LoginActivity.this.finish();
                        }else{
                            popMessage("登录失败！");
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

    @Override
    public void finish() {
        if(MainApplication.isLogin()){//获取用户数据
            MainApplication.getInstance().getUserUpdateCounts();
        }
        super.finish();
    }
}
