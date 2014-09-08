package cc.dingding.snail.forepaly.app.config;

/**
 * Created by koudejian on 14-7-29.
 * api config
 */
public class UrlConfig {

    private final static String HOST = "http://218.244.156.133/foreplay/";

    public final static String GET_TAG_LIST = HOST + "index.php?r=api/getTagList";
    public final static String GET_CASE_LIST_BY_TAG = HOST + "index.php?r=api/getAppListByTag";
    public final static String GET_CASE_LIST_BY_RECOMMEND = HOST + "index.php?r=api/getAppListByRecommend";
    public final static String GET_CASE_LIST_BY_THROUGH = HOST + "index.php?r=api/getPassThroughList";
    public final static String GET_CASE_LIST_BY_KEYWORD = HOST + "index.php?r=api/getAppListByKeyword";
    /**
     * 登录
     */
    public final static String USER_LOGIN_URL = HOST + "index.php?r=api/registe";
    /**
     * 收藏
     */
    public final static String USER_SET_FAVORITE = HOST + "index.php?r=api/setFavorite";
    public final static String USER_REMOVE_FAVORITE = HOST + "index.php?r=api/removeFavorite";
    /**
     * 点赞
     */
    public final static String USER_ADD_APPROVE = HOST + "index.php?r=api/addApprove";
    /**
     * 评论点赞
     */
    public final static String USER_ADD_COMMENTS_APPROVE = HOST + "index.php?r=api/addCommentApprove";
    /**
     * 时间轴
     */
    public final static String GET_CASE_APP_LIST_TIMELINE = HOST + "index.php?r=api/getOneAppList";

    public final static String GET_CASE_COMMENTS_LIST = HOST + "index.php?r=api/getAppCommentList";
    /**
     * 评论
     */
    public final static String ADD_COMMENTS_URL = HOST + "index.php?r=api/addComment";
    //个人中心
    public final static String GET_USR_HISTORY_LIST = HOST + "index.php?r=api/getHistoryList";
    public final static String GET_USR_FAVORITE_LIST = HOST + "index.php?r=api/getFavoriteList";
    public final static String GET_USR_COMMENTS_LIST = HOST + "index.php?r=api/getCommentList";

    public final static String Get_USR_UPDATE_COUNTS = HOST + "index.php?r=api/getUserUpdateCounts";
    //搜索关键词列表
    public final static String GET_KEY_WORD_LIST = HOST + "index.php?r=api/getKeywordList";
    /**
     * 上传用户浏览记录
     */
    public final static String SET_USER_HISTORY_URL = HOST + "index.php?r=api/setHistory";
}
