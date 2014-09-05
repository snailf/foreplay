package cc.dingding.snail.forepaly.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import cc.dingding.snail.forepaly.app.models.CaseModel;
import cc.dingding.snail.forepaly.app.network.PostDataTask;
import cc.dingding.snail.forepaly.app.network.PostParameter;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.views.CustomDialog;
import cc.dingding.snail.forepaly.app.views.xlist.XListView;

/**
 * Created by koudejian on 14-7-30.
 */
public class IndexFragment extends Fragment {
    private View mView = null;
    private IndexHeaderController mIndexHeaderController = null;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private XListView mXListView = null;
    private int mCurrentPage = 1;
    private int mStart = 0;
    private LinkedList<CaseModel> mCaseList = null;
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

    public IndexFragment(NavigationDrawerFragment navigationDrawerFragment){
        mNavigationDrawerFragment = navigationDrawerFragment;
    }
    private final XListView.IXListViewListener mIXListViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            Log.e("test", "refresh");
            mCurrentPage = 1;
            loadData(mCurrentPage);
            mCaseList = null;
        }

        @Override
        public void onLoadMore() {
            Log.e("test", "loadmore");
            loadData(++mCurrentPage);
            mCustomDialog.show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                loadData(1);
            }
        });
        mCustomDialog = new CustomDialog(mContext, CustomDialog.DIALOG_THEME_WAIT_NOT_CANCEL);

        mXListView = (XListView) mView.findViewById(R.id.xlistview);
        mXListView.setXListViewListener(mIXListViewListener);
        mXListView.setPullLoadEnable(true);

        return mView;
    }

    private void loadData(int page){
        if(DeviceUtils.isNetworkConnected(getActivity())){
            // post 参数
            PostParameter param = new PostParameter();
            param.add("uid", MainApplication.gUser.getUid());
            param.add("page", page + "");
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
                                popMessages("update 0 counts datas!");
                                mCurrentPage--;
                            }else {
                                mCaseList = Json2List.getCaseList(data, mCaseList);
                                mCaseAdapter = new CaseAdapter(mContext, mCaseList);
                                mXListView.setAdapter(mCaseAdapter);
                                popMessages("update " + mCaseList.size() + " counts datas!");
                            }
                        }else{
                            mCurrentPage--;
                            popMessages("errors!");
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
        mCaseList = null;
        loadData(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(1);
    }
}
