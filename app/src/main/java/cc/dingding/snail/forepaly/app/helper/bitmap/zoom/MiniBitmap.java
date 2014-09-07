package cc.dingding.snail.forepaly.app.helper.bitmap.zoom;

import android.graphics.Bitmap;
import android.util.Log;

public class MiniBitmap {
    /**
     * 剪切出图片并缩放到指定大小
     * @param source
     * @param width
     * @param height
     * @return Bitmap
     * @throws IllegalArgumentException    if the x, y, width, height values are outside of the dimensions of the source bitmap, or width is <= 0, or height is <= 0
     * @modified koudejian at 2014-01-14, for throws error width or height must > 0;
     *
     * double ratioW = swidth * 1.000 / width;
     * double ratioH = sheight * 1.000 / height;
     */
    public static Bitmap cutBitmap(Bitmap source, int width, int height) {
        int swidth = source.getWidth();
        int sheight = source.getHeight();

        double sratio = swidth * 1.000 / sheight;
        double ratio = width * 1.000 / height;
        int x = 0, y = 0;
        int cw = 0, ch = 0;
        if(sratio > ratio){//裁剪宽度
            ch = sheight;
            cw = (int) (sheight * ratio);
            x = Math.abs(swidth - cw) / 2;
        }else{
            cw = swidth;
            ch = (int) (swidth / ratio);
            y = Math.abs(sheight - ch) / 2;
        }
        Log.e("test", sratio + "-" + ratio + ";" + width + "=" + height + ":" + swidth + "=" + sheight + ";" + x + ":" + y + ":" + cw + ":" + ch);
        Bitmap temp = Bitmap.createBitmap(source, x, y, cw, ch);
//        source.recycle();
        return temp;
        /*
        int swidth = source.getWidth();
        int sheight = source.getHeight();

        double ratioW = swidth * 1.000 / width;
        double ratioH = sheight * 1.000 / height;

        int x = 0, y = 0;
        int cw = 0, ch = 0;
        if (ratioW > ratioH) {
            ch = sheight;
            cw = (int) (width * ratioH);
            x = (swidth - cw) / 2;
//            cw += x;
        } else {
            cw = swidth;
            ch = (int) (height * ratioW);
            y = (sheight - ch) / 2;
//            ch += y;
        }
//        Log.d("test", ratioW + "-" + ratioH + ";" + width + "=" + height + ":" + swidth + "=" + sheight + ";" + x + ":" + y + ":" + cw + ":" + ch);
        Bitmap temp = Bitmap.createBitmap(source, x, y, cw, ch);
//        source.recycle();
        return temp;
        //*/
    }
}
