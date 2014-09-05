package cc.dingding.snail.forepaly.app.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;

/**
 * Created by koudejian on 14-7-31.
 */
public class FavoriteView extends ImageView{

    private boolean mIsFrovrite = false;

    private Drawable mFavoriteDrawable = null;
    private Drawable mNotFavoriteDrawable = null;

    public FavoriteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public FavoriteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public FavoriteView(Context context) {
        super(context);
        init();
    }

    private void init(){
        mFavoriteDrawable = MainApplication.getInstance().getResources().getDrawable(R.drawable.favorite_selected);
        mNotFavoriteDrawable = MainApplication.getInstance().getResources().getDrawable(R.drawable.favorite);
        setFrovite(mIsFrovrite);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setFrovite(boolean flag){
        if(mIsFrovrite != flag){
            mIsFrovrite = flag;
            setBackground(mIsFrovrite ? mFavoriteDrawable : mNotFavoriteDrawable);
        }
    }
    public boolean getValue(){
        return this.mIsFrovrite;
    }
}
