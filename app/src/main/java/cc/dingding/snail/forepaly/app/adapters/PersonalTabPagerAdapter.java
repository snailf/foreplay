package cc.dingding.snail.forepaly.app.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.config.JsonConfig;
import cc.dingding.snail.forepaly.app.factorys.AdapterFactory;
import cc.dingding.snail.forepaly.app.factorys.Json2List;
import cc.dingding.snail.forepaly.app.network.PostDataTask;
import cc.dingding.snail.forepaly.app.network.PostParameter;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;
import cc.dingding.snail.forepaly.app.views.CustomDialog;
import cc.dingding.snail.forepaly.app.views.xlist.XListView;


/**
 * 界面左右切换 viewPager的适配器 com.frogtech.happyapp.ViewPagerAdapter
 * 
 * @author 孙浩 <br/>
 *         create at 2013-8-25 下午5:02:00
 *
 * @author koudejian <br/>
 *         modified at 2014-8-20 早上4:29:45
 */
public class PersonalTabPagerAdapter extends PagerAdapter {

    private Context mContext;
    private final List<View> mListViews;
    private final XListView[] mXListViews = {null, null, null};
    LayoutInflater mInflater = null;
    private List<String> mCaseUrls = null;
    private CustomDialog mCustomDialog = null;
    public PersonalTabPagerAdapter(Context context, List<String> list) {
        this.mContext = context;
        mListViews = new ArrayList<View>();
        mCaseUrls = list;
        mCustomDialog = new CustomDialog(mContext, CustomDialog.DIALOG_THEME_WAIT_NOT_CANCEL);
        initView();
    }

    private void initView() {
        mListViews.clear();
        mInflater = LayoutInflater.from(mContext);
        for (int i = 0; i < 3; i++) {
            View view = mInflater.inflate(R.layout.item_personal_tab_view, null);
            mListViews.add(view);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    /**
     * 销毁Item
     *
     * @param container
     * @param position
     * @param object
     * @see android.support.v4.view.PagerAdapter#destroyItem(android.view.ViewGroup,
     *      int, java.lang.Object)
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public void update(){
        initView();
    }

    /**
     * 对Viewpage添加当前显示View
     *
     * @param container
     * @param position
     * @return
     * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view.ViewGroup,
     *      int)
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListViews.get(position));
        return initCurView(position);
    }

    private View initCurView(int position) {
        View view = mListViews.get(position);
        XListView listView = (XListView) view.findViewById(R.id.xListView);
        listView.setPullLoadEnable(true);
        mXListViews[position] = listView;
        UserCaseAdapter caseAdapter = AdapterFactory.getAdapter(position, mContext, listView);
        if(caseAdapter.getOnChangedListener() == null){
            caseAdapter.setOnChangedListener(new UserCaseAdapter.OnChangedListener() {
                @Override
                public void onChanged(int j) {//更新
                    if(j < mXListViews.length){
                        loadDatas(j);
                    }
                    Log.e("test", "onChanged:" + j);
                }
            });
        }
        listView.setAdapter(caseAdapter);
        listView.setXListViewListener(caseAdapter);
//        mXListViews[i].setAdapter(caseAdapter);
//        mXListViews[i].setXListViewListener(caseAdapter);
        return view;
    }
    //点击滑动切换item
    public void checkedItem(int i){
        UserCaseAdapter caseAdapter = AdapterFactory.getAdapter(i, mContext, mXListViews[i]);
        if(caseAdapter.getList() == null || !AdapterFactory.isAvailable(i)){
            AdapterFactory.setAvailable(i);
            loadDatas(i);
        }else{
            caseAdapter.notifyDataSetChanged();
        }
    }
    private void loadDatas(final int i){
        if(DeviceUtils.isNetworkConnected(mContext) && i < mXListViews.length){
            final int page = AdapterFactory.getAdapter(i, mContext, mXListViews[i]).getPage();
            // post 参数
            PostParameter param = new PostParameter();

            param.add("uid", MainApplication.gUser.getUid());
            param.add("page", page + "");

            PostDataTask postDataTask = new PostDataTask(mCaseUrls.get(i), param) {
                @Override
                public void dealWithResult(String request) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(request);
                        String status = jsonObject.getString(JsonConfig.KEY_STATUS);
                        if("0".equals(status)){//success
                            String data = jsonObject.getString(JsonConfig.KEY_DATA);
                            if("null".equals(data)){
                                popMessage("暂无数据记录!");
                            }else {
                                UserCaseAdapter caseAdapter = AdapterFactory.getAdapter(i, mContext, mXListViews[i]);
                                LinkedList caseList = Json2List.getUserCaseList(data, caseAdapter.getList(), i, page);
                                if(caseList != null && mXListViews[i] != null){
                                    caseAdapter.setList(caseList);
                                    caseAdapter.notifyDataSetChanged();
//                                    mXListViews[i].setAdapter(caseAdapter);
//                                    mXListViews[i].setXListViewListener(caseAdapter);
                                }
                            }
                        }else{
                            popMessage("errors!");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mCustomDialog.cancel();
                    stopLoad();
                }
            };
            mCustomDialog.show();
            postDataTask.execute();
        }else{
            popMessage("暂无网络链接...");
            stopLoad();
        }
    }
    private void popMessage(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
    public void stopLoad(){
        int length = mXListViews.length;
        for(int i = 0; i < length; i++){
            if(mXListViews[i] != null){
                mXListViews[i].stopLoadMore();
                mXListViews[i].stopRefresh();
            }
        }
    }
}
