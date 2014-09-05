package cc.dingding.snail.forepaly.app.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import cc.dingding.snail.forepaly.app.R;


public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    /**
     * 点击任意位置关闭软键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {// 在指派Touch事件时拦截，由于安卓的Touch事件是自顶而下的，所以Activity是第一响应者
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {// 类型为Down才处理，还有Move/Up之类的
            if (this.getCurrentFocus() != null) {// 获取当前焦点
                CloseSoftInput(getCurrentFocus());
            }
        }
        return super.dispatchTouchEvent(ev);// 继续指派Touch事件，如果这里不执行基类的dispatchTouchEvent，事件将不会继续往下传递
    }
    protected void CloseSoftInput(View view) { // 关闭软键盘
        if (view != null) {
            if (view.getWindowToken() != null) {
                InputMethodManager imm;
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(this.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }else{

            }
        }

    }
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    /**
     * 弹出提示消息框
     * @param msg
     */
    public void popMessage(String msg){
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        AsyncBitMapLoaderManager.getInstance().clear();
    }
}
