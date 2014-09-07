package cc.dingding.snail.forepaly.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitMapLoaderManager;

/**
 * Created by koudejian on 14-9-7.
 */
public class BaseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AsyncBitMapLoaderManager.getInstance().clear();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
