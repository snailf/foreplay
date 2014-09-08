package cc.dingding.snail.forepaly.app.activitys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.adapters.TimeLineAdapter;
import cc.dingding.snail.forepaly.app.cache.SharedCache;
import cc.dingding.snail.forepaly.app.config.JsonConfig;
import cc.dingding.snail.forepaly.app.config.UrlConfig;
import cc.dingding.snail.forepaly.app.controllers.TimeLineHeaderController;
import cc.dingding.snail.forepaly.app.factorys.Json2List;
import cc.dingding.snail.forepaly.app.models.CaseModel;
import cc.dingding.snail.forepaly.app.network.PostDataTask;
import cc.dingding.snail.forepaly.app.network.PostParameter;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.views.CusListView;
import cc.dingding.snail.forepaly.app.views.CustomDialog;

/**
 * Created by koudejian on 14-8-7.
 * 时间轴
 */
public class TimeLineActivity extends BaseActivity {
    private Context mContext = null;
    private TimeLineHeaderController mTimeLineHeaderController = null;
    private View mView = null;
    private CaseModel mCaseModel = null;
    private CusListView mCusListView = null;

    private CustomDialog mCustomDialog = null;

    private LinkedList<CaseModel> mCaseList = new LinkedList<CaseModel>();;
    private int mPage = 1;

    private TimeLineAdapter mTimeLineAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        mContext = this;
        mCaseModel = SharedCache.gCurrentCase;

        String title = mCaseModel.getName();
        String logo = mCaseModel.getLogo();

        mView = findViewById(R.id.parent);
        mTimeLineHeaderController = new TimeLineHeaderController(this, mView, title, logo);
        mTimeLineHeaderController.setOnMenuClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimeLineActivity.this.finish();
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCaseList != null){
                        if(mCaseList.size() > 0){
                            String urls = mCaseList.getLast().getDownloadUrl();
                            if("".equals(urls)){
                                popMessage("此App暂未提供下载地址...");
                                return;
                            }
                            Uri url = Uri.parse(urls);
                            mContext.startActivity(new Intent(Intent.ACTION_VIEW, url));
                        }
                    }
                }
            }
        );
        mCusListView = (CusListView) findViewById(R.id.listview);
        mTimeLineAdapter = new TimeLineAdapter(mContext, mCaseList);
        mCusListView.setAdapter(mTimeLineAdapter);

        if(DeviceUtils.isNetworkConnected(mContext)){
            // post 参数
            PostParameter param = new PostParameter();
            param.add("case_id", mCaseModel.getId());
            param.add("page", mPage + "");
            param.add("uid", MainApplication.gUser.getUid());
            PostDataTask postDataTask = new PostDataTask(UrlConfig.GET_CASE_APP_LIST_TIMELINE, param) {
                @Override
                public void dealWithResult(String request) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(request);
                        String status = jsonObject.getString(JsonConfig.KEY_STATUS);
                        if("0".equals(status)){     //success
                            String data = jsonObject.getString(JsonConfig.KEY_DATA);
                            mCaseList = Json2List.getCaseList(data, mCaseList);
                            mTimeLineAdapter.notifyDataSetChanged();
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

    @Override
    protected void onResume() {
        super.onResume();
        //更新评论条数
        if(SharedCache.haveComment && SharedCache.haveCommit){
            if(mTimeLineAdapter != null){
                if(mTimeLineAdapter.getOnCommitSuccessedListener() != null){
                    mTimeLineAdapter.getOnCommitSuccessedListener().onSuccessed();
                }
            }
        }
        SharedCache.haveComment = false;
        SharedCache.haveCommit = false;
    }
}
