package cc.dingding.snail.forepaly.app.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.umeng.socialize.sso.UMSsoHandler;

import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.adapters.GalleryAdapter;
import cc.dingding.snail.forepaly.app.cache.SharedCache;
import cc.dingding.snail.forepaly.app.controllers.DetailFooterController;
import cc.dingding.snail.forepaly.app.views.MyGallery;

/**
 * Created by koudejian on 14-8-6.
 */
public class DetailsActivity extends BaseActivity {
    private View mView = null;
    private DetailFooterController mDetailFooterController = null;

    private MyGallery mMyGallery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mView = findViewById(R.id.parent);

        mDetailFooterController = new DetailFooterController(this, mView);

        mMyGallery = (MyGallery) findViewById(R.id.gallery);

        mMyGallery.setSpacing((int) getResources().getDimension(R.dimen.gallery_gap));
        GalleryAdapter galleryAdapter = new GalleryAdapter(this, SharedCache.gCurrentCase, new GalleryAdapter.OnItemSelectedCallBack() {
            @Override
            public void onItemSelected(int i) {
                mDetailFooterController.setPositioin(i);
            }
        });
        mMyGallery.setAdapter(galleryAdapter);

        mMyGallery.setOnItemSelectedListener(galleryAdapter.mOnSelectedListener);
        mMyGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDetailFooterController.show();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        Log.e("test", "onActivityResult");
        UMSsoHandler ssoHandler = mDetailFooterController.getUMSocialService().getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

}
