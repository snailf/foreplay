package cc.dingding.snail.forepaly.app.models;

/**
 * Created by koudejian on 14-8-20.
 */
public class CaseCommentModel extends CaseModel {

    private String commentId = null;
    private String commentTimes = null;
    private String commentContent = null;

    public CaseCommentModel(String id, String name, String logo, String vtime, String vno, String vname, String flag, String approves, String comments, String url, String commentId, String commentTimes, String commentContent) {
        super(id, name, logo, vtime, vno, vname, flag, approves, comments, url);
        setCommentId(commentId);
        setCommentTimes(commentTimes);
        setCommentContent(commentContent);
    }
    public CaseCommentModel(String id, String name, String logo, String vtime, String vno, String vname, String flag, String approves, String comments, String url) {
        super(id, name, logo, vtime, vno, vname, flag, approves, comments, url);
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setCommentTimes(String commentTimes) {
        this.commentTimes = commentTimes;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getCommentTimes() {
        return commentTimes;
    }

    public String getCommentContent() {
        return commentContent;
    }
}
