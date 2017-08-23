package com.fc.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**获取http请求连接工厂
 * @author fengchao
 *
 */
public class HttpConnectFacory {

	private static final String IP="127.0.0.1";          //默认ip
	
	/**根据参数获取一个httpurlconnection连接
	 * @param schame 请求方式http/https 目前只支持http方式，默认为http
	 * @param domain  ip地址或者域名 比如www.baidu.com，默认为127.0.0.1
	 * @param port   端口,若无需要传null
	 * @param contextPath   请求上下文路径，若无需要传null
	 * @return  HttpConnect http连接
	 * @throws IOException 
	 */
	public static HttpConnect getHttpConnect(String schame,String domain,String port,String contextPath) throws IOException{
		StringBuilder url=new StringBuilder();
		if(isnull(schame)){
			url.append("http");
		}else {
			url.append(schame);
		}
		url.append("://");
		if(isnull(domain)){
			url.append(IP);
		}else{
			url.append(domain);
		}
		if(!isnull(port)){
			url.append(":").append(port);
		}
		if(!isnull(contextPath)){
			url.append("/").append(contextPath.startsWith("/")?contextPath.replaceFirst("/",""):contextPath);
		}
		HttpConnect connect=new HttpConnect(url.toString());
		return connect;
	}
	private static boolean isnull(String str){
		if(str!=null&&!str.isEmpty()){
			return false;
		}
		return true;
	}
	
	/**
	 * http连接实例
	 * @author fengchao
	 *
	 */
	public static class HttpConnect{
		private URL Url;
		private HttpURLConnection connect=null;     //命名规则，不能命名为connection，否则将在创建连接的时候自动建立连接
		private OutputStreamWriter out;
		Logger logger=LoggerFactory.getLogger(this.getClass());
		
		private HttpConnect(String url) {
			try {
				Url=new URL(url);
				connect=(HttpURLConnection) Url.openConnection();   //创建连接对象
				connect.setConnectTimeout(60000);
				connect.setReadTimeout(30000);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("创建连接异常:",e);
			}
		}
		/**
		 * 创建获取连接
		 * @param requestType 请求类型 post/get
		 */
		public void openConnect(HttpMethordEnum methord){
			try {
				//application/x-javascript text/xml->xml数据 application/x-javascript->json对象 application/x-www-form-urlencoded->表单数据
//			connect.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
				if (HttpMethordEnum.GET.equals(methord)) {
					setGetConnect();
				}else if(HttpMethordEnum.POST.equals(methord)){
					setPostConnect();
				}
				else{
					throw new UnsupportedOperationException("不支持的请求方式");
				}
				connect.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
				connect.connect();
				logger.info("已从 "+Url.toString()+" 建立连接");     //在发送数据之前千万不要进行任何的读操作
			} catch (Exception e) {
				logger.error("连接打开异常",e);
				closeConneect();
			}
		}
		/**
		 * 关闭连接
		 */
		public void closeConneect(){
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(connect!=null){
				connect.disconnect();
			}
		}
	    /**设置连接超时时间
	     * @param millisecond 单位毫秒
	     */
	    public void setConnectTimeout(int millisecond){
	    	connect.setConnectTimeout(millisecond);
	    }
	    /**设置读取数据的超时时间
	     * @param millisecond 单位毫秒
	     */
	    public void setReadTimeout(int millisecond){
	    	connect.setReadTimeout(millisecond);
	    }
		/**
		 * 设置post请求参数
		 */
		private void setPostConnect(){
			try {
				connect.setDoOutput(true);      //标志设置为 true，指示 应用程序要从 URL连接读取数据
				connect.setDoInput(true);
				connect.setRequestMethod(HttpMethordEnum.POST.getValue());
				connect.setUseCaches(false);
			} catch (IOException e) {
				logger.error("连接打开异常",e);
				closeConneect();
			}
		}
		/**
		 * 设置get请求参数
		 */
		private void setGetConnect(){
			try {
				connect.setDoOutput(false);      //标志设置为 true，指示 应用程序要从 URL连接读取数据
				connect.setDoInput(true);
				connect.setRequestMethod(HttpMethordEnum.GET.getValue());
				connect.setUseCaches(false);
			} catch (IOException e) {
				logger.error("连接打开异常",e);
				closeConneect();
			}
		}
		/**设置请求head信息
		 * @param key 名称
		 * @param value 值
		 */
		public void setHeader(String key,String value){
			connect.setRequestProperty(key, value);
		}
		/**发送请求数据
		 * @param msg 字符串类型的请求，可以是转换过后的json数据格式
		 */
		public void sendRequest(String msg)
		{
			try {
				if ("POST".equals(connect.getRequestMethod())) {
					if(out==null){
						out=new OutputStreamWriter(connect.getOutputStream());
					}
					out.write(msg);
					out.flush();
					out.close();
				}
				connect.getInputStream();  //此处才是真正的发送数据
				if(200==connect.getResponseCode()){
					logger.info("请求发送成功"+connect.getResponseMessage());
					System.out.println("请求发送成功"+connect.getResponseMessage());
					System.out.println(connect.getResponseMessage());
				}
				else{
					logger.info("请求已发送,ResponseCode="+connect.getResponseCode());
					System.out.println("请求已发送,ResponseCode="+connect.getResponseCode());
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("发送请求异常",e);
				closeConneect();
			}
		}
		/**
		 * 读取请求结果
		 */
		public void read(){
			try {
				BufferedReader reader=new BufferedReader(new InputStreamReader(connect.getInputStream()));
				String str=reader.readLine();
				System.out.println(str);
				reader.close();
			} catch (IOException e) {
				logger.error("读取结果异常",e);
			}
		}
		public URL getUrl() {
			return Url;
		}
		public void setUrl(URL url) {
			Url = url;
		}
	}
}
