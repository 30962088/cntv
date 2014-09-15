package com.cntv.magazine.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class ServerDataInterface {
	public static String httpGet(String urlStr){
		URL url ;
		HttpURLConnection connect = null;
		InputStream in = null;
		String response = null;
		try {
			url = new URL(urlStr);
			connect = (HttpURLConnection)url.openConnection();
			connect.setDoInput(true);
			connect.setConnectTimeout(3000);
			connect.setRequestMethod("GET");
			connect.setRequestProperty("accept", "*/*");
			connect.connect();
			int responeCode = connect.getResponseCode();
			if(responeCode == 200){
				in = connect.getInputStream();
				response = getResponse(in);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	private static String getResponse(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = -1;
		byte[] buffer = new byte[1024];
		while((len = in.read(buffer))!=-1){
			out.write(buffer, 0, len);
		}
		byte[] data = out.toByteArray(); 
		return new String(data,"UTF-8");
		
	}
	
	 public static String getMD5(String str) {   
	        MessageDigest messageDigest = null;   
	   
	        try {   
	            messageDigest = MessageDigest.getInstance("MD5");   
	   
	            messageDigest.reset();   
	   
	            messageDigest.update(str.getBytes("UTF-8"));   
	        } catch (NoSuchAlgorithmException e) {   
	            System.out.println("NoSuchAlgorithmException caught!");   
	            System.exit(-1);   
	        } catch (UnsupportedEncodingException e) {   
	            e.printStackTrace();   
	        }   
	   
	        byte[] byteArray = messageDigest.digest();   
	   
	        StringBuffer md5StrBuff = new StringBuffer();   
	   
	        for (int i = 0; i < byteArray.length; i++) {               
	            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)   
	                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));   
	            else   
	                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));   
	        }   
	   
	        return md5StrBuff.toString().toUpperCase();   
	    }   



	
	
}
