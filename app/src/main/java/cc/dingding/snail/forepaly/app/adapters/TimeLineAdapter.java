package cc.dingding.snail.forepaly.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.activitys.CaseAppActivity;
import cc.dingding.snail.forepaly.app.activitys.CommentPopActivity;
import cc.dingding.snail.forepaly.app.activitys.LoginActivity;
import cc.dingding.snail.forepaly.app.cache.SharedCache;
import cc.dingding.snail.forepaly.app.config.JsonConfig;
import cc.dingding.snail.forepaly.app.config.UrlConfig;
import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitmapLoader;
import cc.dingding.snail.forepaly.app.helper.bitmap.model.ImageModel;
import cc.dingding.snail.forepaly.app.models.CaseModel;
import cc.dingding.snail.forepaly.app.models.ImageUrlModel;
import cc.dingding.snail.forepaly.app.network.PostDataTask;
import cc.dingding.snail.forepaly.app.network.PostParameter;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.views.CustomDialog;
import cc.dingding.snail.forepaly.app.views.FavoriteView;

/**
 * Created by koudejian on 14-7-31.
 */
public class TimeLineAdapter extends BaseAdapter {
    private final String TAG = "CaseAppAdapter";
    private Context mContext = null;
    private LayoutInflater mInflater;
    private LinkedList<CaseModel> mCaseList = null;

    private int mWidth = 0;
    private int mHeight = 0;

    private float mScale = (float) 1.5;         //640x960(3:2)

    private CustomDialog mCustomDialog = null;

    public TimeLineAdapter(Context context, LinkedList<CaseModel> list){
        mContext = context;
        mCaseList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int mScreenWidth = DeviceUtils.deviceWidth(mContext);
        Resources resources = mContext.getResources();
        mWidth = (mScreenWidth - (int)(2 * resources.getDimension(R.dimen.index_item_rl_gap))
                - (int)resources.getDimension(R.dimen.index_item_be_gap))/2;
        mHeight = (int) (mScale * mWidth);
    }
    @Override
    public int getCount() {
        Log.e(TAG, mCaseList.size() + "");
        if(mCaseList == null){
            return 0;
        }
        return mCaseList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCaseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_case_app_timeline, null);
        //*
        final CaseModel caseModel = mCaseList.get(position);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedCache.gCurrentCase = caseModel;
                mContext.startActivity(new Intent(mContext, CaseAppActivity.class));
            }
        };

        ImageView new_dot = (ImageView) view.findViewById(R.id.left_top_dot);

        if(position == 0){
            RelativeLayout top_line = (RelativeLayout) view.findViewById(R.id.left_top_line);
            top_line.setVisibility(View.INVISIBLE);
        }
        List<ImageUrlModel> images = caseModel.getImages();
        if(images != null){
            int length = images.size();
            ImageView[] imageviews = {
                    (ImageView) view.findViewById(R.id.image0),
                    (ImageView) view.findViewById(R.id.image1),
                    (ImageView) view.findViewById(R.id.image2),
                    (ImageView) view.findViewById(R.id.image3)
            };
            for(int i = 0; i < 4; i++){
                if(i < length){
                    new AsyncBitmapLoader(new ImageModel(images.get(i).getUrl()), imageviews[i]).start();
                    imageviews[i].setOnClickListener(listener);
                }else{
                    imageviews[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        //点赞评论数
        final TextView approve_count = (TextView) view.findViewById(R.id.approve_count);
        approve_count.setText(caseModel.getApproves());
        TextView comment_count = (TextView) view.findViewById(R.id.comment_count);
        comment_count.setText(caseModel.getComments());
        View approve = view.findViewById(R.id.approve);
        //点赞，评论
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("0".equals(MainApplication.gUser.getUid())){
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }else{
                    setApprove(caseModel.getId(), approve_count);
                }
            }
        });
        //评论
        View comment = view.findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("0".equals(MainApplication.gUser.getUid())){
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }else{
                    SharedCache.gCurrentCase = caseModel;
                    mContext.startActivity(new Intent(mContext, CommentPopActivity.class));
                }
            }
        });
        //收藏
        final FavoriteView favoriteView = (FavoriteView) view.findViewById(R.id.isfavate);
        favoriteView.setFrovite(caseModel.getIsfavorite());
        View favorite = view.findViewById(R.id.favorite);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("0".equals(MainApplication.gUser.getUid())){
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }else{
                    setFavorite(caseModel.getId(), favoriteView);
                }
            }
        });
        //分享
        View share = view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //*/
        return view;
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

    /**
     * 点赞
     * @param cid
     * @param approveCountsView
     */
    private void setApprove(String cid, final TextView approveCountsView){
        if(DeviceUtils.isNetworkConnected(mContext)){
            // post 参数
            PostParameter param = new PostParameter();
            param.add("uid", MainApplication.gUser.getUid());
            param.add("cid", cid);

            PostDataTask postDataTask = new PostDataTask(UrlConfig.USER_ADD_APPROVE, param) {
                @Override
                public void dealWithResult(String request) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(request);
                        String status = jsonObject.getString(JsonConfig.KEY_STATUS);
                        if("0".equals(status)){//success
                            popMessage("点赞成功...");
                            approveCountsView.setText((Integer.valueOf(approveCountsView.getText().toString()) + 1) + "");
                        }else{
                            String data = jsonObject.getString(JsonConfig.KEY_DATA);
                            if("408".equals(data)){
                                popMessage("您已经点赞，请勿重复操作...");
                            }else{
                                popMessage("点赞失败，请稍候再试...");
                            }
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
            popMessage("暂无网络链接...");
        }
    }
    public void popMessage(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
