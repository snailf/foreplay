package cc.dingding.snail.forepaly.app.tasks;

import android.os.AsyncTask;

import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitmapLoader;

/**
 * Created by koudejian on 14-9-7.
 */
public class DownloadImageTask extends AsyncTask {
    private String mUrl = null;
    private String mResult = "";
    private OnDownloadFinishedListener mOnDownloadFinishedListener = null;

    public interface OnDownloadFinishedListener{
        public void onFinished(String msg);
    }
    public void setOnDownloadFinishedListener(OnDownloadFinishedListener onDownloadFinishedListener){
        mOnDownloadFinishedListener = onDownloadFinishedListener;
    }


    public DownloadImageTask(String url){
        mUrl = url;
    }
    public DownloadImageTask(String url, OnDownloadFinishedListener onDownloadFinishedListener){
        this(url);
        setOnDownloadFinishedListener(onDownloadFinishedListener);
    }
    @Override
    protected Object doInBackground(Object[] params) {
        if(mUrl != null && !"".equals(mUrl)){
            mResult = AsyncBitmapLoader.downloadImage(mUrl);
        }else{
            mResult = "图片地址有误...";
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(mOnDownloadFinishedListener != null){
            mOnDownloadFinishedListener.onFinished(mResult);
        }
    }
}
