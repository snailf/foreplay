package cc.dingding.snail.forepaly.app.network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class PostParameter {
    public List <NameValuePair> params = null;

    public PostParameter(){
        this.params = new ArrayList<NameValuePair>();
    }
    public void add(String key,String value){
        if(key!=null&&key.length()!=0){        //key is not null
            this.params.add(new BasicNameValuePair(key,value));
        }
    }
    public List <NameValuePair> getParas(){
        return this.params;
    }

    public String getUid(){
        String uid = "0";
        for (int i = 0; i < params.size(); i++) {
            NameValuePair temp = params.get(i);
            if("uid".equals(temp.getName())){
                uid = temp.getValue();
            }
        }
        return uid;
    }
}
