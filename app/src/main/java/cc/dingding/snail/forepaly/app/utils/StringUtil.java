package cc.dingding.snail.forepaly.app.utils;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.dingding.snail.forepaly.app.helper.bitmap.utils.MD5;

public class StringUtil {

    public static boolean isEmpty(String str) {
        if (str == null || "null".equals(str) || str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPhoneNumber(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.find();
    }

    public static boolean isIdCard(String idCard) {
        if (isEmpty(idCard)) {
            return false;
        }
        String regExp = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[x|X|\\d]$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(idCard);
        return m.find();
    }

    public static String inputStream2String(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static String idCardDeal(String idCard) {
        StringBuffer sb = new StringBuffer(idCard);
        sb.replace(6, 16, "**********");
        return sb.toString();
    }

    /**
     * 常用的格式化日期
     * @param date Date
     * @return String
     */
    public static String getDate(java.util.Date date, String format) {
        String result = "";
        if(date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        return  result;
    }
    /**
     * 常用的格式化日期
     * @param date Date
     * @return String
     */
    public static String getDate(java.util.Date date) {
        String result = "";
        if(date != null) {
            try {
                SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                result = dff.format(date);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        return  result;
    }

    /**
     * 常用的格式化日期
     * @param date Date
     * @return String
     */
    public static String getDate() {
        String result = "";
        Date date = new Date();
        if(date != null) {
            try {
                SimpleDateFormat dff = new SimpleDateFormat("yyyyMMddHHmm");
                dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                result = dff.format(date);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        return  result;
    }
    /**
     * 用户验证
     * @param url
     * @return
     */
    public static String getApiIdentityToken(String url, String uid){
        String[] array = url.split("r=");
        String uri = "";
        for(String item: array){
            uri = item;
        }
        String[] arrays = uri.split("&");
        uri = arrays[0];
        String temp = uid + uri.toLowerCase() + getDate();
        Log.e("test", temp);
        return MD5.md5Encode(temp).substring(24);
    }
}

