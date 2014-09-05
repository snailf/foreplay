package cc.dingding.snail.forepaly.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.adapters.PersonalTabPagerAdapter;
import cc.dingding.snail.forepaly.app.config.UrlConfig;
import cc.dingding.snail.forepaly.app.controllers.PersonalHeaderController;
import cc.dingding.snail.forepaly.app.controllers.PersonalTabBannerController;
import cc.dingding.snail.forepaly.app.factorys.AdapterFactory;
import cc.dingding.snail.forepaly.app.views.ViewPagerView;

/**
 * Created by koudejian on 14-7-30.
 */
public class PersonalFragment extends Fragment {
    private View mView = null;
    private PersonalHeaderController mPersonalHeaderController = null;
    private PersonalTabBannerController mPersonalTabBannerController = null;
    private ViewPagerView mViewPager = null;
    private PersonalTabPagerAdapter mPersonalTabPagerAdapter = null;
    private List<String> mCaseUrls = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("lift", "onCreateView");
        mView  = (View) inflater.inflate(R.layout.fragment_personal, container, false);
        //header and userinfo
        mPersonalHeaderController = new PersonalHeaderController(this.getActivity(), mView);

        //tab banner
        mPersonalTabBannerController = new PersonalTabBannerController(this.getActivity(), mView);

        mCaseUrls = new ArrayList<String>();
        mCaseUrls.add(UrlConfig.GET_USR_HISTORY_LIST);
        mCaseUrls.add(UrlConfig.GET_USR_FAVORITE_LIST);
        mCaseUrls.add(UrlConfig.GET_USR_COMMENTS_LIST);

        mPersonalTabBannerController.setOnCheckedListener(new PersonalTabBannerController.OnCheckedListener() {
            @Override
            public void onChecked(int i) {
                mViewPager.setCurrentItem(i);
                mPersonalTabPagerAdapter.checkedItem(i);
            }
        });

        mViewPager = (ViewPagerView) mView.findViewById(R.id.viewPager);
        mPersonalTabPagerAdapter = new PersonalTabPagerAdapter(getActivity(), mCaseUrls);
        mViewPager.setAdapter(mPersonalTabPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPagerChangeListener());

        return mView;
    }

    class ViewPagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            mPersonalTabBannerController.checkTab(arg0);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("lift", "onResume");
        mPersonalTabPagerAdapter.checkedItem(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("lift", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("lift", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AdapterFactory.clear();
        Log.e("lift", "onDestory");
    }
}
