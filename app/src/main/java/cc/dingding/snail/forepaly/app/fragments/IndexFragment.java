package cc.dingding.snail.forepaly.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.adapters.CaseAdapter;
import cc.dingding.snail.forepaly.app.config.JsonConfig;
import cc.dingding.snail.forepaly.app.config.UrlConfig;
import cc.dingding.snail.forepaly.app.controllers.IndexHeaderController;
import cc.dingding.snail.forepaly.app.factorys.Json2List;
import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitMapLoaderManager;
import cc.dingding.snail.forepaly.app.models.CaseModel;
import cc.dingding.snail.forepaly.app.network.PostDataTask;
import cc.dingding.snail.forepaly.app.network.PostParameter;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.views.CustomDialog;
import cc.dingding.snail.forepaly.app.views.xlist.XListView;

/**
 * Created by koudejian on 14-7-30.
 */
public class IndexFragment extends BaseFragment {
    private View mView = null;
    private IndexHeaderController mIndexHeaderController = null;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private XListView mXListView = null;
    private int mCurrentPage = 1;
    private int mStart = 0;
    private LinkedList<CaseModel> mCaseList = new LinkedList<CaseModel>();
    private CaseAdapter mCaseAdapter = null;
    private int mWhich = 1;
    private TextViews mTvMsg = null;
    private Context mContext = null;
    private CustomDialog mCustomDialog = null;

    /**
     * pop textview
     */
    private class TextViews {
        private TextView textView = null;
        private LinearLayout linearLayout = null;

        public TextViews(TextView textView, LinearLayout linearLayout){
            this.textView = textView;
            this.linearLayout = linearLayout;
        }

        public void setVisibility(int visibility){
            if(linearLayout != null){
                linearLayout.setVisibility(visibility);
            }
        }
        public void setText(String text){
            if(textView != null){
                textView.setText(text);
            }
        }

    }
    public void setNavigationDrawerFragment(NavigationDrawerFragment navigationDrawerFragment){
        mNavigationDrawerFragment = navigationDrawerFragment;
    }
    private final XListView.IXListViewListener mIXListViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            Log.e("test", "refresh");
            mCurrentPage = 1;
            loadData(true);
            mCaseList.clear();
        }

        @Override
        public void onLoadMore() {
            Log.e("test", "loadmore");
            mCurrentPage++;
            loadData(false);
//            mCustomDialog.show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView  = (View) inflater.inflate(R.layout.fragment_index, container, false);
        mContext = mView.getContext();
        mIndexHeaderController = new IndexHeaderController(getActivity(), mView);
        mIndexHeaderController.setOnMenuClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavigationDrawerFragment.openDrawer();
            }
        });
        mIndexHeaderController.setOnButtonCheckedListener(new IndexHeaderController.OnButtonCheckedListener() {
            @Override
            public void onChecked(int which) {
                mWhich = which;
                loadData(true);
            }
        });
        mCustomDialog = new CustomDialog(mContext, CustomDialog.DIALOG_THEME_WAIT_NOT_CANCEL);

        mXListView = (XListView) mView.findViewById(R.id.xlistview);
        mXListView.setXListViewListener(mIXListViewListener);
        mXListView.setPullLoadEnable(true);

        mCaseAdapter = new CaseAdapter(mContext, mCaseList);
        mXListView.setAdapter(mCaseAdapter);

        mCurrentPage = 1;
        loadData(true);

        return mView;
    }

    private void loadData(final boolean needClear){
        if(DeviceUtils.isNetworkConnected(getActivity())){
            // post 参数
            PostParameter param = new PostParameter();
            param.add("uid", MainApplication.gUser.getUid());
            param.add("page", mCurrentPage + "");
            String url = "";
            param.add("tag_id", MainApplication.gTag);
            param.add("start", mStart + "");
            url = (mWhich == 1) ? UrlConfig.GET_CASE_LIST_BY_RECOMMEND : UrlConfig.GET_CASE_LIST_BY_THROUGH;

            PostDataTask postDataTask = new PostDataTask(url, param) {
                @Override
                public void dealWithResult(String request) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(request);
                        String status = jsonObject.getString(JsonConfig.KEY_STATUS);
                        if("0".equals(status)){//success
                            String data = jsonObject.getString(JsonConfig.KEY_DATA);
                            if("null".equals(data)){
                                popMessages("更新失败...");
                                mCurrentPage--;
                            }else {
                                if(needClear){
                                    mCaseList.clear();
                                }
                                mCaseList = Json2List.getCaseList(data, mCaseList);
                                mCaseAdapter.notifyDataSetChanged();
                                if(mWhich != 1 && needClear){
                                    String time = mCaseList.getFirst().getVtime();
                                    popMessages("随机穿越到" + time.substring(0, 10));
                                }else {
                                    popMessages("已更新数据 " + mCaseList.size() + "条...");
                                }
                            }
                        }else{
                            mCurrentPage--;
                            popMessages("更新失败...");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mCustomDialog.cancel();
                    mXListView.stopLoadMore();
                    mXListView.stopRefresh();
                }
            };
            if(needClear){
                mCustomDialog.show();
            }
            AsyncBitMapLoaderManager.getInstance().clear();
            postDataTask.execute();
        }
    }
    private void popMessages(String msg){
        if(mTvMsg == null){
            mTvMsg = new TextViews((TextView) mView.findViewById(R.id.message), (LinearLayout) mView.findViewById(R.id.message_rl));
        }
        if(msg != null){
            mTvMsg.setText(msg);
            mTvMsg.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    mTvMsg.setVisibility(View.INVISIBLE);
                }
            }, 2000);
        }
    }

    public void onTagChecked(int tag){
        Log.e("test", "tag:" + tag);
        mCurrentPage = 1;
        loadData(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mXListView != null){
            mXListView.stopLoadMore();
            mXListView.stopRefresh();
        }
    }
}
