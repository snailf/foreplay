package cc.dingding.snail.forepaly.app.factorys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cc.dingding.snail.forepaly.app.config.JsonConfig;
import cc.dingding.snail.forepaly.app.graphics.ButtonModel;
import cc.dingding.snail.forepaly.app.models.CaseCommentModel;
import cc.dingding.snail.forepaly.app.models.CaseFavoriteModel;
import cc.dingding.snail.forepaly.app.models.CaseHistoryModel;
import cc.dingding.snail.forepaly.app.models.CaseModel;
import cc.dingding.snail.forepaly.app.models.CaseTagModel;
import cc.dingding.snail.forepaly.app.models.CommentsModel;
import cc.dingding.snail.forepaly.app.models.ImageUrlModel;

/**
 * Created by koudejian on 14-7-29.
 */
public class Json2List {
    /**
     *
     * @param jsonData
     * @param result
     * @return
     */
    public static LinkedList<CaseTagModel> getCaseTagList(String jsonData, LinkedList<CaseTagModel> result){
        if(result == null){
            result = new LinkedList<CaseTagModel>();
        }
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            int length = jsonArray.length();
            for(int i = 0; i < length; i++){
                JSONObject temp = (JSONObject) jsonArray.get(i);
                String id = temp.getString(JsonConfig.KEY_TAG_ID);
                String name = temp.getString(JsonConfig.KEY_TAG_NAME);
                String icon = temp.getString(JsonConfig.KEY_TAG_ICON);
                result.add(new CaseTagModel(id, name, icon));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param jsonData
     * @param result
     * @return
     */
    public static LinkedList<CaseModel> getCaseList(String jsonData, LinkedList<CaseModel> result){
        if(result == null){
            result = new LinkedList<CaseModel>();
        }
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            int length = jsonArray.length();
            for(int i = 0; i < length; i++){
                JSONObject temp = (JSONObject) jsonArray.get(i);
                String id = temp.getString(JsonConfig.KEY_CASE_ID);
                String name = temp.getString(JsonConfig.KEY_CASE_NAME);
                String logo = temp.getString(JsonConfig.KEY_CASE_LOGO);
                String vtime = temp.getString(JsonConfig.KEY_CASE_VERSION_TIME);
                String vno = temp.getString(JsonConfig.KEY_CASE_VERSION_NO);
                String vname = temp.getString(JsonConfig.KEY_CASE_VERSION_NAME);
                String flag = temp.getString(JsonConfig.KEY_CASE_FAVORITE);
                String approve_count = temp.getString(JsonConfig.KEY_CASE_APPROVE_COUNT);
                String comments_count = temp.getString(JsonConfig.KEY_CASE_COMMENTS_COUNT);
                String images = temp.getString(JsonConfig.KEY_CASE_IAMGES);
                String urls = temp.getString(JsonConfig.KEY_CASE_DOWNLOAD_URL);
                CaseModel caseModel = new CaseModel(id, name, logo, vtime, vno, vname, flag, approve_count, comments_count, urls);
                if(!JsonConfig.DATA_NULL.equals(images)){
                    JSONArray imageArray = new JSONArray(images);
                    int imageLength = imageArray.length();
                    List<ImageUrlModel> imageUrlModels = new ArrayList<ImageUrlModel>();
                    for(int j = 0; j < imageLength; j++){
                        JSONObject imageObj = (JSONObject) imageArray.get(j);
                        String url = imageObj.getString(JsonConfig.KEY_IMAGE_URL);
                        imageUrlModels.add(new ImageUrlModel(url));
                    }
                    caseModel.setImages(imageUrlModels);
                }
                result.add(caseModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static LinkedList getUserCaseList(String jsonData, LinkedList result, int position){
        return getUserCaseList(jsonData, result, position, 1);
    }
    /**
     *
     * @param jsonData
     * @param result
     * @param position 0=>history, 1=>favorite, 2=>comment, null
     * @return LinkedList<CaseModel>
     */
    public static LinkedList getUserCaseList(String jsonData, LinkedList result, int position, int page){
        if(result == null){
            if(position == 0){
                result = new LinkedList<CaseHistoryModel>();
            }else if(position == 1){
                result = new LinkedList<CaseFavoriteModel>();
            }else if(position == 2){
                result = new LinkedList<CaseCommentModel>();
            }else {
                result = new LinkedList<CaseModel>();
            }
        }else {
            if(page == 0){
                result.clear();
            }
        }
        try {
            JSONArray jsonArray = new JSONArray(jsonData);

            int length = jsonArray.length();
            for(int i = 0; i < length; i++){
                List<ImageUrlModel> imageUrlModels = new ArrayList<ImageUrlModel>();
                boolean haveImage = false;
                JSONObject temp = (JSONObject) jsonArray.get(i);
                String id = temp.getString(JsonConfig.KEY_CASE_ID);
                String name = temp.getString(JsonConfig.KEY_CASE_NAME);
                String logo = temp.getString(JsonConfig.KEY_CASE_LOGO);
                String vtime = temp.getString(JsonConfig.KEY_CASE_VERSION_TIME);
                String vno = temp.getString(JsonConfig.KEY_CASE_VERSION_NO);
                String vname = temp.getString(JsonConfig.KEY_CASE_VERSION_NAME);
                String flag = temp.getString(JsonConfig.KEY_CASE_FAVORITE);
                String approve_count = temp.getString(JsonConfig.KEY_CASE_APPROVE_COUNT);
                String comments_count = temp.getString(JsonConfig.KEY_CASE_COMMENTS_COUNT);
                String images = temp.getString(JsonConfig.KEY_CASE_IAMGES);
                String urls = temp.getString(JsonConfig.KEY_CASE_DOWNLOAD_URL);
                if(!JsonConfig.DATA_NULL.equals(images)){
                    JSONArray imageArray = new JSONArray(images);
                    int imageLength = imageArray.length();

                    for(int j = 0; j < imageLength; j++){
                        JSONObject imageObj = (JSONObject) imageArray.get(j);
                        String url = imageObj.getString(JsonConfig.KEY_IMAGE_URL);
                        imageUrlModels.add(new ImageUrlModel(url));
                    }
                    haveImage = true;

                }
                if(position == 0){
                    CaseHistoryModel caseModel = new CaseHistoryModel(id, name, logo, vtime, vno, vname, flag, approve_count, comments_count, urls);
                    if(temp.has(JsonConfig.KEY_CASE_HISTORY_ID)){
                        String hid = temp.getString(JsonConfig.KEY_CASE_HISTORY_ID);
                        String htime = temp.getString(JsonConfig.KEY_CASE_HISTORY_TIMES);
                        String hcount = temp.getString(JsonConfig.KEY_CASE_HISTORY_FREQUENCY);

                        caseModel.setHistoryId(hid);
                        caseModel.setHistoryTimes(htime);
                        caseModel.setHistoryFrequency(hcount);
                    }
                    if(haveImage){
                        caseModel.setImages(imageUrlModels);
                    }
                    result.add(caseModel);
                }else if(position == 1){
                    CaseFavoriteModel caseModel = new CaseFavoriteModel(id, name, logo, vtime, vno, vname, flag, approve_count, comments_count, urls);
                    if(temp.has(JsonConfig.KEY_CASE_FAVORITE_ID)){
                        String fid = temp.getString(JsonConfig.KEY_CASE_FAVORITE_ID);
                        String ftime = temp.getString(JsonConfig.KEY_CASE_FAVORITE_TIMES);

                        caseModel.setFavoriteId(fid);
                        caseModel.setFavoriteTimes(ftime);
                    }
                    if(haveImage){
                        caseModel.setImages(imageUrlModels);
                    }
                    result.add(caseModel);
                }else if(position == 2){
                    CaseCommentModel caseModel = new CaseCommentModel(id, name, logo, vtime, vno, vname, flag, approve_count, comments_count, urls);
                    if(temp.has(JsonConfig.KEY_CASE_COMMENT_ID)){
                        String cid = temp.getString(JsonConfig.KEY_CASE_COMMENT_ID);
                        String ctime = temp.getString(JsonConfig.KEY_CASE_COMMENT_TIMES);
                        String ccontent = temp.getString(JsonConfig.KEY_CASE_COMMENT_COMMENTS);

                        caseModel.setCommentId(cid);
                        caseModel.setCommentTimes(ctime);
                        caseModel.setCommentContent(ccontent);
                    }
                    if(haveImage){
                        caseModel.setImages(imageUrlModels);
                    }
                    result.add(caseModel);
                }else {
                    CaseModel caseModel = new CaseModel(id, name, logo, vtime, vno, vname, flag, approve_count, comments_count, urls);
                    if(haveImage){
                        caseModel.setImages(imageUrlModels);
                    }
                    result.add(caseModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static List<ButtonModel> getSearchKeyList(String jsonData){
        List<ButtonModel> result = new ArrayList<ButtonModel>();
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            int length = jsonArray.length();
            for(int i = 0; i < length; i++){
                JSONObject temp = (JSONObject) jsonArray.get(i);
                String id = temp.getString(JsonConfig.KEY_SEARCH_KEY_ID);
                String name = temp.getString(JsonConfig.KEY_SEARCH_KEY_NAME);
                String color = temp.getString(JsonConfig.KEY_SEARCH_KEY_COLOR);
                result.add(new ButtonModel(color, name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static LinkedList<CommentsModel> getCommentsList(String jsonData, LinkedList<CommentsModel> result){
        if(result == null ){
            result = new LinkedList<CommentsModel>();
        }
        if(result.size() == 0){//第一条数据占位，被app详情顶替
            result.add(new CommentsModel("0", "0000-00-00 00:00:00", "null", "", ""));
        }
        if(jsonData != null){
            if(!"".equals(jsonData)){
                try {
                    JSONArray arr = new JSONArray(jsonData);
                    int length = arr.length();
                    for(int i = 0; i < length; i++){
                        JSONObject temp = (JSONObject) arr.get(i);

                        String id = temp.getString(JsonConfig.KEY_COMMENTS_ID);
                        String times = temp.getString(JsonConfig.KEY_COMMENTS_TIME);
                        String comments = temp.getString(JsonConfig.KEY_COMMENTS_CONTENT);
                        String avatar = temp.getString(JsonConfig.KEY_COMMENTS_USER_AVATAR);
                        String nick = temp.getString(JsonConfig.KEY_COMMENTS_USER_NICK);
                        result.add(new CommentsModel(id, times, comments, avatar, nick));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
