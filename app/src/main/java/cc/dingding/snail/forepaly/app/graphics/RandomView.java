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
import java.util.Random;

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
    private int mMargin = 20;
    private int mHeight = 60;
    private int mWidth = 80;
    private int mMarginTop = 60;
    private int mScreenWidth = 400;
    private int mWhiteSpace = 150;
    private int mTextSize = 22;
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
        mMarginTop = (int) MainApplication.getInstance().getResources().getDimension(R.dimen.search_margin_top);
        mMargin = (int) MainApplication.getInstance().getResources().getDimension(R.dimen.search_view_item_margin);
        mHeight = (int) MainApplication.getInstance().getResources().getDimension(R.dimen.search_view_item_height);
        mWidth = (int) MainApplication.getInstance().getResources().getDimension(R.dimen.search_view_item_width);
        mMarginTop = (int) MainApplication.getInstance().getResources().getDimension(R.dimen.search_view_item_margin_top);
        mWhiteSpace = (int) MainApplication.getInstance().getResources().getDimension(R.dimen.search_view_item_white_space);
        mTextSize = (int) MainApplication.getInstance().getResources().getDimension(R.dimen.search_view_item_text_size);
//        mWhiteSpace = 180;
    }

    public void setButtonModels(List<ButtonModel> buttonModels){
        mButtonViews.clear();
        mButtonModels = buttonModels;
        mLength = mButtonModels.size();
        int line = 0;
        int offset = 0;
        //button 位置
        Random random =new Random();
        for(int i = 0; i < mLength; i++){
            ButtonModel buttonModel = mButtonModels.get(i).setTextSize(mTextSize);
            ButtonView  buttonView = new ButtonView(this, buttonModel);
            int minWidth = (buttonModel.getText().length() > 2) ? mWidth*3/2 : mWidth;
            int width = minWidth + random.nextInt(mWidth);
            int available = mScreenWidth - mWhiteSpace - width - offset;
            if(available < 0){//不在一行内
                if(available + width > minWidth){
                    width = available + width - mMargin;
                }else {
                    line++;
                    offset = 0;
                }
            }
            int left = offset + random.nextInt(mMargin) + mWhiteSpace/2;
            int right = offset + width + mWhiteSpace/2;
            offset += width + mMargin;
            int top = mMarginTop + line * (mHeight + mMargin) + random.nextInt(mMargin);
            int bottom = top + mHeight;
            buttonView.setRectF(new RectF(left, top, right, bottom));
            mButtonViews.add(buttonView);
        }
        invalidate();
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
