package cc.dingding.snail.forepaly.app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitmapLoader;
import cc.dingding.snail.forepaly.app.helper.bitmap.listener.BitMapLoadedListener;
import cc.dingding.snail.forepaly.app.helper.bitmap.model.ImageModel;
import cc.dingding.snail.forepaly.app.models.CaseModel;
import cc.dingding.snail.forepaly.app.models.ImageUrlModel;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.views.MyGallery;

public class GalleryAdapter extends BaseAdapter {
    private int mWidth = 0;
    private int mHeight = 0;

    private final Context mContext;
    private List<ImageUrlModel> mPhoto = null;

    private CaseModel mCaseModel = null;

    private MyGallery mGallery = null;

    public AdapterView.OnItemSelectedListener mOnSelectedListener = null;
    private LayoutInflater mInflater;
    public GalleryAdapter(Context context, CaseModel caseModel, MyGallery gallery) {
        mContext = context;
        mCaseModel = caseModel;
        mPhoto = mCaseModel.getImages();
        mGallery = gallery;
        mHeight = DeviceUtils.deviceHeight(context);
        mWidth = DeviceUtils.deviceWidth(context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mOnSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view, int position, long id) {
                String url = mPhoto.get(position).getUrl();
                final ImageView imageView = (ImageView) view.findViewById(R.id.image);
                new AsyncBitmapLoader(new ImageModel(url), imageView).setBitMapLoadedListener(new BitMapLoadedListener() {
                    @Override
                    public void OnSuccessed(Bitmap bitmap) {
                        if(bitmap != null){
                            int height = bitmap.getHeight();
                            int width = bitmap.getWidth();

                            int height_rl, width_rl;
                            double deviceRate = 1.000 * mHeight/mWidth ;
                            double imageRate = 1.000 * height/width;
                            if( deviceRate > imageRate){
                                width_rl = mWidth;
                                height_rl = (int) (width_rl * imageRate);
                            }else if(deviceRate < imageRate){
                                height_rl = mHeight;
                                width_rl = (int) (height_rl / imageRate);
                            }else{
                                width_rl = mWidth;
                                height_rl = mHeight;
                            }
                            Log.e("GalleryAdapter", mWidth + ":" + mHeight + ";" + width + ":" + height + ";" + width_rl + ":" + height_rl );
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width_rl, height_rl);
                            params.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
                            imageView.setLayoutParams(params);
                            imageView.setScaleType(ImageView.ScaleType.CENTER);
                        }
                    }
                }).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    public int getCount() {
        if(mPhoto == null){
            return 0;
        }
        return mPhoto.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_gallery, null);
        return view;
    }
}