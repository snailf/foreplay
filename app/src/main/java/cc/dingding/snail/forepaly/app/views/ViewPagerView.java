package cc.dingding.snail.forepaly.app.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class ViewPagerView extends ViewPager {

    private boolean canScroll;
    private GestureDetector mGestureDetector;

    public ViewPagerView(Context context){
        super(context);
    }
    public ViewPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(new XScrollDetector());
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        canScroll = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP)
            canScroll = true;
        return super.onInterceptTouchEvent(ev)
                && mGestureDetector.onTouchEvent(ev);
    }
    class XScrollDetector extends SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
            if (canScroll)
                if (Math.abs(distanceX) >= Math.abs(distanceY))
                    canScroll = true;
                else
                    canScroll = false;
            return canScroll;
        }
    }
}
