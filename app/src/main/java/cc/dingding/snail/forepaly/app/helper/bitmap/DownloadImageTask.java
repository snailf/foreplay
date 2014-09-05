package cc.dingding.snail.forepaly.app.helper.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cc.dingding.snail.forepaly.app.helper.bitmap.listener.BitMapLoadListener;
import cc.dingding.snail.forepaly.app.helper.bitmap.listener.ImageDownloadListener;

/**
 *
 * @author snail
 * @time 2013/9/20
 * @description get image stream from httpUrl,
 *
 */

public class DownloadImageTask extends AsyncTask<String, Void , String> {
    Bitmap mPhoto = null;
    String mUrl = "";

    private ImageDownloadListener mDownloadListener = null;
    private BitMapLoadListener mBitMapLoadListener = null;
    public DownloadImageTask(String url, BitMapLoadListener bitMapLoadListener){
        this.mUrl = url;
        mBitMapLoadListener = bitMapLoadListener;
    }

    /**
     * 任务完成监听事件
     * @param listener
     */
    public void setImageDownloadListener(ImageDownloadListener listener){
        mDownloadListener = listener;
    }
    @Override
    protected String doInBackground(String... params) {
        //*
        String TAG = "http";
        final int IO_BUFFER_SIZE = 4096;
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 5000);
        HttpConnectionParams.setSoTimeout(client.getParams(), 5000);
        final HttpGet getRequest = new HttpGet(this.mUrl);
        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    inputStream = entity.getContent();
                    final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                    outputStream = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i;
                    while ((i = inputStream.read()) != -1) {
                        baos.write(i);
                    }
                    String str = baos.toString();
                    outputStream.write(str.getBytes());
                    outputStream.flush();

                    final byte[] data = baos.toByteArray();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    baos.close();
                    mPhoto = bitmap;
                } finally {
                    if (inputStream !=null) {
                        inputStream.close();
                    }
                    if (outputStream !=null) {
                        outputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (IOException e) {
            getRequest.abort();
            Log.w(TAG, "I/O errorwhile retrieving bitmap from " + mUrl, e);
        } catch (IllegalStateException e) {
            getRequest.abort();
            Log.w(TAG, "Incorrect URL:" + mUrl);
        } catch (Exception e) {
            getRequest.abort();
            Log.w(TAG, "Error whileretrieving bitmap from " + mUrl, e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return "";
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void onPostExecute(String result) {
        if(mPhoto != null){
            if(mDownloadListener != null ){
                mDownloadListener.OnSuccessed(this);
            }
            if(mBitMapLoadListener != null){
                mBitMapLoadListener.OnSuccessed(mPhoto);
            }
        }
    }
}