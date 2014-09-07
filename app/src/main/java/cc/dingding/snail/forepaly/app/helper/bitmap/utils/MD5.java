package cc.dingding.snail.forepaly.app.helper.bitmap.utils;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MD5 {
    public static String md5Encode(String str)
    {
        StringBuffer buf = new StringBuffer();
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes());
            byte bytes[] = md5.digest();
            for(int i = 0; i < bytes.length; i++)
            {
                String s = Integer.toHexString(bytes[i] & 0xff);
                if(s.length()==1){
                    buf.append("0");
                }
                buf.append(s);
            }
        }
        catch(Exception ex){}
        return buf.toString();
    }

    /**
     * 常用的格式化日期  
     * @param date Date
     * @return String
     */
    public static String getDate() {
        Date date = new Date();
        String result = "";
        String format = "yyyy-MM-dd";
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
}
