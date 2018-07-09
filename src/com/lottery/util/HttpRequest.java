package com.lottery.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequest {
	private final static String TAG="HttpRequest";
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url 发送请求的URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
            Log.println(TAG+":URL="+url+",param="+param,Common.DEBUG);
			// 打开和URL之间的连接
//			URLConnection connection = realUrl.openConnection();
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestMethod("GET");
			// 设置通用的请求属性
//			connection.setRequestProperty("Host", "api.api68.com");
//			connection.setRequestProperty("Accept", "*/*");
//			connection.setRequestProperty("Connection", "Keep-Alive");
//			connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
//			connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
//			connection.setRequestProperty("Origin", "http://www.1500kai.com");
//			connection.setRequestProperty("Referer", "http://www.1500kai.com/html/PK10/pk10kai_history.html");
//			connection.setRequestProperty("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:58.0) Gecko/20100101 Firefox/58.0");
			if(connection.getResponseCode()==200) {
				// 定义 BufferedReader输入流来读取URL的响应
				in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常:" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
            Log.println(TAG+":URL="+url+",param="+param,Common.DEBUG);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Host", "bksjw.hust.edu.cn");
			conn.setRequestProperty("Accept-Language",
					"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
			conn.setRequestProperty("Cookie", "usertype=xs; JSESSIONID=0000XhkdI6viIRVX9vhpATqBtvl:167iomm50;");
			conn.setRequestProperty("Content", "key1=190015&key2=1890877fbd792dce4a77003a69cbba5e&type=cj&stuSfid=U201011166&xqselect=20122");
			conn.setRequestProperty(
					"hub_service",
					"cjrgeLM+5k/n8+9XXgIw+dpmaj1+eZt1ICOi5fNIdzUKXMJa4v7PTY+P1ZHkcBdT/tsX95eX7TLmlw==");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("user-agent",
					"Mozilla/5.0 (Windows NT 6.1; rv:19.0) Gecko/20100101 Firefox/19.0");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}