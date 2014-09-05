package cc.dingding.snail.forepaly.app.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

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
        GalleryAdapter galleryAdapter = new GalleryAdapter(this, SharedCache.gCurrentCase, mMyGallery);
        mMyGallery.setAdapter(galleryAdapter);

        mMyGallery.setOnItemSelectedListener(galleryAdapter.mOnSelectedListener);
        mMyGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDetailFooterController.show();
            }
        });

    }
}
