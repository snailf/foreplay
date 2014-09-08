package cc.dingding.snail.forepaly.app.cache;

import android.content.SharedPreferences;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

import cc.dingding.snail.forepaly.app.MainApplication;
import cc.dingding.snail.forepaly.app.config.Config;
import cc.dingding.snail.forepaly.app.models.CaseModel;
import cc.dingding.snail.forepaly.app.utils.StringUtil;

/**
 * Created by koudejian on 14-7-29.
 */
public class SharedCache {
    /**
     * 预览case
     */
    public static CaseModel gCurrentCase = null;

    /**
     * 评论是否成功
     */
    public static boolean haveComment = false;
    public static boolean haveCommit = false;
    //tag 缓存
    private static final String tagTemp = "[{\"id\":\"1\",\"name\":\"\\u63a8\\u8350\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140823185843_08549816.png\"},{\"id\":\"2\",\"name\":\"\\u56fe\\u4e66\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140823185730_05475085.png\"},{\"id\":\"3\",\"name\":\"\\u5546\\u54c1\\u6307\\u5357\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140906132817_14565112.png\"},{\"id\":\"4\",\"name\":\"\\u6559\\u80b2\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140823190011_86700810.png\"},{\"id\":\"5\",\"name\":\"\\u5a31\\u4e50\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140906133100_97808734.png\"},{\"id\":\"6\",\"name\":\"\\u8d22\\u52a1\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140906132656_06214422.png\"},{\"id\":\"7\",\"name\":\"\\u7f8e\\u98df\\u4f73\\u996e\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140906132744_24608363.png\"},{\"id\":\"8\",\"name\":\"\\u6e38\\u620f\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140906133448_80160896.png\"},{\"id\":\"9\",\"name\":\"\\u5065\\u5eb7\\u5065\\u7f8e\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140906132732_45253983.png\"},{\"id\":\"10\",\"name\":\"\\u751f\\u6d3b\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140906132910_43647301.png\"},{\"id\":\"11\",\"name\":\"\\u533b\\u7597\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140906133042_04294150.png\"},{\"id\":\"12\",\"name\":\"\\u97f3\\u4e50\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140823191905_08684440.png\"},{\"id\":\"13\",\"name\":\"\\u5bfc\\u822a\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140823191928_23312836.png\"},{\"id\":\"14\",\"name\":\"\\u65b0\\u95fb\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140906133002_91968713.png\"},{\"id\":\"15\",\"name\":\"\\u62a5\\u520a\\u6742\\u5fd7\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140906132633_65521587.png\"},{\"id\":\"16\",\"name\":\"\\u6444\\u5f71\\u4e0e\\u5f55\\u50cf\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140906133524_47561668.png\"},{\"id\":\"17\",\"name\":\"\\u6548\\u7387\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140906133015_42688928.png\"},{\"id\":\"18\",\"name\":\"\\u53c2\\u8003\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140906132714_45521871.png\"},{\"id\":\"19\",\"name\":\"\\u793e\\u4ea4\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140824002006_16362017.png\"},{\"id\":\"20\",\"name\":\"\\u4f53\\u80b2\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140824002024_02625829.png\"},{\"id\":\"21\",\"name\":\"\\u65c5\\u6e38\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140824002045_23536864.png\"},{\"id\":\"22\",\"name\":\"\\u5de5\\u5177\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140824002102_37439843.png\"},{\"id\":\"23\",\"name\":\"\\u5929\\u6c14\",\"icon\":\"http:\\/\\/218.244.156.133\\/foreplay\\/admin\\/logo\\/20140824002151_63012884.png\"}]";
    private static final String tagKey = "tag_cache";
    private static String tagCache = "";
    public static String getTagCache(){
        if("".equals(tagCache)){//返回内存缓存
            SharedPreferences setting = MainApplication.getInstance().getSharedPreferences(Config.PREFS_NAME, 0);
            tagCache = setting.getString(tagKey, "");
            if("".equals(tagCache)){ //获取本地数据
                AssetManager am =  MainApplication.getInstance().getAssets();
                try {
                    InputStream temp = am.open("tag_list.dat");
                    tagCache = StringUtil.inputStream2String(temp);
                    setTagCache(tagCache);
                } catch (IOException e) {
                    tagCache = tagTemp;
                    e.printStackTrace();
                }
            }
        }

        return tagCache;
    }

    public static void setTagCache(String str){
        SharedPreferences setting = MainApplication.getInstance().getSharedPreferences(Config.PREFS_NAME, 0);
        setting.edit().putString(tagKey, str).commit();
    }

}
