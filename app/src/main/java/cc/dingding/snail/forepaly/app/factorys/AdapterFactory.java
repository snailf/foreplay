package cc.dingding.snail.forepaly.app.factorys;

import android.content.Context;

import cc.dingding.snail.forepaly.app.adapters.CommentsCaseAdapter;
import cc.dingding.snail.forepaly.app.adapters.FavoriteCaseAdapter;
import cc.dingding.snail.forepaly.app.adapters.HistoryCaseAdapter;
import cc.dingding.snail.forepaly.app.adapters.UserCaseAdapter;
import cc.dingding.snail.forepaly.app.views.xlist.XListView;

/**
 * Created by koudejian on 14-8-21.
 */
public class AdapterFactory {
    private static UserCaseAdapter[] baseAdapterMap = {null, null, null};
    private static boolean[] isAvailable = {false, false, false};
    public static UserCaseAdapter getAdapter(int i, Context context, XListView xListView){
        UserCaseAdapter baseAdapter = baseAdapterMap[i];
        if(baseAdapter == null){
            if(i == 1){
                baseAdapter = new FavoriteCaseAdapter(xListView, context, null);
            }else if(i == 2){
                baseAdapter = new CommentsCaseAdapter(xListView, context, null);
            }else{//i == 0
                baseAdapter = new HistoryCaseAdapter(xListView, context, null);
            }
            baseAdapterMap[i] = baseAdapter;
        }
        return baseAdapter;
    }
    public static void clear(){
        for(int i = 0; i < baseAdapterMap.length; i++){
            baseAdapterMap[i] = null;
        }
    }
    public static void setAvailable(int i){
        if(i < isAvailable.length && i >= 0){
            isAvailable[i] = true;
        }
    }
    public static boolean isAvailable(int i){
        if(i < isAvailable.length && i >= 0){
            return isAvailable[i];
        }
        return false;
    }
}
