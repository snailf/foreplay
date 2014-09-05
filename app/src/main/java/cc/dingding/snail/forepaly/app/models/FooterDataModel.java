/*
 * Copyright (c) 2013. 1010.am
 *
 * You may obtain a copy of the License at
 *
 *      http://1010.am
 */
package cc.dingding.snail.forepaly.app.models;

import java.io.Serializable;

/**
 * 进行序列化 方便传递 <p>
 *  modified by <b>孙浩</b> on 14-2-16.
 * 
 * 
 * @author snail
 * 
 * 
 * @version 1.1
 */
public class FooterDataModel implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1078484621336710445L;
    private final int id;
    private final int id_selected;
    private String text;
    private boolean isNew = false;

    public FooterDataModel(int id, int id_selected) {
        this.id = id;
        this.id_selected = id_selected;
        isNew = false;
    }

    public FooterDataModel(int id, int id_selected, boolean flag) {
        this.id = id;
        this.id_selected = id_selected;
        isNew = flag;
    }

    public FooterDataModel(int id, int id_selected, String text) {
        this(id, id_selected);
        this.text = text;
    }

    public FooterDataModel(int id, int id_selected, String text, boolean flag) {
        this.id = id;
        this.id_selected = id_selected;
        this.text = text;
        isNew = flag;
    }

    public int getId() {
        return id;
    }

    public int getIdSelected() {
        return id_selected;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "";
    }

    public boolean getIsNew() {
        return isNew;
    }
}
