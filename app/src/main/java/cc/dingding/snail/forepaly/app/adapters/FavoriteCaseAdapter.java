package cc.dingding.snail.forepaly.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cc.dingding.snail.forepaly.app.activitys.TimeLineActivity;
import cc.dingding.snail.forepaly.app.cache.SharedCache;
import cc.dingding.snail.forepaly.app.config.JsonConfig;
import cc.dingding.snail.forepaly.app.config.UrlConfig;
import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitmapLoader;
import cc.dingding.snail.forepaly.app.helper.bitmap.model.ImageModel;
import cc.dingding.snail.forepaly.app.models.CaseFavoriteModel;
import cc.dingding.snail.forepaly.app.models.CaseModel;
import cc.dingding.snail.forepaly.app.network.PostDataTask;
import cc.dingding.snail.forepaly.app.network.PostParameter;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.views.CustomDialog;
import cc.dingding.snail.forepaly.app.views.FavoriteView;
import cc.dingding.snail.forepaly.app.views.xlist.XListView;

/**
 * Created by koudejian on 14-7-31.
 */
public class FavoriteCaseAdapter extends UserCaseAdapter{


    private int mWidth = 0;
    private int mHeight = 0;
    private int mTitleHeight = 0;
    private float mScale = (float) 1.5;         //640x960(3:2)

    private CustomDialog mCustomDialog = null;

    protected XListView mXListView = null;
    protected LinkedList<CaseFavoriteModel> mCaseList = null;
    protected LayoutInflater mInflater;

    public FavoriteCaseAdapter(XListView xListView, Context context, LinkedList<CaseFavoriteModel> list){
        super(context);
        this.mXListView = xListView;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_favorite_case_list, null);
        //每次取两条 2*position && 2*position+1
        Log.e("test", position + "");
        int positions = 2*position;
        CaseModel caseModel = null;

        if( positions < mCaseList.size()){
            caseModel = mCaseList.get(positions);
            ImageView logo = (ImageView) view.findViewById(R.id.logo);
            TextView name = (TextView) view.findViewById(R.id.title);
//            final FavoriteView favoriteView = (FavoriteView) view.findViewById(R.id.isfavate);
//            favoriteView.setVisibility(View.GONE);
            RelativeLayout leftTop = (RelativeLayout) view.findViewById(R.id.top);
            leftTop.setLayoutParams(new RelativeLayout.LayoutParams(mWidth, mTitleHeight));

            ImageView imageView = (ImageView) view.findViewById(R.id.fimage);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.fimage_rl);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mWidth, mHeight);
            relativeLayout.setLayoutParams(layoutParams);
            new AsyncBitmapLoader(new ImageModel(caseModel.getLogo()), logo).start();
            name.setText(caseModel.getName());

            final CaseModel finalCaseModel1 = caseModel;
            if(caseModel.getImages() != null){
                if(caseModel.getImages() != null ){
                    if(caseModel.getImages().size() > 0) {
                        new AsyncBitmapLoader(new ImageModel(caseModel.getImages().get(0).getUrl(), mWidth, mHeight), imageView).start();
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedCache.gCurrentCase = finalCaseModel1;
                                mContext.startActivity(new Intent(mContext, DetailsActivity.class));
                            }
                        });
                    }
                }
            }

            //listener


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedCache.gCurrentCase = finalCaseModel1;
                    mContext.startActivity(new Intent(mContext, DetailsActivity.class));
                }
            });
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedCache.gCurrentCase = finalCaseModel1;
                    mContext.startActivity(new Intent(mContext, TimeLineActivity.class));
                }
            });
        }else{
            view.findViewById(R.id.left_rl).setVisibility(View.GONE);
        }

        positions += 1;
        if(positions < mCaseList.size()){
            caseModel = mCaseList.get(positions);
            ImageView logo = (ImageView) view.findViewById(R.id.rlogo);
            TextView name = (TextView) view.findViewById(R.id.rtitle);
//            final FavoriteView favoriteView = (FavoriteView) view.findViewById(R.id.risfavate);
//            favoriteView.setVisibility(View.GONE);

            ImageView imageView = (ImageView) view.findViewById(R.id.rfimage);

            RelativeLayout rightTop = (RelativeLayout) view.findViewById(R.id.rtop);
            rightTop.setLayoutParams(new RelativeLayout.LayoutParams(mWidth, mTitleHeight));

            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rfimage_rl);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mWidth, mHeight);
            relativeLayout.setLayoutParams(layoutParams);

            new AsyncBitmapLoader(new ImageModel(caseModel.getLogo()), logo).start();
            name.setText(caseModel.getName());
            final CaseModel finalCaseModel2 = caseModel;
            if(caseModel.getImages() != null){
                if(caseModel.getImages() != null ){
                    if(caseModel.getImages().size() > 0) {
                        new AsyncBitmapLoader(new ImageModel(caseModel.getImages().get(0).getUrl(), mWidth, mHeight), imageView).start();
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedCache.gCurrentCase = finalCaseModel2;
                                mContext.startActivity(new Intent(mContext, DetailsActivity.class));
                            }
                        });
                    }
                }
            }

            //listener

            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedCache.gCurrentCase = finalCaseModel2;
                    mContext.startActivity(new Intent(mContext, TimeLineActivity.class));
                }
            });
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

    @Override
    public LinkedList getList() {
        return mCaseList;
    }

    @Override
    public void setList(LinkedList list) {
        mCaseList = list;
    }

    @Override
    public void clear() {
        if(mCaseList != null){
            mCaseList.clear();
        }
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        mOnChangedListener.onChanged(1);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        mOnChangedListener.onChanged(1);
    }
}
