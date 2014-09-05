package cc.dingding.snail.forepaly.app.activitys;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.cache.SharedCache;
import cc.dingding.snail.forepaly.app.config.JsonConfig;
import cc.dingding.snail.forepaly.app.config.UrlConfig;
import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitmapLoader;
import cc.dingding.snail.forepaly.app.helper.bitmap.model.ImageModel;
import cc.dingding.snail.forepaly.app.models.CaseModel;
import cc.dingding.snail.forepaly.app.models.ImageUrlModel;
import cc.dingding.snail.forepaly.app.network.PostDataTask;
import cc.dingding.snail.forepaly.app.network.PostParameter;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.views.CustomDialog;

/**
 * Created by koudejian on 14-8-22.
 * 弹出评论框
 */
public class CommentPopActivity extends BaseActivity {
    private ImageView mIvImage = null;
    private EditText mEtComment = null;
    private TextView mTvCancel = null;
    private TextView mTvSend = null;

    private Context mContext = null;
    private CustomDialog mCustomDialog = null;
    private CaseModel mCaseModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_comments);

        mContext = this;
        mCaseModel = SharedCache.gCurrentCase;

        int size = (int) getResources().getDimension(R.dimen.comment_pop_image_size);
        mIvImage = (ImageView) findViewById(R.id.image);
        List<ImageUrlModel> images = mCaseModel.getImages();
        if(images != null){
            if(images.size() > 0){
                new AsyncBitmapLoader(new ImageModel(images.get(0).getUrl(), size, size), mIvImage).start();
            }
        }
        mEtComment = (EditText) findViewById(R.id.comments);

        //button
        mTvCancel = (TextView) findViewById(R.id.cancel);
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentPopActivity.this.finish();
            }
        });
        mTvSend = (TextView) findViewById(R.id.send);
        mTvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mEtComment.getText().toString();
                if(!"".equals(msg) && !"0".equals(MainApplication.gUser.getUid())){
                    addComments(msg);
                }
            }
        });

    }

    private void addComments(String msg) {
        if(DeviceUtils.isNetworkConnected(mContext)){
            // post 参数
            PostParameter param = new PostParameter();
            param.add("create_uid", MainApplication.gUser.getUid());
            param.add("cid", mCaseModel.getId());
            param.add("comment", msg);
            PostDataTask postDataTask = new PostDataTask(UrlConfig.ADD_COMMENTS_URL, param) {
                @Override
                public void dealWithResult(String request) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(request);
                        String status = jsonObject.getString(JsonConfig.KEY_STATUS);
                        if("0".equals(status)){//success
                            popMessage("发表评论成功...");
                            CommentPopActivity.this.finish();
                        }else{
                            popMessage("评论失败，请稍候再试...");
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
}
