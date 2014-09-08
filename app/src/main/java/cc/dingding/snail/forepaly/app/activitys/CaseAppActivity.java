package cc.dingding.snail.forepaly.app.activitys;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedList;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.adapters.CaseAppAdapter;
import cc.dingding.snail.forepaly.app.cache.SharedCache;
import cc.dingding.snail.forepaly.app.config.Config;
import cc.dingding.snail.forepaly.app.config.JsonConfig;
import cc.dingding.snail.forepaly.app.config.UrlConfig;
import cc.dingding.snail.forepaly.app.controllers.CommentsFooterController;
import cc.dingding.snail.forepaly.app.controllers.CommonHeaderController;
import cc.dingding.snail.forepaly.app.factorys.Json2List;
import cc.dingding.snail.forepaly.app.helper.umeng.UmengHelper;
import cc.dingding.snail.forepaly.app.models.CaseModel;
import cc.dingding.snail.forepaly.app.models.CommentsModel;
import cc.dingding.snail.forepaly.app.network.PostDataTask;
import cc.dingding.snail.forepaly.app.network.PostParameter;
import cc.dingding.snail.forepaly.app.tasks.SpliceBitmapTask;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.utils.StringUtil;
import cc.dingding.snail.forepaly.app.views.CustomDialog;
import cc.dingding.snail.forepaly.app.views.xlist.XListView;

/**
 * Created by koudejian on 14-8-9.
 * 单个应用列表
 */
public class CaseAppActivity extends BaseActivity {

    private CommonHeaderController mCommonHeaderController = null;
    private CommentsFooterController mCommentsFooterController = null;

    private View mView = null;
    private CaseModel mCaseModel = null;

    private Context mContext = null;
    private CustomDialog mCustomDialog = null;
    private XListView mXListView = null;
    private int mCurrentPage = 0;

    private CaseAppAdapter mCaseAppAdapter = null;
    private LinkedList<CommentsModel> mCommentsList = new LinkedList<CommentsModel>();

    private final XListView.IXListViewListener mIXListViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            Log.e("test", "refresh");
            mCurrentPage = 0;
            loadData(true);
            mCommentsList.clear();
        }
        @Override
        public void onLoadMore() {
            Log.e("test", "loadmore");
            mCurrentPage++;
            loadData(false);
            mCustomDialog.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_app);
        mContext = this;
        mView = findViewById(R.id.parent);
        mCaseModel = SharedCache.gCurrentCase;
        //header
        mCommonHeaderController = new CommonHeaderController(this, mView, mCaseModel.getName());
        mCommonHeaderController.setRightBtn(R.drawable.timeline_share);
        mCommonHeaderController.setLeftBtn(R.drawable.btn_back);
        mCommonHeaderController.setOnMenuClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CaseAppActivity.this.finish();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("test", "shared");
                        if(mCaseModel.getImages() == null){
                            popMessage("暂无图片可分享...");
                            return;
                        }

                        if(mCustomDialog == null){
                            mCustomDialog = new CustomDialog(mContext, CustomDialog.DIALOG_THEME_WAIT_NOT_CANCEL);
                        }
                        mCustomDialog.show();
                        new SpliceBitmapTask(mCaseModel.getImages(), new SpliceBitmapTask.OnSpliceFinishedListener() {
                            @Override
                            public void onFinished(Bitmap bitmap) {
                                UmengHelper.getInstance().share(mContext, mCaseModel, bitmap);
                                mCustomDialog.cancel();
                            }
                        }).start();
                    }
                }
        );
        //评论栏
        mCommentsFooterController = new CommentsFooterController(this, mView);
        mCommentsFooterController.setOnCommitSuccessedListener(new CommentsFooterController.OnCommitSuccessedListener() {
            @Override
            public void onCommitSuccessed(CommentsModel commentsModel) {
                mCommentsList.add(commentsModel);
                mCaseAppAdapter.notifyDataSetChanged();
            }
        });

        mCustomDialog = new CustomDialog(mContext, CustomDialog.DIALOG_THEME_WAIT_NOT_CANCEL);
        //xlistview
        mXListView = (XListView) mView.findViewById(R.id.xlistview);
        mXListView.setXListViewListener(mIXListViewListener);
        mXListView.setPullLoadEnable(true);
        mCaseAppAdapter = new CaseAppAdapter(mContext, mCommentsList);
        mXListView.setAdapter(mCaseAppAdapter);

        String history = mCaseModel.getId() + Config.USER_HISTORY_ITEM_GAP + StringUtil.getDate(new Date())+ Config.USER_HISTORY_ITEM_GAP + "1";
        MainApplication.getInstance().addUserHistory(history);

    }

    private void loadData(final boolean needClear){
        if(DeviceUtils.isNetworkConnected(mContext)){
            // post 参数
            PostParameter param = new PostParameter();
            param.add("cid", mCaseModel.getId());
            param.add("page", mCurrentPage + "");
            PostDataTask postDataTask = new PostDataTask(UrlConfig.GET_CASE_COMMENTS_LIST, param) {
                @Override
                public void dealWithResult(String request) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(request);
                        String status = jsonObject.getString(JsonConfig.KEY_STATUS);
                        if("0".equals(status)){//success
                            String data = jsonObject.getString(JsonConfig.KEY_DATA);
                            if("null".equals(data)){
                                mCurrentPage--;
                            }else {
                                if(needClear){
                                    mCommentsList.clear();
                                }
                                mCommentsList = Json2List.getCommentsList(data, mCommentsList);
                                mCaseAppAdapter.notifyDataSetChanged();
                            }
                        }else{
                            mCurrentPage--;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mCustomDialog.cancel();
                    mXListView.stopLoadMore();
                    mXListView.stopRefresh();
                }
            };
            mCustomDialog.show();
            postDataTask.execute();
        }else{
            mXListView.stopLoadMore();
            mXListView.stopRefresh();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentPage = 0;
        loadData(true);
    }
}
