package cc.dingding.snail.forepaly.app.models;

/**
 * Created by koudejian on 14-7-29.
 */
public class CaseTagModel extends BaseModel {

    public CaseTagModel(String id, String name) {
        super(id, name);
    }

    public CaseTagModel(String id, String name, String icon){
        super(id, name);
        setIcon(icon);
    }

    private String icon = null;

    public CaseTagModel() {
        super();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "id=" + getId() + ";name=" + getName() + ";icon=" + getIcon();
    }
}
