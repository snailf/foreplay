package cc.dingding.snail.forepaly.app.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;

/**
 * Created by admin on 14-8-18.
 */
public class RandomView extends View {
    private final String TAG = "RandomView";
    private List<ButtonModel> mButtonModels = null;
    private List<ButtonView> mButtonViews = null;

    private int mLength = 0;
    private int mMargin = 10;
    private int mHeight = 60;

    private int mScreenWidth = 400;
    private int mWhiteSpace = 150;
    private RandomButtonOnClickListener mRandomButtonOnClickListener = null;
    public void setRandomButtonOnClickListener(RandomButtonOnClickListener randomButtonOnClickListener){
        mRandomButtonOnClickListener = randomButtonOnClickListener;
    }

    public RandomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }
    public RandomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public RandomView(Context context) {
        super(context);
    }

    /**
     * init 参数
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        mButtonViews = new ArrayList<ButtonView>();
        mScreenWidth = DeviceUtils.deviceWidth(context);
        mWhiteSpace = (int) MainApplication.getInstance().getResources().getDimension(R.dimen.search_margin_size);
    }

    public void setButtonModels(List<ButtonModel> buttonModels){
        mButtonModels = buttonModels;
        mLength = mButtonModels.size();
        int left = (mScreenWidth - 80) / 2;
        //button 位置
        for(int i = 0; i < mLength; i++){
            ButtonView  buttonView = new ButtonView(this, mButtonModels.get(i));
            buttonView.setRectF(new RectF( left , 40 + i * (mHeight + mMargin), left + 80, 40 + (i+1) * (mHeight + mMargin) - mMargin));
            mButtonViews.add(buttonView);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i = 0; i < mLength; i++){
            mButtonViews.get(i).onDraw(canvas);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for(int i = 0; i < mLength; i++){
            if ( mButtonViews.get(i).onTouchEvent(event)) {
                if (mRandomButtonOnClickListener != null) {
                    mRandomButtonOnClickListener.OnRandomButtonClick(i);
                    Log.d(TAG, "click item :" + i);
                }
                break;
            };
        }
        return super.onTouchEvent(event);
    }


}
