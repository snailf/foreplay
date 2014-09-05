package cc.dingding.snail.forepaly.app.controllers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitmapLoader;
import cc.dingding.snail.forepaly.app.helper.bitmap.model.ImageModel;

/**
 * Created by koudejian on 14-7-31.
 */
public class CommonHeaderController extends BaseController {
    private final String TAG = "CommonHeaderController";
    private View mBtnReturnRl = null;
    private View mBtnRightRl = null;
    private ImageView mIvLogo = null;
    private TextView mTvTitle = null;

    private String mTitle = "";
    private String mLogo = "";


    private ImageView mIvLeftBtn = null;
    private ImageView mIVRightBtn = null;

    public CommonHeaderController(Context context, View view, String title, String logo) {
        super(context, view);
        mTitle = title;
        mLogo = logo;
        init();
    }
    public CommonHeaderController(Context context, View view, String title) {
        this(context, view, title, null);
    }
    @Override
    public void init() {
        Log.e(TAG, "init()");
        mBtnReturnRl = mView.findViewById(R.id.return_btn_rl);
        mBtnRightRl = mView.findViewById(R.id.right_btn_rl);

        mTvTitle = (TextView) mView.findViewById(R.id.title);
        mTvTitle.setText(mTitle);
        mIvLogo = (ImageView) mView.findViewById(R.id.logo);
        if(mLogo != null && !"".equals(mLogo)){
            new AsyncBitmapLoader(new ImageModel(mLogo), mIvLogo).start();
        }else{
            mIvLogo.setVisibility(View.GONE);
        }

        mIvLeftBtn = (ImageView) mView.findViewById(R.id.left_btn);
        mIVRightBtn = (ImageView) mView.findViewById(R.id.right_btn);
    }
    public void setRightBtn(int ResId){
        if(mIVRightBtn == null){
            mIVRightBtn = (ImageView) mView.findViewById(R.id.right_btn);
        }
        mIVRightBtn.setBackgroundDrawable(mContext.getResources().getDrawable(ResId));
    }
    public void setLeftBtn(int ResId){
        if(mIvLeftBtn == null){
            mIvLeftBtn = (ImageView) mView.findViewById(R.id.left_btn);
        }
        mIvLeftBtn.setBackgroundDrawable(mContext.getResources().getDrawable(ResId));
    }
    public void setOnMenuClickListener(View.OnClickListener onRetrunClickListener, View.OnClickListener onRightClickListener) {
        if(mBtnReturnRl == null){
            mBtnReturnRl = mView.findViewById(R.id.menu_rl);
        }
        if(mBtnRightRl == null){
            mBtnRightRl = mView.findViewById(R.id.right_btn_rl);
        }
        if(onRetrunClickListener != null){
            mBtnReturnRl.setOnClickListener(onRetrunClickListener);
        }
        if(onRightClickListener != null) {
            mBtnRightRl.setOnClickListener(onRightClickListener);
        }
    }
}
