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

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.activitys.DetailsActivity;
import cc.dingding.snail.forepaly.app.activitys.LoginActivity;
import cc.dingding.snail.forepaly.app.activitys.TimeLineActivity;
import cc.dingding.snail.forepaly.app.cache.SharedCache;
import cc.dingding.snail.forepaly.app.config.JsonConfig;
import cc.dingding.snail.forepaly.app.config.UrlConfig;
import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitmapLoader;
import cc.dingding.snail.forepaly.app.helper.bitmap.model.ImageModel;
import cc.dingding.snail.forepaly.app.models.CaseModel;
import cc.dingding.snail.forepaly.app.network.PostDataTask;
import cc.dingding.snail.forepaly.app.network.PostParameter;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.views.CustomDialog;
import cc.dingding.snail.forepaly.app.views.FavoriteView;

/**
 * Created by koudejian on 14-7-31.
 */
public class CaseAdapter extends BaseAdapter {
    private Context mContext = null;
    private LayoutInflater mInflater;
    private LinkedList<CaseModel> mCaseList = null;

    private int mWidth = 0;
    private int mHeight = 0;
    private int mTitleHeight = 0;
    private float mScale = (float) 1.5;         //640x960(3:2)

    private CustomDialog mCustomDialog = null;

    public CaseAdapter(Context context, LinkedList<CaseModel> list ){
        mContext = context;
        mCaseList = list;
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
        if(mCaseList != null){
            int length = mCaseList.size();
            return (length % 2 == 1) ? (length + 1)/2 : length/2;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mCaseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void initData(final CaseModel caseModel, ImageView logo, TextView name, final FavoriteView favoriteView, RelativeLayout topView, ImageView imageView, RelativeLayout relativeLayout){
        topView.setLayoutParams(new RelativeLayout.LayoutParams(mWidth, mTitleHeight));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mWidth, mHeight);
        relativeLayout.setLayoutParams(layoutParams);
        new AsyncBitmapLoader(new ImageModel(caseModel.getLogo()), logo).start();
        name.setText(caseModel.getName());
        //listener
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(caseModel.getImages() == null ){
                    return;
                }
                if(caseModel.getImages().size() == 0){
                    return ;
                }
                SharedCache.gCurrentCase = caseModel;
                mContext.startActivity(new Intent(mContext, DetailsActivity.class));
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedCache.gCurrentCase = caseModel;
                mContext.startActivity(new Intent(mContext, TimeLineActivity.class));
            }
        };
        name.setOnClickListener(onClickListener);
        logo.setOnClickListener(onClickListener);

//        favoriteView.setFrovite(caseModel.getIsfavorite());
        favoriteView.setVisibility(View.GONE);
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
        if(caseModel.getImages() != null){
            if(caseModel.getImages().size() > 0){
                new AsyncBitmapLoader(new ImageModel(caseModel.getImages().get(0).getUrl(), mWidth, mHeight), imageView).start();
            }
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_case_list, null);
        //每次取两条 2*position && 2*position+1
        Log.e("test", position + "");
        int positions = 2*position;
        CaseModel caseModel = null;
        if( positions < mCaseList.size()){
            caseModel = mCaseList.get(positions);
            ImageView logo = (ImageView) view.findViewById(R.id.logo);
            TextView name = (TextView) view.findViewById(R.id.title);
            FavoriteView favoriteView = (FavoriteView) view.findViewById(R.id.isfavate);
            RelativeLayout topView = (RelativeLayout) view.findViewById(R.id.top);
            ImageView imageView = (ImageView) view.findViewById(R.id.fimage);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.fimage_rl);
            initData(caseModel, logo, name, favoriteView, topView, imageView, relativeLayout);
        }else{
            view.findViewById(R.id.left_rl).setVisibility(View.GONE);
        }
        positions += 1;
        if(positions < mCaseList.size()){
            caseModel = mCaseList.get(positions);
            ImageView logo = (ImageView) view.findViewById(R.id.rlogo);
            TextView name = (TextView) view.findViewById(R.id.rtitle);
            final FavoriteView favoriteView = (FavoriteView) view.findViewById(R.id.risfavate);
            ImageView imageView = (ImageView) view.findViewById(R.id.rfimage);
            RelativeLayout topView = (RelativeLayout) view.findViewById(R.id.rtop);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rfimage_rl);
            initData(caseModel, logo, name, favoriteView, topView, imageView, relativeLayout);
        }else{
            view.findViewById(R.id.right_rl).setVisibility(View.GONE);
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

    private void popMessage(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
