package cc.dingding.snail.forepaly.app.models;

/**
 * Created by koudejian on 14-7-29.
 */
public class BaseModel {
    private String id = null;
    private String name = null;


    public BaseModel(String id, String name){
        this.id = id;
        this.name = name;
    }

    public BaseModel() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
