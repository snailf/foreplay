package cc.dingding.snail.forepaly.app.helper.bitmap.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by snail on 2014-01-09.
 */
public class MemoryCache {
    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private static LruCache<String, Bitmap> mMemoryCache = null;

    private static MemoryCache INSTANCE = null;

    private MemoryCache(){
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 3;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    public static MemoryCache getInstance(){
        if(INSTANCE == null){
            INSTANCE = new MemoryCache();
        }
        return INSTANCE;
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key LruCache的键，这里传入图片名称。
     * @return 对应传入键的Bitmap对象，或者null。
     */
    public Bitmap get(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key  LruCache的键，这里传入图片的URL地址。
     * @param bitmap LruCache的键，这里传入从网络上下载的Bitmap对象。
     */
    public void put(String key, Bitmap bitmap) {
        if (get(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }
}
