/*
 * Copyright (c) 2013. 1010.am
 *
 * You may obtain a copy of the License at
 *
 *      http://1010.am
 */

package cc.dingding.snail.forepaly.app.fragments;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.models.FooterDataModel;


/**
 * Created by sunkai on 13-9-2.
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements FooterFragment.FooterClickListner {

    protected abstract List<FooterDataModel> getItemPairs();

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
