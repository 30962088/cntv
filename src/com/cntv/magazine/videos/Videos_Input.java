package com.cntv.magazine.videos;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cntv.magazine.entity.KindItem;
import com.cntv.magazine.entity.ListItem;
import com.cntv.magazine.entity.VideosContent;
import com.cntv.magazine.utils.JsonUtils;
import com.cntv.magazine.utils.ServerDataInterface;

public class Videos_Input {
	public static VideosContent getVideo_content(String video_mobileid,
			int type) {
		String md;
		md = ServerDataInterface.getMD5("1"+video_mobileid+"adc5674059f996cc34baf91556cdb2cb");
		String res = ServerDataInterface.httpGet("http://app.games.cntv.cn/api.php?op=mobileappcontent&client=1&mobileid="+video_mobileid+"&md5="+md);
		if(res != null){
			JsonUtils json = new JsonUtils();
			return (VideosContent) json.parseContent(res,type);
		}
		return null;
	}
	
	public static ArrayList<ListItem> init_VideoList(String type,String catid) {
		String md;
		md = ServerDataInterface.getMD5("1"+type+catid+"adc5674059f996cc34baf91556cdb2cb");
		String res = ServerDataInterface.httpGet("http://app.games.cntv.cn/api.php?op=mobileapplist&client=1&type="+type+"&catid="+catid+"&pagesize=20&md5="+md);
		if(res != null){
		JsonUtils json = new JsonUtils();
		return  json.parseListJson(res,catid);
		}
		return null;
	}
	public static  ArrayList<KindItem> GetVideoData1(int catid){

		String md;
		md = ServerDataInterface.getMD5("1"+catid+"adc5674059f996cc34baf91556cdb2cb");
		String res = ServerDataInterface.httpGet("http://app.games.cntv.cn/api.php?op=mobileappcatlist&client=1&catid="+catid+"&md5="+md);
		JsonUtils json = new JsonUtils(); 
		if(res != null){
			/*pid = �����Pid
			 *pid = 2:ȡ�õ�һ��Ϸ��΢���ء���Ϸ��Ƶ���б�
			 *pid = 209:��һ��Ϸ������Ŀ*/
			return json.parseKindList(res);
		
			
		}else{
			return null;
		}
		
	}
public static  Bitmap read_VideoSmall_img(String path) {
		
		URL url;
		Bitmap bmp = null;
        try {
			url = new URL(path);
			URLConnection conn = url.openConnection();
			conn.connect();
	        InputStream in = conn.getInputStream();
	        BufferedInputStream bis = new BufferedInputStream(in);
	        
	        BitmapFactory.Options opts = new BitmapFactory.Options();
	        //���ŵı����������Ǻ��Ѱ�׼���ı����������ŵģ���ֵ�������ŵı�����SDK�н�����ֵ��2��ָ��ֵ,ֵԽ��ᵼ��ͼƬ������
            opts.inSampleSize = 2;
            bmp = BitmapFactory.decodeStream(bis, null, opts);
            //pictures_gv_img.setImageBitmap(bmp);
            bis.close();
	        in.close();
	       
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmp;
	}

public static  Bitmap read_VideoBig_img(String path) {
	
	URL url;
	Bitmap bmp = null;
    try {
		url = new URL(path);
		URLConnection conn = url.openConnection();
		conn.connect();
        InputStream in = conn.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(in);
        
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //���ŵı����������Ǻ��Ѱ�׼���ı����������ŵģ���ֵ�������ŵı�����SDK�н�����ֵ��2��ָ��ֵ,ֵԽ��ᵼ��ͼƬ������
        opts.inSampleSize = 2;
        bmp = BitmapFactory.decodeStream(bis, null, opts);
        //pictures_gv_img.setImageBitmap(bmp);
        bis.close();
        in.close();
       
	} catch (MalformedURLException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	return bmp;
}
}
