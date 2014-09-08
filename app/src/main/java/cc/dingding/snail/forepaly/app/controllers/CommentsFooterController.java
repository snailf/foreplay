package cc.dingding.snail.forepaly.app.controllers;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.activitys.LoginActivity;
import cc.dingding.snail.forepaly.app.cache.SharedCache;
import cc.dingding.snail.forepaly.app.config.JsonConfig;
import cc.dingding.snail.forepaly.app.config.UrlConfig;
import cc.dingding.snail.forepaly.app.models.CaseModel;
import cc.dingding.snail.forepaly.app.models.CommentsModel;
import cc.dingding.snail.forepaly.app.network.PostDataTask;
import cc.dingding.snail.forepaly.app.network.PostParameter;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.utils.StringUtil;
import cc.dingding.snail.forepaly.app.views.CustomDialog;

/**
 * Created by koudejian on 14-9-8.
 */
public class CommentsFooterController extends BaseController {
    private TextView mTvSend = null;
    private EditText mEtMsg = null;
    private CaseModel mCaseModel = null;
    private CustomDialog mCustomDialog = null;

    private OnCommitSuccessedListener mOnCommitSuccessedListener = null;
    public CommentsFooterController(Context context, View view) {
        super(context, view);
        init();
    }

    public interface OnCommitSuccessedListener{
        public void onCommitSuccessed(CommentsModel commentsModel);
    }
    public void setOnCommitSuccessedListener(OnCommitSuccessedListener onCommitSuccessedListener){
        mOnCommitSuccessedListener = onCommitSuccessedListener;
    }
    @Override
    void init() {
        mCaseModel = SharedCache.gCurrentCase;
        mTvSend = (TextView) findViewById(R.id.send);
        mEtMsg = (EditText) findViewById(R.id.comment_text);
        mTvSend.setEnabled(canCommit());
        mTvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mEtMsg.getText().toString().trim();
                if(!"".equals(msg)){
                    if(MainApplication.isLogin()){
                        addCaseComments(msg);
                    }else{
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    }
                }
            }
        });
        mEtMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                mTvSend.setEnabled(canCommit());
            }
        });
    }
    private boolean canCommit() {
        String msg = mEtMsg.getText().toString().trim();
        if (StringUtil.isEmpty(msg)) {
            return false;
        }
        return true;
    }

    private void addCaseComments(final String msg) {
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
                            if(mOnCommitSuccessedListener != null){
                                String data = jsonObject.getString(JsonConfig.KEY_DATA);
                                JSONObject temp = new JSONObject(data);
                                String id = temp.getString(JsonConfig.KEY_COMMENTS_ID);
                                String times = temp.getString(JsonConfig.KEY_COMMENTS_TIME);
                                mOnCommitSuccessedListener.onCommitSuccessed(new CommentsModel(id, times, msg, "0", MainApplication.gUser.getAvatar(), MainApplication.gUser.getNick()));
                            }
                            mEtMsg.setText("");
                            popMessage("发表评论成功...");
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
