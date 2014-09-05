package cc.dingding.snail.forepaly.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.helper.bitmap.AsyncBitmapLoader;
import cc.dingding.snail.forepaly.app.helper.bitmap.model.ImageModel;
import cc.dingding.snail.forepaly.app.models.CaseTagModel;

/**
 * Created by koudejian on 14-7-29.
 */
public class DrawerAdapter extends BaseAdapter {

    private Context mContext = null;
    private final LayoutInflater mInflater;
    private LinkedList<CaseTagModel> mList;
    public DrawerAdapter(Context context, LinkedList<CaseTagModel> list){
        mContext = context;
        mList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
//    private List<>

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CaseTagModel caseTagModel = mList.get(position);
        View view = mInflater.inflate(R.layout.item_drawer, null);
        TextView name = (TextView) view.findViewById(R.id.title);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);

        name.setText(caseTagModel.getName());
        new AsyncBitmapLoader(new ImageModel(caseTagModel.getIcon()), icon).start();
        return view;
    }
}
