package cc.dingding.snail.forepaly.app.views;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import cc.dingding.snail.forepaly.app.R;


public class CustomDialog {
    public static String TAG = "CustomDialog";

    /**
     * 等待对话框,可以取消
     */
    public static final int DIALOG_THEME_WAIT_CAN_CANCEL = 0;
    /**
     * 等待对话框,不可以取消
     */
    public static final int DIALOG_THEME_WAIT_NOT_CANCEL = 1;

    /**
     * 等待对话框,可以取消
     */
    public static final int DIALOG_THEME_ONE_BUTTON_CAN_CANCEL = 2;
    /**
     * 等待对话框,不可以取消
     */
    public static final int DIALOG_THEME_ONE_BUTTON_NOT_CANCEL = 3;

    /**
     * 等待对话框,可以取消
     */
    public static final int DIALOG_THEME_TWO_BUTTON_CAN_CANCEL = 4;
    /**
     * 等待对话框,不可以取消
     */
    public static final int DIALOG_THEME_TWO_BUTTON_NOT_CANCEL = 5;

    private boolean canCancel;
    private int layout;
    private int style;
    private Dialog dialog = null;
    private final Context context;
    private final int theme;
    private OnCustomDialogListener mListener;

    public CustomDialog(Context context, int theme) {
        this.context = context;
        this.theme = theme;
        init(theme);
    }

    private void init(int theme) {
        canCancel = (theme % 2) == 0;
        if (canCancel) {
            style = R.style.cancelDialogTheme;
        } else {
            style = R.style.DialogTheme;
        }
        dialog = new Dialog(context, style) {

            @Override
            protected void onStop() {
                super.onStop();
                if (mListener != null) {
                    mListener.onFinish();
                }
            }

            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                if (mListener != null) {
                    mListener.onKeyDown();
                }
                return super.onKeyDown(keyCode, event);
            }

        };
        switch (theme) {
            case DIALOG_THEME_TWO_BUTTON_CAN_CANCEL:
            case DIALOG_THEME_TWO_BUTTON_NOT_CANCEL:
                layout = R.layout.dialog_two_btn;
                break;

            case DIALOG_THEME_ONE_BUTTON_CAN_CANCEL:
            case DIALOG_THEME_ONE_BUTTON_NOT_CANCEL:
                layout = R.layout.dialog_one_btn;
                break;

            case DIALOG_THEME_WAIT_CAN_CANCEL:
            case DIALOG_THEME_WAIT_NOT_CANCEL:
                layout = R.layout.window_layout;
                break;

        }

        dialog.setContentView(layout);
    }

    /**
     * 设置提示内容
     * 
     * @param message
     * @return
     */
    public CustomDialog setMessage(String message) {
        if (theme == DIALOG_THEME_WAIT_CAN_CANCEL) {
            return this;
        }
        TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
        title.setText(message);
        return this;
    }

    public CustomDialog setButton(String btn1Str, OnClickListener btn1OnClickListener, String btn2Str,
            final OnClickListener btn2OnClickListener) {
        this.setButton(btn1Str, btn1OnClickListener);
        if (theme != DIALOG_THEME_TWO_BUTTON_CAN_CANCEL && theme != DIALOG_THEME_TWO_BUTTON_NOT_CANCEL) {
            return this;
        }
        TextView btn2 = (TextView) dialog.findViewById(R.id.dialog_btn2);
        btn2.setText(btn2Str);
        btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (btn2OnClickListener != null) {
                    btn2OnClickListener.onClick(view);
                }
            }
        });
        return this;

    }

    public CustomDialog setButton(String btnStr, final OnClickListener btnOnClickListener) {
        if (theme == DIALOG_THEME_WAIT_CAN_CANCEL || theme == DIALOG_THEME_WAIT_NOT_CANCEL) {
            return this;
        }
        TextView btn1 = (TextView) dialog.findViewById(R.id.dialog_btn1);
        btn1.setText(btnStr);
        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (btnOnClickListener != null) {
                    btnOnClickListener.onClick(view);
                }
            }
        });
        return this;
    }

    public CustomDialog setOnCustomDialogListener(OnCustomDialogListener listener) {
        this.mListener = listener;
        return this;
    }

    public CustomDialog show() {
        try {
            dialog.show();
        } catch (Exception e) {
            Log.e(TAG, "dialog err", e);
        }
        return this;
    }

    public void dismiss() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
            Log.e(TAG, "dialog err", e);
        }
    }

    public void cancel() {
        try {
            dialog.cancel();
        } catch (Exception e) {
            Log.e(TAG, "dialog err", e);
        }
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    public abstract class OnCustomDialogListener {
        public void onFinish() {

        }

        public void onKeyDown() {

        }
    }

}
