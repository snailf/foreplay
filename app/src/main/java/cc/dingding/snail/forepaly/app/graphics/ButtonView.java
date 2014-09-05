package cc.dingding.snail.forepaly.app.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by admin on 14-8-18.
 */
public class ButtonView extends BaseTouchView{
    private final String TAG = "ButtonView";
    private ButtonModel mButtonModel = null;
    private Paint mPaint = null;
    private RectF mRectF = null;

    private boolean mFlag = false;

    private Paint mTextPaint = null;
    private View mView = null;
    public ButtonView(View view, ButtonModel buttonModel){
        this.mButtonModel = buttonModel;
        //背景画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mButtonModel.getBackgroundColor());
        mView = view;
        //字体画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTypeface(Typeface.SANS_SERIF);
        mTextPaint.setFakeBoldText(false);
        mTextPaint.setStrokeWidth(mButtonModel.getTextGap());
        mTextPaint.setTextSize(mButtonModel.getTextSize());
        mTextPaint.setColor(mButtonModel.getTextColor());
    }

    public void setRectF(RectF rectF){
        mRectF = rectF;
        initRange((int)rectF.left, (int)rectF.top, (int)rectF.right, (int)rectF.bottom);
    }
    public boolean onTouchEvent(MotionEvent event) {
        if (hasTouched(event)) {
            Log.e(TAG, "down:" + this.toString());
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                mPaint.setColor(mButtonModel.getPressedBackgroundColor());
                mView.invalidate((int)mRectF.left, (int)mRectF.top, (int)mRectF.right, (int)mRectF.bottom);
                mFlag = true;
                return true;
            }
            /*
            else if(event.getAction() == MotionEvent.ACTION_UP){
                Log.e(TAG, "up:" + this.toString());
                if(mFlag){
                    mFlag = false;
                    return true;
                }
            }
            //*/
        }else{
            mFlag = false;
            mPaint.setColor(mButtonModel.getBackgroundColor());
            mView.invalidate((int)mRectF.left, (int)mRectF.top, (int)mRectF.right, (int)mRectF.bottom);
        }
        return false;
    }

    public void onDraw(Canvas canvas) {
        if(mRectF != null && mPaint != null){
            canvas.drawRect(mRectF, mPaint);
            canvas.drawText(mButtonModel.getText(), mButtonModel.getLeft(mRectF), mButtonModel.getBottom(mRectF), mTextPaint);
        }
    }
}
