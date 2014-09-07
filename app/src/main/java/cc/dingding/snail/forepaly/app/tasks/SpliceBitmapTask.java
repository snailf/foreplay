package cc.dingding.snail.forepaly.app.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitmapLoader;
import cc.dingding.snail.forepaly.app.helper.bitmap.config.Config;
import cc.dingding.snail.forepaly.app.helper.bitmap.listener.BitMapLoadedListener;
import cc.dingding.snail.forepaly.app.helper.bitmap.model.ImageModel;
import cc.dingding.snail.forepaly.app.helper.bitmap.utils.MD5;
import cc.dingding.snail.forepaly.app.models.ImageUrlModel;

/**
 * Created by koudejian on 14-9-7.
 */
public class SpliceBitmapTask extends AsyncTask {
    List<AsyncBitmapLoader> mTaskQueue = new ArrayList<AsyncBitmapLoader>();
    List<ImageUrlModel> mImageModelList = null;

    private List<Bitmap> mBitmapList = new ArrayList<Bitmap>();
    private Bitmap mBitmap = null;
    private String mBitmapPath = null;
    private String mBitmapStr = "";
    private int mCount = 0;
    private OnSpliceFinishedListener mOnSpliceFinishedListener = null;
    public interface OnSpliceFinishedListener{
        public void onFinished(Bitmap bitmap);
    }
    public void setOnSpliceFinishedListener(OnSpliceFinishedListener onSpliceFinishedListener){
        mOnSpliceFinishedListener = onSpliceFinishedListener;
    }

    private SpliceBitmapTask(List<ImageUrlModel> imageModelList){
        mImageModelList = imageModelList;
    }
    public SpliceBitmapTask(List<ImageUrlModel> imageModelList, OnSpliceFinishedListener onSpliceFinishedListener){
        this(imageModelList);
        setOnSpliceFinishedListener(onSpliceFinishedListener);
    }
    public void start(){
        for(int i = 0; i < mImageModelList.size(); i++){
            mBitmapStr += mImageModelList.get(i).getUrl();
            //create queue.
            mTaskQueue.add(new AsyncBitmapLoader(new ImageModel(mImageModelList.get(i).getUrl()), new BitMapLoadedListener() {
                @Override
                public void OnSuccessed(Bitmap bitmap) {
                    mBitmapList.add(bitmap);
                    mCount++;
                    if(mCount == mImageModelList.size()){
                        SpliceBitmapTask.this.execute();
                    }
                }
            }).start());
        }
    }
    @Override
    protected Object doInBackground(Object[] params) {
        mBitmapPath = MD5.md5Encode(mBitmapStr);
        File temp = new File(Environment.getExternalStorageDirectory() + Config.IMAGE_SPLICE_DIR + mBitmapPath);
        if(temp.exists()) {
            mBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + Config.IMAGE_SPLICE_DIR + mBitmapPath);
        }else{
            int height = 0;
            int width = 0;
            for(int i = 0; i < mBitmapList.size(); i++){
                Bitmap bitmap = mBitmapList.get(i);
                if(bitmap != null){
                    height += bitmap.getHeight();
                    width = (width > bitmap.getWidth())? width : bitmap.getWidth();
                }
            }
            if(height > 0 && width > 0){//拼接
                mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mBitmap);
                int offset = 0;
                for(int i = 0; i < mBitmapList.size(); i++){
                    Bitmap bitmap = mBitmapList.get(i);
                    int x = (width - bitmap.getWidth()) / 2;
                    if(bitmap != null){
                        canvas.drawBitmap(bitmap, x, offset, null);
                        offset += bitmap.getHeight();
                    }
                }
            }
        }
        mTaskQueue.clear();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(mOnSpliceFinishedListener != null){
            mOnSpliceFinishedListener.onFinished(mBitmap);
        }
    }
}
