package cc.dingding.snail.forepaly.app.controllers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import cc.dingding.snail.forepaly.app.R;

/**
 * Created by koudejian on 14-7-31.
 */
public class IndexHeaderController extends BaseController {
    private final String TAG = "IndexHeaderController";
    private View mBtnMenuRl = null;
    public IndexHeaderController(Context context, View view) {
        super(context, view);
        init();
    }
    private RadioButton mRecommend = null;
    private RadioButton mThrough = null;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checkButton(buttonView, isChecked);
        }
    };
    private OnButtonCheckedListener mOnButtonCheckedListener = null;
    public void setOnButtonCheckedListener(OnButtonCheckedListener listener){
        mOnButtonCheckedListener = listener;
    }
    public interface OnButtonCheckedListener{
        public void onChecked(int witch);
    }
    @Override
    public void init() {
        Log.e(TAG, "init()");
        mBtnMenuRl = mView.findViewById(R.id.menu_rl);
        mRecommend = (RadioButton) mView.findViewById(R.id.recommend);
        mThrough = (RadioButton) mView.findViewById(R.id.through);
        mRecommend.setChecked(true);
        mRecommend.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mThrough.setOnCheckedChangeListener(mOnCheckedChangeListener);

    }

    public void setOnMenuClickListener(View.OnClickListener onMenuClickListener) {
        if(mBtnMenuRl == null){
            mBtnMenuRl = mView.findViewById(R.id.menu_rl);
        }
        mBtnMenuRl.setOnClickListener(onMenuClickListener);
    }
    private void checkButton(View v, boolean checked){
        if(!checked){
            return;
        }
        int witch = 0;
        if(v == mRecommend){
            witch = 1;
            mThrough.setChecked(false);
        }else if(v == mThrough){
            witch = 2;
            mRecommend.setChecked(false);
        }
        if(mOnButtonCheckedListener != null){
            mOnButtonCheckedListener.onChecked(witch);
        }
    }
}
