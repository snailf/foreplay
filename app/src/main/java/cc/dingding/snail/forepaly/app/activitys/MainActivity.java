package cc.dingding.snail.forepaly.app.activitys;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.fragments.BaseFragmentActivity;
import cc.dingding.snail.forepaly.app.fragments.FooterFragment;
import cc.dingding.snail.forepaly.app.fragments.IndexFragment;
import cc.dingding.snail.forepaly.app.fragments.NavigationDrawerFragment;
import cc.dingding.snail.forepaly.app.fragments.PersonalFragment;
import cc.dingding.snail.forepaly.app.fragments.SearchFragment;
import cc.dingding.snail.forepaly.app.models.FooterDataModel;


public class MainActivity extends BaseFragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private static android.support.v4.app.FragmentManager fm = null;
    private FooterFragment mFooterFragment = null;

    private IndexFragment mIndexFragment;
    private PersonalFragment mPersonalFragment;
    private SearchFragment mSearchFragment;

    private static MainActivity INSTANCE;
    public static MainActivity getInstance(){
        return INSTANCE;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        INSTANCE = this;
        mNavigationDrawerFragment = (NavigationDrawerFragment)getFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout)findViewById(R.id.drawer_layout));
        mIndexFragment = new IndexFragment(mNavigationDrawerFragment);
        mPersonalFragment = new PersonalFragment();
        mSearchFragment = new SearchFragment();

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        mFooterFragment = new FooterFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("footdata", (Serializable) getItemPairs());
        // 向detailFragment传入参数
        mFooterFragment.setArguments(bundle);

        ft.add(R.id.fragment_radiogroup, mFooterFragment);
        ft.add(R.id.fragment_view, mIndexFragment);
        mFooterFragment.changeDisplayItem(1);
        ft.commit();

    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        Log.e("test", "" + number);
        MainApplication.gTag = String.valueOf(number);
        mFooterFragment.changeDisplayItem(1);
        mIndexFragment.onTagChecked(1);
    }

    @Override
    protected List<FooterDataModel> getItemPairs() {
        List<FooterDataModel> itemPairs = new ArrayList<FooterDataModel>();
        itemPairs.add(new FooterDataModel(R.drawable.foot_index_xml, R.drawable.footer_index_selected, "首页", false));
        itemPairs.add(new FooterDataModel(R.drawable.foot_search_xml, R.drawable.footer_search_selected, "搜索 ", false));
        itemPairs.add(new FooterDataModel(R.drawable.foot_personal_xml, R.drawable.footer_personal_selected, "我的 ", true));
        return itemPairs;
    }


    @Override
    public void onFooterClick(int position) {
        if(position == 3 && !MainApplication.isLogin()){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return;
        }
        mFooterFragment.changeDisplayItem(position);
        FragmentTransaction ftTemp = fm.beginTransaction();
        MainApplication.CURRENT = position;
        switch (position) {
            case 1:
                ftTemp.replace(R.id.fragment_view, mIndexFragment);
                break;
            case 2:
                ftTemp.replace(R.id.fragment_view, mSearchFragment);
                break;
            case 3:
                ftTemp.replace(R.id.fragment_view, mPersonalFragment);
                break;
        }
        ftTemp.commit();
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(MainApplication.CURRENT == 2){//SearchFragment
                if(mSearchFragment.onBackKeyDown()){//返回true消费该事件
                    return false;
                }
            }
            exitBy2Click(); // 调用双击退出函数
        }
        return false;
    }
    /**
     * 双击退出函数
     */
    private Boolean isGoingToExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isGoingToExit == false) {
            isGoingToExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isGoingToExit = false; // 取消退出
                }
            }, 2000);       // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            finish();
            System.exit(0);
        }
    }
}
