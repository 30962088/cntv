package com.cntv.magazine.news;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.cntv.magazine.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class NewsTools {
	public static int collect_Type;
	public static String collect_Title;
	public static String collect_Ctitle;
	public static String collect_Thumb;
	public static String collect_Mobileid;
	public static String collect_Catid;
	public static String Collect_inputtime;
	public static int channelId;
	public static String mobileId;
	public static String backName;
	public static int Tag;
	//读取图片的方法
	public static Bitmap readPic(String thumb) {
		URL url;
		Bitmap bitmap = null;
		try {
			url = new URL(thumb);
			URLConnection conn = url.openConnection();
	        conn.connect();
	        InputStream in = conn.getInputStream();
	        BufferedInputStream bis= new BufferedInputStream(in);
	        BitmapFactory.Options opts = new BitmapFactory.Options();
	        opts.inSampleSize = 2;
	        bitmap = BitmapFactory.decodeStream(bis,null,opts);
	        bis.close();
	        in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return bitmap;
	}
	
	public static class ImageLoadTask extends AsyncTask<ImageView,Void,Bitmap>{
		private ImageView mImgView;

		protected Bitmap doInBackground(ImageView... views) {
			Bitmap bmp = null;
			ImageView view = views[0];
			if(view.getTag() != null){
				bmp = readPic(view.getTag().toString());
			}
			this.mImgView = view;
			return bmp;
		}
		public void onPostExecute(Bitmap bmp) {
			if (bmp != null){
				this.mImgView.setImageBitmap(bmp); // 更新图片
				this.mImgView = null;
			}else{
				this.mImgView.setImageResource(R.drawable.news_index_img);
			}
		}
		protected void onCancelled(){
			super.onCancelled();
		}
	}
}
