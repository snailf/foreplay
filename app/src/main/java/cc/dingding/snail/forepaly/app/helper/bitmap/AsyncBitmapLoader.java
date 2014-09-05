package cc.dingding.snail.forepaly.app.helper.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cc.dingding.snail.forepaly.app.helper.bitmap.cache.MemoryCache;
import cc.dingding.snail.forepaly.app.helper.bitmap.config.Config;
import cc.dingding.snail.forepaly.app.helper.bitmap.listener.BitMapLoadListener;
import cc.dingding.snail.forepaly.app.helper.bitmap.listener.BitMapLoadedListener;
import cc.dingding.snail.forepaly.app.helper.bitmap.model.ImageModel;
import cc.dingding.snail.forepaly.app.helper.bitmap.zoom.MiniBitmap;

public class AsyncBitmapLoader {
    private final String TAG = "asyncBitmapLoader";
    private String mImageUrl = "";
    private ImageView mImageView = null;
    private String mImageName = "";
    private ImageModel mImage = null;
    private BitMapLoadedListener mBitMapLoadedListener = null;
    private BitMapLoadListener mBitMapLoadListener = null;
    public AsyncBitmapLoader(ImageModel image){
        this.mImageUrl = image.getUrl();
        this.mImageName = getImageName(mImageUrl, image);
        this.mImage = image;
        mBitMapLoadListener = new BitMapLoadListener() {
            @Override
            public void OnSuccessed(Bitmap bitmap) {
                //下载完成，保存并设置图片
                //is need cut image
                if(mImage.isCut()){
                    try {
                        //原图存入SD卡
                        String name = getImageName(mImageUrl);
                        saveBitMap(bitmap, name);
//                        MemoryCache.getInstance().put(name, bitmap);      //put into memory cache
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //裁剪
                    bitmap = MiniBitmap.cutBitmap(bitmap, mImage.getWidth(), mImage.getHeight());
                }
                if(mBitMapLoadedListener != null){
                    mBitMapLoadedListener.OnSuccessed(bitmap);
                }
                if(mImageView != null){
                    mImageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
                }
                //放入内存
                MemoryCache.getInstance().put(mImageName, bitmap);
                try {
                    //存入SD卡
                    saveBitMap(bitmap,mImageName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public AsyncBitmapLoader(ImageModel image, ImageView iv) {
        this(image);
        this.mImageView = iv;
    }

    /**
     * auto load bitmap
     * @param image
     * @param listener
     */
    public AsyncBitmapLoader(ImageModel image, BitMapLoadedListener listener){
        this(image);
        setBitMapLoadedListener(listener);
    }

    /**
     * 图片加载完成监听
     * @param listener
     */
    public AsyncBitmapLoader setBitMapLoadedListener(BitMapLoadedListener listener){
        mBitMapLoadedListener = listener;
        return this;
    }

    /**
     * 开始下载图片，下载完成自动加载
     */
    public void start(){
        if(!"".equals(mImageUrl)){
            //1.read image cache in HashMap
            Log.d(TAG, "step:1");
            if(!LoadImageFromCache()){//failed
                //2.read image from SD card
                Log.d(TAG, "step:2");
                if(!LoadImageFromSDcard()){//failed
                    //3.download image form server
                    Log.d(TAG, "step:3");
                    AsyncBitMapLoaderManager.getInstance().add(new DownloadImageTask(mImageUrl, mBitMapLoadListener));
                }
            }
        }
    }
    /**
     * get image names in SD card
     * @param url
     * @return
     */
    public String getImageName(String url, ImageModel image){
        url = url.replaceAll("/", "");
        url = url.replaceAll(":", "");
        
        if(image.isCut()){
            url = image.getWidth() + "x" + image.getHeight() + url;
        }
        return url;
    }
    public String getImageName(String url){
        url = url.replaceAll("/", "");
        url = url.replaceAll(":", "");
        return url;
    }
    private Boolean LoadImageFromCache(){
        Bitmap bitmap = MemoryCache.getInstance().get(mImageName);
        if(bitmap != null){//cache is exist
            Log.d(TAG, "cache: " + mImageName);
            if(mBitMapLoadedListener != null){
                mBitMapLoadedListener.OnSuccessed(bitmap);
            }
            if(mImageView != null){
                mImageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }
            return true;
        }else{
            return false;
        }
    }
    private Boolean LoadImageFromSDcard(){
         File temp = new File(Environment.getExternalStorageDirectory() + Config.IMAGE_CACHE_DIR + mImageName);
         if(temp.exists()){//read file into memory if file is exist on SDcard
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + Config.IMAGE_CACHE_DIR + mImageName);
                if(mBitMapLoadedListener != null){
                    mBitMapLoadedListener.OnSuccessed(bitmap);
                }
                if(mImageView != null){
                    mImageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
                }
                MemoryCache.getInstance().put(mImageName, bitmap);      //put into memory cache
                Log.d(TAG, "sdCard: " + mImageName);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 保存图片
     * @param img
     * @param fileName
     * @throws java.io.IOException
     */
    public void saveBitMap(Bitmap img,String fileName) throws IOException {
        Log.d(TAG, "image: " + Config.IMAGE_CACHE_DIR + fileName);
        createDir(Environment.getExternalStorageDirectory() + Config.IMAGE_CACHE_DIR);
        File myCaptureFile = new File( Environment.getExternalStorageDirectory() + Config.IMAGE_CACHE_DIR + fileName);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            try {
                img.compress(Bitmap.CompressFormat.PNG, 100, bos);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * create dir at direction
     * @param dir
     */
    public void createDir(String dir){
        File file = new File(dir);
        if (!file.exists()) {
            try {
                //按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
