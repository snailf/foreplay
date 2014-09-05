package cc.dingding.snail.forepaly.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Gallery;

/**
 * Created by koudejian on 14-2-13.
 */
public class MyGallery extends Gallery {
    private boolean canScroll;
    private GestureDetector mGestureDetector;

    public MyGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(new XScrollDetector());
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        canScroll = true;
    }
    /**
     * 一次滑动只滚动一张图片
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        if (velocityX > 0) {
            // 往左边滑动
            super.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
        } else {
            // 往右边滑动
            super.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
        }
        return false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP)
            canScroll = true;
        return super.onInterceptTouchEvent(ev)
                && mGestureDetector.onTouchEvent(ev);
    }
    class XScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (canScroll)
                if (Math.abs(distanceY) >= Math.abs(distanceX))
                    canScroll = true;
                else
                    canScroll = false;
            return canScroll;
        }
    }


}
