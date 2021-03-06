package cc.dingding.snail.forepaly.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.adapters.CaseAdapter;
import cc.dingding.snail.forepaly.app.cache.SharedCache;
import cc.dingding.snail.forepaly.app.config.JsonConfig;
import cc.dingding.snail.forepaly.app.config.UrlConfig;
import cc.dingding.snail.forepaly.app.factorys.Json2List;
import cc.dingding.snail.forepaly.app.graphics.ButtonModel;
import cc.dingding.snail.forepaly.app.graphics.RandomButtonOnClickListener;
import cc.dingding.snail.forepaly.app.graphics.RandomView;
import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitMapLoaderManager;
import cc.dingding.snail.forepaly.app.listeners.ShakeListener;
import cc.dingding.snail.forepaly.app.models.CaseModel;
import cc.dingding.snail.forepaly.app.network.PostDataTask;
import cc.dingding.snail.forepaly.app.network.PostParameter;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.views.CustomDialog;
import cc.dingding.snail.forepaly.app.views.xlist.XListView;

/**
 * Created by koudejian on 14-7-30.
 */
public class SearchFragment extends BaseFragment {
    private View mView = null;
    private View mSearchView = null;

    private String mKeyWord = "";
    private RelativeLayout mRlSearch = null;
    private EditText mEtKey = null;

    private Context mContext = null;

    private CustomDialog mCustomDialog = null;

    private XListView mXListView = null;
    private int mCurrentPage = 1;
    private LinkedList<CaseModel> mCaseList = new LinkedList<CaseModel>();;
    private CaseAdapter mCaseAdapter = null;
    private TextViews mTvMsg = null;

    private ShakeListener mShakeListener = null;

    private List<ButtonModel> mButtonModelList = null;
    private RandomView mRandomView = null;

    private Boolean mCanBack = false;
    public boolean onBackKeyDown() {
        if(mCanBack){
            switchView(false);
            return true;
        }
        return false;
    }
    private void switchView(boolean isVisible){
        mSearchView.setVisibility( isVisible ? View.VISIBLE : View.INVISIBLE);
        mCanBack = isVisible;
        if(mShakeListener != null){
            if(isVisible){
                mShakeListener.stop();
            }else{
                mShakeListener.start();
            }
        }
    }
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

