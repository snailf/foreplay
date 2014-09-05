package cc.dingding.snail.forepaly.app.helper.bitmap.listener;

import android.graphics.Bitmap;

/**
 *
 * 加载图片监听事件
 * Created by koudejian on 14-1-14.
 *
 */
public interface BitMapLoadListener {
    //开始加载时回调
    void OnSuccessed(Bitmap bitmap);
}
