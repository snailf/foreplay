package cc.dingding.snail.forepaly.app.models;

/**
 * Created by koudejian on 14-8-22.
 */
public class CommentsModel {
    private String id = null;
    private String times = null;
    private String comments = null;
    private String avatar = null;
    private String nick = null;

    public CommentsModel(String id, String times, String comments, String avatar, String nick){
        setId(id);
        setTimes(times);
        setComments(comments);
        setAvatar(avatar);
        setNick(nick);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getId() {
        return id;
    }

    public String getTimes() {
        return times;
    }

    public String getComments() {
        return comments;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNick() {
        return nick;
    }
}