    private final XListView.IXListViewListener mIXListViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            Log.e("test", "refresh");
            mCurrentPage = 1;
            loadData(mCurrentPage);
        }

        @Override
        public void onLoadMore() {
            Log.e("test", "loadmore");
            loadData(mCurrentPage++, true);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView  = inflater.inflate(R.layout.fragment_search, container, false);
        mContext = getActivity();
        mSearchView = mView.findViewById(R.id.search_result_rl);
        mSearchView.setVisibility(View.GONE);

        mEtKey = (EditText) mView.findViewById(R.id.key);
        mEtKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String key = mEtKey.getText().toString();
                if(!"".equals(key)){
                    if(mSearchView.getVisibility() == View.INVISIBLE){
                        switchView(true);
                    }
                }else{
                    switchView(false);
                }
            }
        });

        mRlSearch = (RelativeLayout) mView.findViewById(R.id.search_rl);
        mRlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = mEtKey.getText().toString();
                if(!"".equals(key)){
                    mKeyWord = key;
                    loadData(1);
                }else{
                    switchView(false);
                }
            }
        });

        mCustomDialog = new CustomDialog(mContext, CustomDialog.DIALOG_THEME_WAIT_NOT_CANCEL);

        mXListView = (XListView) mView.findViewById(R.id.xlistview);
        mXListView.setXListViewListener(mIXListViewListener);
        mXListView.setPullLoadEnable(true);

        mCaseAdapter = new CaseAdapter(mContext, mCaseList);
        mXListView.setAdapter(mCaseAdapter);

        //search key
        mRandomView = (RandomView) mView.findViewById(R.id.myrandom_view);
        mRandomView.setRandomButtonOnClickListener(new RandomButtonOnClickListener() {
            @Override
            public void OnRandomButtonClick(int which) {
                if(which < mButtonModelList.size()){
                    mKeyWord = mButtonModelList.get(which).getText();
                    mEtKey.setText(mKeyWord);
                    loadData(1);
                }
            }
        });
        return mView;
    }
    private void loadKeyword(){
        if(DeviceUtils.isNetworkConnected(mContext)){
            // post 参数
            PostDataTask postDataTask = new PostDataTask(UrlConfig.GET_KEY_WORD_LIST, new PostParameter()) {
                @Override
                public void dealWithResult(String request) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(request);
                        String status = jsonObject.getString(JsonConfig.KEY_STATUS);
                        if("0".equals(status)){//success
                            String data = jsonObject.getString(JsonConfig.KEY_DATA);
                            if(!"null".equals(data)){
                                SharedCache.setKeywordCache(data);
                                mButtonModelList = Json2List.getSearchKeyList(data);
                                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in_xml);
                                animation.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {}
                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        mRandomView.setButtonModels(mButtonModelList);
                                        mRandomView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_out_xml));
                                    }
                                    @Override
                                    public void onAnimationRepeat(Animation animation) {}
                                });
                                mRandomView.startAnimation(animation);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(mShakeListener != null){
                                mShakeListener.start();
                            }
                        }
                    }, 2000);

                }
            };
            if(mShakeListener != null){
                mShakeListener.stop();
            }
            postDataTask.execute();
        }
    }
    private void loadData(int page){
        loadData(page, false);
    }
    private void loadData(int page, final boolean flag){
        if(DeviceUtils.isNetworkConnected(mContext)){
            // post 参数
            PostParameter param = new PostParameter();
            param.add("uid", MainApplication.gUser.getUid());
            param.add("key", mKeyWord + "");
            param.add("page", page + "");

            PostDataTask postDataTask = new PostDataTask(UrlConfig.GET_CASE_LIST_BY_KEYWORD, param) {
                @Override
                public void dealWithResult(String request) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(request);
                        String status = jsonObject.getString(JsonConfig.KEY_STATUS);
                        if("0".equals(status)){//success
                            String data = jsonObject.getString(JsonConfig.KEY_DATA);
                            if("null".equals(data)){
                                popMessages("暂无搜索记录...");
                                if(mCurrentPage > 1){
                                    mCurrentPage--;
                                }

                            }else {
                                if(!flag && mCaseList != null){
                                    mCaseList.clear();
                                }
                                switchView(true);
                                mCaseList = Json2List.getCaseList(data, mCaseList);
                                mCaseAdapter.notifyDataSetChanged();
                                popMessages("已更新数据 " + mCaseList.size() + "条...");
                            }
                        }else{
                            if(mCurrentPage > 1){
                                mCurrentPage--;
                            }
                            popMessage("搜索失败，请稍候再试...");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mCustomDialog.cancel();
                    mXListView.stopLoadMore();
                    mXListView.stopRefresh();
                }
            };
            if(!flag){
                mCustomDialog.show();
            }
            AsyncBitMapLoaderManager.getInstance().clear();
            postDataTask.execute();
        }else{
            popMessage("暂无网络链接...");
            mXListView.stopLoadMore();
            mXListView.stopRefresh();
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
    /**
     * 弹出消息
     * @param msg
     */
    private void popMessage(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mShakeListener != null){
            mShakeListener.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mButtonModelList = Json2List.getSearchKeyList(SharedCache.getKeywordCache());
        mRandomView.setButtonModels(mButtonModelList);

        if(mShakeListener == null){//init shake listener
            mShakeListener = new ShakeListener(mContext);
            mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
                @Override
                public void onShake() {
                    loadKeyword();
                }
            });
        }
        mShakeListener.start();
    }
}
