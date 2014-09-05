package cc.dingding.snail.forepaly.app.models;

import java.util.List;

/**
 * Created by koudejian on 14-7-31.
 */
public class CaseModel extends BaseModel {
    /**
     *
     {
         "id": "15",
         "app_name": "豆瓣客户端",
         "app_log": "http://218.244.156.133/foreplay/admin/logo/20140725193101_02891707.png",
         "version_time": "2013-12-21 00:00:00",
         "version_no": "1",
         "version_name": "v1.0.1",
         "iamges": [
         {
         "orders": "0",
         "image_url": "http://foreplay.oss-cn-hangzhou.aliyuncs.com/20140725193300_00836780.jpg"
         },
         {
         "orders": "0",
         "image_url": "http://foreplay.oss-cn-hangzhou.aliyuncs.com/20140725193305_54132156.jpg"
         }
         ],
         "is_favorite": "0"
     },
     */

    private String logo = null;
    private String vtime = null;
    private String vno = null;
    private String vname = null;
    private boolean isfavorite = false;
    private String approves = null;     //赞条数
    private String comments = null;     //评论条数
    private List<ImageUrlModel> images = null;
    private String downloadUrl = null;

    /**
     * basic constructor
     * @param id
     * @param name
     * @param logo
     * @param vtime
     * @param vno
     * @param vname
     * @param flag
     * @param approves
     * @param comments
     */
    public CaseModel(String id, String name, String logo, String vtime, String vno, String vname, String flag, String approves, String comments, String url){
        super(id, name);
        setLogo(logo);
        setVtime(vtime);
        setVname(vname);
        setVno(vno);
        setIsfavorite(flag);
        setApproves(approves);
        setComments(comments);
        setDownloadUrl(url);
    }
//    public CaseModel(String id, String name, String logo, String vtime, String vno, String vname, String flag, String approves, String comments, ){
//        this(id, name, logo, vtime, vno, vname, flag, approves, comments);
//    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public void setVno(String vno) {
        this.vno = vno;
    }

    public void setVtime(String vtime) {
        this.vtime = vtime;
    }

    public void setIsfavorite(String isfavorite) {
        this.isfavorite = "1".equals(isfavorite) ? true : false;
    }

    public void setApproves(String approves) {
        this.approves = approves;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setImages(List<ImageUrlModel> images) {
        this.images = images;
    }

    public List<ImageUrlModel> getImages() {
        return images;
    }

    public String getLogo() {
        return logo;
    }

    public String getVname() {
        return vname;
    }

    public String getVno() {
        return vno;
    }

    public String getVtime() {
        return vtime;
    }

    public boolean getIsfavorite(){
        return this.isfavorite;
    }

    public String getApproves() {
        return approves;
    }

    public String getComments() {
        return comments;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
