package cc.dingding.snail.forepaly.app.helper.bitmap.listener;

import android.graphics.Bitmap;

/**
 *
 * 加载图片完成时监听事件
 * Created by koudejian on 14-1-14.
 *
 */
public interface BitMapLoadedListener {
    //加载完成时回调
    void OnSuccessed(Bitmap bitmap);
}
