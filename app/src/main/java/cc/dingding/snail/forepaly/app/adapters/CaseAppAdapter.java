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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.activitys.LoginActivity;
import cc.dingding.snail.forepaly.app.cache.SharedCache;
import cc.dingding.snail.forepaly.app.config.JsonConfig;
import cc.dingding.snail.forepaly.app.config.UrlConfig;
import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitmapLoader;
import cc.dingding.snail.forepaly.app.helper.bitmap.model.ImageModel;
import cc.dingding.snail.forepaly.app.models.CaseModel;
import cc.dingding.snail.forepaly.app.models.CommentsModel;
import cc.dingding.snail.forepaly.app.network.PostDataTask;
import cc.dingding.snail.forepaly.app.network.PostParameter;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.views.CustomDialog;
import cc.dingding.snail.forepaly.app.views.FavoriteView;
import cc.dingding.snail.forepaly.app.views.MyGallery;

/**
 * Created by koudejian on 14-7-31.
 */
public class CaseAppAdapter extends BaseAdapter {
    private Context mContext = null;
    private LayoutInflater mInflater;
    private LinkedList<CommentsModel> mCommentsList = null;

    private int mWidth = 0;
    private int mHeight = 0;
    private int mTitleHeight = 0;
    private float mScale = (float) 1.5;         //640x960(3:2)

    private CustomDialog mCustomDialog = null;

    private MyGallery mMyGallery = null;

    public CaseAppAdapter(Context context, LinkedList<CommentsModel> list){
        mContext = context;
        mCommentsList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int mScreenWidth = DeviceUtils.deviceWidth(mContext);
        Resources resources = mContext.getResources();
        mWidth = (mScreenWidth - (int)(2 * resources.getDimension(R.dimen.index_item_rl_gap))
                - (int)resources.getDimension(R.dimen.index_item_be_gap))/2;
        mHeight = (int) (mScale * mWidth);
        mTitleHeight = (int) mContext.getResources().getDimension(R.dimen.index_item_top_height);
    }
    @Override
    public int getCount() {
        if(mCommentsList != null){
            return mCommentsList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mCommentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(position == 0){
            final CaseModel caseModel = SharedCache.gCurrentCase;
            view =  mInflater.inflate(R.layout.item_app_details, null);
            if(caseModel != null){
                TextView name = (TextView) view.findViewById(R.id.name);
                name.setText(caseModel.getName());

                ImageView logo = (ImageView) view.findViewById(R.id.logo);
                new AsyncBitmapLoader(new ImageModel(caseModel.getLogo()), logo).start();

                final FavoriteView favoriteView = (FavoriteView) view.findViewById(R.id.isfavate);
                favoriteView.setFrovite(caseModel.getIsfavorite());
                favoriteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if("0".equals(MainApplication.gUser.getUid())){
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        }else{
                            setFavorite(caseModel.getId(), favoriteView);
                        }
                    }
                });
                mMyGallery = (MyGallery) view.findViewById(R.id.gallery);
                mMyGallery.setSpacing((int) mContext.getResources().getDimension(R.dimen.gallery_app_gap));
                CaseAppGalleryAdapter galleryAdapter = new CaseAppGalleryAdapter(mContext, caseModel);
                mMyGallery.setAdapter(galleryAdapter);
                mMyGallery.setOnItemSelectedListener(galleryAdapter.mOnSelectedListener);

            }
        }else{
            view =  mInflater.inflate(R.layout.item_comments_info, null);
            final CommentsModel commentsModel = mCommentsList.get(position);
            if(position == 1){
                TextView title = (TextView) view.findViewById(R.id.title_tv);
                title.setVisibility(View.VISIBLE);
            }

            TextView comments_content = (TextView) view.findViewById(R.id.comments_content);
            comments_content.setText(commentsModel.getComments());

            TextView time = (TextView) view.findViewById(R.id.time);
            time.setText(commentsModel.getTimes().substring(0, 10));

            TextView username = (TextView) view.findViewById(R.id.username);
            username.setText(commentsModel.getNick());

            ImageView avatarView = (ImageView) view.findViewById(R.id.avatar);
            new AsyncBitmapLoader(new ImageModel(commentsModel.getAvatar()), avatarView).start();
            Log.e("test", commentsModel.getAvatar());

            //点赞评论数
            final TextView approve_count = (TextView) view.findViewById(R.id.approve_count);
            approve_count.setText(commentsModel.getApproveCount());
            View approve = view.findViewById(R.id.approve);
            //点赞，评论
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if("0".equals(MainApplication.gUser.getUid())){
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    }else{
                        setApprove(commentsModel.getId(), approve_count);
                    }
                }
            });
        }

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

            PostDataTask postDataTask = new PostDataTask(UrlConfig.USER_ADD_COMMENTS_APPROVE, param) {
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
    private void popMessage(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
