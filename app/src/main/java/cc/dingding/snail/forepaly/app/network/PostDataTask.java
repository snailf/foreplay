package cc.dingding.snail.forepaly.app.network;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.List;

public abstract class PostDataTask extends AsyncTask<Void, Void, Void> {
    private String mResult = "";
    private String mUrl = "";
    private PostParameter mPostParameter = null;

    public PostDataTask(String urls, PostParameter params) {
        // TODO Auto-generated constructor stub
        this.mUrl = urls;
        this.mPostParameter = params;
        // this.mPostParameter.add("token", UserVerification.getUserIdentityStr(mUrl));
    }

    @Override
    protected Void doInBackground(Void... params) {
        postData(mUrl, mPostParameter.getParas());
        return null;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        String result = "";
        for (int i = 0; i < mPostParameter.getParas().size(); i++) {
            result += "&" + mPostParameter.getParas().get(i);
        }

        Log.e("mUrl-post", mUrl + result);
        this.mResult = "";
    }

    @Override
    protected void onPostExecute(Void result) {
        if (mResult == null) {
            mResult = "";
        }
        Log.e("test", mResult);
        dealWithResult(this.mResult);
    }

    public void postData(String url, List<NameValuePair> params) {
        String destUrl = url;
        DefaultHttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 5000);
        HttpConnectionParams.setSoTimeout(client.getParams(), 5000);
        HttpEntityEnclosingRequestBase httpRequest = new HttpPost(destUrl);
        try {
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            // execute the post and get the response from servers
            HttpResponse httpResponse = client.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                Log.d("tests-httppost", strResult);
                this.mResult = strResult;
            } else {
                Log.d("tests-httppost", "Error Response" + httpResponse.toString());
                Log.d("tests-httppost", "Error Response" + httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            Log.d("tests-httppost", "error occurs");
        }
    }
    public abstract void dealWithResult(String request);
}