package cc.dingding.snail.forepaly.app;

import android.app.Application;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import cc.dingding.snail.forepaly.app.config.Config;
import cc.dingding.snail.forepaly.app.config.JsonConfig;
import cc.dingding.snail.forepaly.app.config.UrlConfig;
import cc.dingding.snail.forepaly.app.models.UserModel;
import cc.dingding.snail.forepaly.app.network.PostDataTask;
import cc.dingding.snail.forepaly.app.network.PostParameter;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;

/**
 * Created by koudejian on 14-7-29.
 */
public class MainApplication extends Application {
    public static int CURRENT = 0;
    private static MainApplication INSTANCE = null;
    public static String gTag = "0";

    private boolean isUpdate = false;

    public static UserModel gUser = new UserModel();
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        initUserInfo();
        if(!"0".equals(gUser.getUid())){
            getUserUpdateCounts();
//            clearUserHistory();
            uploadUserHistory();
        }
    }

    public static MainApplication getInstance(){
        return INSTANCE;
    }

    /**
     * 登录信息
     * @param user
     */
    public static void login(UserModel user) {
        gUser = user;
        // 同步存储本地
        SharedPreferences sp = MainApplication.getInstance().getSharedPreferences(Config.PREFS_NAME, 0);
        sp.edit().putString("oid", user.getOpenid()).commit();
        sp.edit().putString("uid", user.getUid()).commit();
        sp.edit().putString("nick", user.getNick()).commit();
        sp.edit().putString("avatar", user.getAvatar()).commit();
    }
    public static void logout(){
        gUser.clear();
        // 同步存储本地
        SharedPreferences sp = MainApplication.getInstance().getSharedPreferences(Config.PREFS_NAME, 0);
        sp.edit().putString("oid", gUser.getOpenid()).commit();
        sp.edit().putString("uid", gUser.getUid()).commit();
        sp.edit().putString("nick", gUser.getNick()).commit();
        sp.edit().putString("avatar", gUser.getAvatar()).commit();
        sp.edit().putString("history_counts", gUser.getHistoryCounts()).commit();
        sp.edit().putString("favorite_counts", gUser.getFavoriteCounts()).commit();
        sp.edit().putString("comments_counts", gUser.getCommentscounts()).commit();
    }

    /**
     * 用户登录
     */
    private void initUserInfo() {
        SharedPreferences sp = getSharedPreferences(Config.PREFS_NAME, 0);
        String oid = sp.getString("oid", "");
        String uid = sp.getString("uid", "0");
        String nick = sp.getString("nick", "");
        String avatar = sp.getString("avatar", "");

        String historyCounts = sp.getString("history_counts", "0");
        String favoriteCounts = sp.getString("favorite_counts", "0");
        String commentsCounts = sp.getString("comments_counts", "0");
        this.gUser = new UserModel(oid, uid, nick, avatar, historyCounts, favoriteCounts, commentsCounts);
    }
    private void storageUserCounts(String historyCounts, String favoriteCounts, String commentsCounts){
        // 同步存储本地
        SharedPreferences sp = MainApplication.getInstance().getSharedPreferences(Config.PREFS_NAME, 0);
        sp.edit().putString("history_counts", historyCounts).commit();
        sp.edit().putString("favorite_counts", favoriteCounts).commit();
        sp.edit().putString("comments_counts", commentsCounts).commit();
        //更新
        gUser.setHistoryCounts(historyCounts);
        gUser.setFavoriteCounts(favoriteCounts);
        gUser.setCommentscounts(commentsCounts);
    }
    public void getUserUpdateCounts(){
        if(!isLogin()){
            return;
        }
        if(DeviceUtils.isNetworkConnected(this)){
            // post 参数
            PostParameter param = new PostParameter();

            param.add("uid", gUser.getUid());

            PostDataTask postDataTask = new PostDataTask(UrlConfig.Get_USR_UPDATE_COUNTS, param) {
                @Override
                public void dealWithResult(String request) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(request);
                        String status = jsonObject.getString(JsonConfig.KEY_STATUS);
                        if("0".equals(status)){//success
                            String data = jsonObject.getString(JsonConfig.KEY_DATA);
                            if("null".equals(data)){

                            }else {
                                JSONObject obj = new JSONObject(data);
                                String historyCounts = obj.getString(JsonConfig.KEY_USER_HISTORY_COUNTS);
                                String favoriteCounts = obj.getString(JsonConfig.KEY_USER_FAVORITE_COUNTS);
                                String commentsCounts = obj.getString(JsonConfig.KEY_USER_COMMENTS_COUNTS);
                                //存储数据
                                storageUserCounts(historyCounts, favoriteCounts, commentsCounts);
                            }
                        }else{

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            postDataTask.execute();
        }
    }
    public static boolean isLogin(){
        return (!"0".equals(gUser.getUid()));
    }

    /**
     * 清空用户浏览记录
     */
    private void clearUserHistory(){
        SharedPreferences sp = getSharedPreferences(Config.PREFS_NAME, 0);
        sp.edit().putString(Config.USER_HISTORY_KEY, "").commit();
    }

    /**
     * 添加用户浏览记录
     * @param temp
     */
    public void addUserHistory(String temp){
        SharedPreferences sp = getSharedPreferences(Config.PREFS_NAME, 0);
        String history = sp.getString(Config.USER_HISTORY_KEY, "");
        history += ("".equals(history))? temp : (Config.USER_HISTORY_COUNT_GAP + temp);
        sp.edit().putString(Config.USER_HISTORY_KEY, history).commit();
    }
    /**
     * 上传用户浏览记录
     */
//    public void uploadUserHistory(final OnLoadListener onLoadListener){
    public void uploadUserHistory(){
        SharedPreferences sp = getSharedPreferences(Config.PREFS_NAME, 0);
        String history = sp.getString(Config.USER_HISTORY_KEY, "");
        if("".equals(history)){
            return;
        }
        if(!isLogin()){
            clearUserHistory();
            return;
        }
        if(DeviceUtils.isNetworkConnected(this)){
            // post 参数
            PostParameter param = new PostParameter();
            param.add("uid", gUser.getUid());
            param.add("recall", "1");
            param.add("history", history);
            PostDataTask postDataTask = new PostDataTask(UrlConfig.SET_USER_HISTORY_URL, param) {
                @Override
                public void dealWithResult(String request) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(request);
                        String status = jsonObject.getString(JsonConfig.KEY_STATUS);
                        if("0".equals(status)){//success
//                            String data = jsonObject.getString(JsonConfig.KEY_DATA);
//                            if(!"null".equals(data)){
//                                if(onLoadListener != null){
//                                    onLoadListener.onLoaded(data);
//                                }
//                            }
                            clearUserHistory();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    isUpdate = false;
                }
            };
            postDataTask.execute();
            isUpdate = true;
        }
    }

}
