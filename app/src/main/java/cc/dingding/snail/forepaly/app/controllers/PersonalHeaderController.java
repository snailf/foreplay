package cc.dingding.snail.forepaly.app.controllers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.activitys.MainActivity;
import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitmapLoader;
import cc.dingding.snail.forepaly.app.helper.bitmap.model.ImageModel;

/**
 * Created by koudejian on 14-7-31.
 */
public class PersonalHeaderController extends BaseController {
    private final String TAG = "PersonalHeaderController";
    private TextView mTvTitle = null;
    private TextView mTvNick = null;
    private ImageView mIvAvatar = null;

    private View mLoginOut = null;
    public PersonalHeaderController(Context context, View view) {
        super(context, view);
        init();
    }

    @Override
    public void init() {
        Log.e(TAG, "init()");
        mTvTitle = (TextView) mView.findViewById(R.id.header_title);
        mTvTitle.setText("个人中心");

        mTvNick = (TextView) mView.findViewById(R.id.nick);
        mTvNick.setText(MainApplication.gUser.getNick());

        String avatar_url = MainApplication.gUser.getAvatar();
        mIvAvatar = (ImageView) mView.findViewById(R.id.avatar);
        new AsyncBitmapLoader(new ImageModel(avatar_url), mIvAvatar).start();

        mLoginOut = findViewById(R.id.header_right);
        mLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainApplication.logout();
                MainActivity.getInstance().onFooterClick(1);
            }
        });
    }

}
