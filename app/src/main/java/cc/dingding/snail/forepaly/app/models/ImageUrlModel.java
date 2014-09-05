package cc.dingding.snail.forepaly.app.models;

/**
 * Created by koudejian on 14-7-31.
 */
public class ImageUrlModel {
    private String url = null;
    private String id = null;
    private int order = 0;

    public ImageUrlModel(String url){
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public int getOrder() {
        return order;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
