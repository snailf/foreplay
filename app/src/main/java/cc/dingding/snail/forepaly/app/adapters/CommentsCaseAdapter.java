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

import java.util.LinkedList;

import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.activitys.CaseAppActivity;
import cc.dingding.snail.forepaly.app.cache.SharedCache;
import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitmapLoader;
import cc.dingding.snail.forepaly.app.helper.bitmap.model.ImageModel;
import cc.dingding.snail.forepaly.app.models.CaseCommentModel;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.views.CustomDialog;
import cc.dingding.snail.forepaly.app.views.xlist.XListView;

/**
 * Created by koudejian on 14-7-31.
 */
public class CommentsCaseAdapter extends UserCaseAdapter{

    private int mWidth = 0;
    private int mHeight = 0;
    private int mTitleHeight = 0;
    private float mScale = (float) 1.5;         //640x960(3:2)

    private CustomDialog mCustomDialog = null;

    protected XListView mXListView = null;
    protected LinkedList<CaseCommentModel> mCaseList = null;
    protected LayoutInflater mInflater;

    public CommentsCaseAdapter(XListView xListView, Context context, LinkedList<CaseCommentModel> list){
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
        View view = mInflater.inflate(R.layout.item_comment_case_list, null);
        //每次取两条 2*position && 2*position+1
        Log.e("test", position + "");
        int positions = 2*position;
        CaseCommentModel caseModel = null;

        if( positions < mCaseList.size()){
            caseModel = mCaseList.get(positions);
            TextView name = (TextView) view.findViewById(R.id.title);

            ImageView imageView = (ImageView) view.findViewById(R.id.fimage);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.fimage_rl);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mWidth, mHeight);
            relativeLayout.setLayoutParams(layoutParams);
            name.setText(caseModel.getCommentContent());
            if(caseModel.getImages() != null){
                if(caseModel.getImages().size() > 0){
                    new AsyncBitmapLoader(new ImageModel(caseModel.getImages().get(0).getUrl(), mWidth, mHeight), imageView).start();
                }
            }

            RelativeLayout leftBottom = (RelativeLayout) view.findViewById(R.id.bottom);
            leftBottom.setLayoutParams(new RelativeLayout.LayoutParams(mWidth, mTitleHeight));

            //listener

            final CaseCommentModel finalCaseModel1 = caseModel;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedCache.gCurrentCase = finalCaseModel1;
                    mContext.startActivity(new Intent(mContext, CaseAppActivity.class));
                }
            });

        }else{
            view.findViewById(R.id.left_rl).setVisibility(View.GONE);
        }

        positions += 1;
        //*
        if(positions < mCaseList.size()){
            caseModel = mCaseList.get(positions);
            TextView name = (TextView) view.findViewById(R.id.rtitle);
            ImageView imageView = (ImageView) view.findViewById(R.id.rfimage);

            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rfimage_rl);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mWidth, mHeight);
            relativeLayout.setLayoutParams(layoutParams);
            name.setText(caseModel.getCommentContent());
            if(caseModel.getImages() != null){
                if(caseModel.getImages().size() > 0){
                    new AsyncBitmapLoader(new ImageModel(caseModel.getImages().get(0).getUrl(), mWidth, mHeight), imageView).start();
                }else{

                }
            }

            RelativeLayout rightBottom = (RelativeLayout) view.findViewById(R.id.rbottom);
            rightBottom.setLayoutParams(new RelativeLayout.LayoutParams(mWidth, mTitleHeight));

            //listener
            final CaseCommentModel finalCaseModel2 = caseModel;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedCache.gCurrentCase = finalCaseModel2;
                    mContext.startActivity(new Intent(mContext, CaseAppActivity.class));
                }
            });
        }else{
            view.findViewById(R.id.right_rl).setVisibility(View.GONE);
        }
        //*/
        return view;
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
        mOnChangedListener.onChanged(2);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        mOnChangedListener.onChanged(2);
    }
}
