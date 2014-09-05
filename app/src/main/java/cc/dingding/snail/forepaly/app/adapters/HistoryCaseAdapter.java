package cc.dingding.snail.forepaly.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.activitys.CaseAppActivity;
import cc.dingding.snail.forepaly.app.cache.SharedCache;
import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitmapLoader;
import cc.dingding.snail.forepaly.app.helper.bitmap.listener.BitMapLoadedListener;
import cc.dingding.snail.forepaly.app.helper.bitmap.model.ImageModel;
import cc.dingding.snail.forepaly.app.models.CaseHistoryModel;
import cc.dingding.snail.forepaly.app.models.ImageUrlModel;
import cc.dingding.snail.forepaly.app.views.CustomDialog;
import cc.dingding.snail.forepaly.app.views.xlist.XListView;

/**
 * Created by koudejian on 14-7-31.
 */
public class HistoryCaseAdapter extends UserCaseAdapter{

    private float mScale = (float) 1.5;         //640x960(3:2)

    private CustomDialog mCustomDialog = null;

    protected XListView mXListView = null;
    protected LinkedList<CaseHistoryModel> mCaseList = null;
    protected LayoutInflater mInflater;
    private int mWidth = 0;
    public HistoryCaseAdapter(XListView xListView, Context context, LinkedList<CaseHistoryModel> list){
        super(context);
        this.mXListView = xListView;

        mCaseList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        if(mCaseList != null){
            return mCaseList.size();
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
        View view = mInflater.inflate(R.layout.item_history_case_list, null);

        final CaseHistoryModel caseModel = mCaseList.get(position);

        TextView timeline = (TextView) view.findViewById(R.id.timeline);
        timeline.setText(getTimeLile(caseModel.getHistoryTimes()));
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
        final ImageView imageView = (ImageView) view.findViewById(R.id.image0);
        RelativeLayout image_rl = (RelativeLayout) view.findViewById(R.id.image_rl);
        mWidth = image_rl.getLayoutParams().width;

        List<ImageUrlModel> images = caseModel.getImages();
        if(images != null){
            if(images.size() > 0){
                new AsyncBitmapLoader(new ImageModel(images.get(0).getUrl()), imageView).setBitMapLoadedListener(new BitMapLoadedListener() {
                    @Override
                    public void OnSuccessed(Bitmap bitmap) {
                        if (bitmap != null) {
                            if(mWidth > 0) {
                                int height = bitmap.getHeight();
                                int width = bitmap.getWidth();

                                int height_rl, width_rl = mWidth;
                                height_rl = (int) (1.000 * mWidth * height / width);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width_rl, height_rl);
                                imageView.setLayoutParams(params);
                            }
                        }
                    }
                }).start();
                imageView.setOnClickListener(listener);
            }
        }

        return view;
    }

    private String getTimeLile(String time){
        String result = "unknow";
        if(time != null && time.length() == 19){
            int position = time.indexOf("-");
            result = time.substring(position+1, position + 6);
            result.replaceAll("-", ".");
        }
        return result;
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
        mOnChangedListener.onChanged(0);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        mOnChangedListener.onChanged(0);
    }
}
