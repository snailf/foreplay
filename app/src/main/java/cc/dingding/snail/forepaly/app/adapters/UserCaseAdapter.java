package cc.dingding.snail.forepaly.app.adapters;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.LinkedList;

import cc.dingding.snail.forepaly.app.views.xlist.XListView;

/**
 * Created by koudejian on 14-8-21.
 */
public abstract class UserCaseAdapter extends BaseAdapter implements XListView.IXListViewListener{

    protected int mPage = 0;
    public abstract LinkedList getList();
    public abstract void setList(LinkedList list);
    public abstract void clear();
    protected Context mContext = null;


    public int getPage(){
        return mPage;
    }
    public UserCaseAdapter(Context context){

        mContext = context;

    }
    public OnChangedListener mOnChangedListener = null;
    public void setOnChangedListener(OnChangedListener onChangedListener){
        mOnChangedListener = onChangedListener;
    }
    public OnChangedListener getOnChangedListener(){
        return mOnChangedListener;
    }
    public interface OnChangedListener{
        public void onChanged(int i);
    }

}
