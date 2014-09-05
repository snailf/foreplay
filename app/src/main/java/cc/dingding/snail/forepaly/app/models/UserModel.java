package cc.dingding.snail.forepaly.app.models;

/**
 * Created by koudejian on 14-7-29.
 */
public class UserModel {
    private String openid = "";
    private String uid = "0";
    private String avatar = null;
    private String nick = null;

    private String favoriteCounts = "0";
    private String historyCounts = "0";
    private String commentscounts = "0";

    public UserModel(){}

    public UserModel(String oid, String uid, String nick, String avatar, String historyCounts, String favoriteCounts, String commentscounts){
        this(oid, uid, nick, avatar);
        setHistoryCounts(historyCounts);
        setFavoriteCounts(favoriteCounts);
        setCommentscounts(commentscounts);
    }
    public UserModel(String oid, String uid, String nick, String avatar){
        this.openid = oid;
        this.uid = uid;
        this.nick = nick;
        this.avatar = avatar;
    }
    public void setFavoriteCounts(String favoriteCounts) {
        this.favoriteCounts = favoriteCounts;
    }

    public void setHistoryCounts(String historyCounts) {
        this.historyCounts = historyCounts;
    }

    public void setCommentscounts(String commentscounts) {
        this.commentscounts = commentscounts;
    }

    public String getFavoriteCounts() {
        return favoriteCounts;
    }

    public String getHistoryCounts() {
        return historyCounts;
    }

    public String getCommentscounts() {
        return commentscounts;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNick() {

        return nick;
    }

    public String getAvatar() {

        return avatar;
    }

    public String getUid() {

        return uid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setUid(String uid) {

        this.uid = uid;
    }


    public void clear(){
        this.setAvatar("");
        this.setNick("");
        this.setUid("0");
        this.setCommentscounts("0");
        this.setFavoriteCounts("0");
        this.setHistoryCounts("0");
    }
}
