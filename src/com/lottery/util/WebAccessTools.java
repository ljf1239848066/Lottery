package com.lottery.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
/**
 * @author lxzh
 * 网站访问工具类，用于Android的网络访问
 */
public class WebAccessTools {
	public static String TAG="WebAccessTools";

	public static String getHttpsWebContent(String url){
		Log.println(TAG+"-getHttpsWebContent:url="+url);
        try {
            URL httpsUrl = new URL(url);
            HttpsURLConnection httpsConn = (HttpsURLConnection)httpsUrl.openConnection();
			String charset = "UTF-8";
			Pattern pattern = Pattern.compile("charset=\\S*");
			Matcher matcher = pattern.matcher(httpsConn.getContentType());
			if (matcher.find()) {
				charset = matcher.group().replace("charset=", "");
			}
			Log.println(TAG+"-getHttpsWebContent:1");
            //取得该连接的输入流，以读取响应内容
            InputStream ins = httpsConn.getInputStream();
            BufferedReader breader = new BufferedReader(new InputStreamReader(ins,charset));
            Log.println(TAG+"-getHttpsWebContent:2");
            int rtnCode=breader.read();
            StringBuilder strBui=new StringBuilder();
            Log.println(TAG+"-getHttpsWebContent:3");
            String input;
            while((input=breader.readLine())!=null){
            	strBui.append(input+"\n");
            }
            Log.println(TAG+"-getHttpsWebContent:4");
            return strBui.toString();
        } catch (Exception e) {
            Log.println("The error is:"+e.getMessage());
            e.printStackTrace();
            Log.println(TAG+"-getHttpsWebContent:catch");
        }
        return null;
	}
}