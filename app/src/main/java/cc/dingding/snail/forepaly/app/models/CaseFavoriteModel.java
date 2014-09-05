package cc.dingding.snail.forepaly.app.models;

/**
 * Created by koudejian on 14-8-20.
 */
public class CaseFavoriteModel extends CaseModel {

    private String favoriteId = null;
    private String favoriteTimes = null;

    public CaseFavoriteModel(String id, String name, String logo, String vtime, String vno, String vname, String flag, String approves, String comments, String url, String favoriteId, String favoriteTimes) {
        super(id, name, logo, vtime, vno, vname, flag, approves, comments, url);
        setFavoriteId(favoriteId);
        setFavoriteTimes(favoriteTimes);
    }
    public CaseFavoriteModel(String id, String name, String logo, String vtime, String vno, String vname, String flag, String approves, String comments, String url) {
        super(id, name, logo, vtime, vno, vname, flag, approves, comments, url);
    }

    public String getFavoriteId() {
        return favoriteId;
    }

    public String getFavoriteTimes() {
        return favoriteTimes;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    public void setFavoriteTimes(String favoriteTimes) {
        this.favoriteTimes = favoriteTimes;
    }
}
