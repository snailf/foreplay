package cc.dingding.snail.forepaly.app.graphics;

import android.util.Log;
import android.view.MotionEvent;


public abstract class BaseTouchView {

    protected int left,top,right,bottom;

    protected void initRange(int left, int top, int right, int bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public boolean hasTouched(MotionEvent event){
        if(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom){
            Log.e("BaseTouchView", "onTouchEvent :" + true);
            return true;
        }
        return false;
    }

}
