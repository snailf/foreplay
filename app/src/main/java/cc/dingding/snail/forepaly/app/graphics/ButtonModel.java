package cc.dingding.snail.forepaly.app.graphics;

import android.graphics.Color;
import android.graphics.RectF;

/**
 * Created by admin on 14-8-18.
 */
public class ButtonModel {
    private int textColor = 0xffffffff;
    private int height = 65;
    private int width = 80;
    private int backgroundColor = 0xff00ffff;
    private int pressedBackgroundColor = 0xff000000;

    private String text = "音乐";
    private int textSize = 22;
    private int textGap = 1;

    public ButtonModel(int backgroundColor, String text){
        this.text = text;
        this.backgroundColor = backgroundColor;
        this.pressedBackgroundColor = backgroundColor - 0x90000000;
    }
    public ButtonModel(String backgroudColor, String text){
        this(Color.parseColor(backgroudColor), text );
    }

    public int getTextColor() {
        return textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public String getText() {
        return text;
    }

    public int getTextSize() {
        return textSize;
    }

    public int getTextGap() {
        return textGap;
    }
    public int getLeft(RectF rectF){
        return (int) (((rectF.right - rectF.left) - (textSize + textGap) * text.length()) / 2 + textGap + rectF.left);
    }
    public int getBottom(RectF rectF){
        return (int) (((rectF.bottom - rectF.top) - textSize) / 2 + textSize + rectF.top);
    }
}
