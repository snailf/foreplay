package cc.dingding.snail.forepaly.app.controllers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;

/**
 * Created by koudejian on 14-7-31.
 */
public class PersonalTabBannerController extends BaseController {
    private final String TAG = "PersonalTabBannerController";

    private int mLength = 0;
    private View[] mTabView = {null, null, null};
    private TextView[] mCountView = {null, null, null};
    private TextView[] mTextView = {null, null, null};

    public interface OnCheckedListener{
        public void onChecked(int i);
    }

    private OnCheckedListener mOnCheckedListener = null;

    public void setOnCheckedListener(OnCheckedListener onCheckedListener) {
        this.mOnCheckedListener = onCheckedListener;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for(int i = 0; i < mLength; i++){
                if(v == mTabView[i]){
                    checkTab(i);
                    break;
                }
            }
        }
    };

    public PersonalTabBannerController(Context context, View view) {
        super(context, view);
        init();
    }

    @Override
    public void init() {
        Log.e(TAG, "init()");
        mLength = 3;

        int[] tabView = {R.id.history_rl, R.id.favorite_rl, R.id.comments_rl};
        for(int i = 0; i < mLength; i++){
            mTabView[i] = findViewById(tabView[i]);
            mTabView[i].setOnClickListener(mOnClickListener);
        }

        int[] countRes = {R.id.history_count, R.id.favorite_count, R.id.comments_count};
        for(int i = 0; i < mLength; i++){
            mCountView[i] = (TextView) findViewById(countRes[i]);
        }

        int[] textRes = {R.id.history_text, R.id.favorite_text, R.id.comments_text};
        for(int i = 0; i < mLength; i++){
            mTextView[i] = (TextView) findViewById(textRes[i]);
        }

        initCountsData();
        //init to check item 0.
        checkTab(0);
    }

    public void initCountsData() {
        //初始化counts数据.
        mCountView[0].setText(MainApplication.gUser.getHistoryCounts());
        mCountView[1].setText(MainApplication.gUser.getFavoriteCounts());
        mCountView[2].setText(MainApplication.gUser.getCommentscounts());
    }

    public void checkTab(int position){
        if(position < mLength){
            if(mOnCheckedListener != null){
                mOnCheckedListener.onChecked(position);
            }
            for(int i = 0; i < mLength; i++){
                if(position == i){
                    mTabView[i].setBackgroundColor(mContext.getResources().getColor(R.color.personal_tab_bgcolor_selected));
                    mCountView[i].setTextColor(mContext.getResources().getColor(R.color.personal_tab_text_selected));
                    mTextView[i].setTextColor(mContext.getResources().getColor(R.color.personal_tab_text_selected));
                }else{
                    mTabView[i].setBackgroundColor(mContext.getResources().getColor(R.color.personal_tab_bgcolor));
                    mCountView[i].setTextColor(mContext.getResources().getColor(R.color.personal_tab_text));
                    mTextView[i].setTextColor(mContext.getResources().getColor(R.color.personal_tab_text));
                }
            }
        }
    }
}
