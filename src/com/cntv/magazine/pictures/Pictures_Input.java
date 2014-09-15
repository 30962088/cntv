package com.cntv.magazine.pictures;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.cntv.magazine.R;
import com.cntv.magazine.entity.KindItem;
import com.cntv.magazine.entity.PicturesContent;
import com.cntv.magazine.utils.JsonUtils;
import com.cntv.magazine.utils.ServerDataInterface;

public class Pictures_Input {
	static Bitmap bitmap;
	static ArrayList<KindItem> PicturesData_list1;

	
	public static Bitmap read_Small_img(String path) {

		URL url;
		Bitmap bmp = null;
		try {
			url = new URL(path);
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(in);

			BitmapFactory.Options opts = new BitmapFactory.Options();
			// ���ŵı����������Ǻ��Ѱ�׼���ı����������ŵģ���ֵ�������ŵı�����SDK�н�����ֵ��2��ָ��ֵ,ֵԽ��ᵼ��ͼƬ������
			opts.inSampleSize = 2;
			bmp = BitmapFactory.decodeStream(bis, null, opts);
			// pictures_gv_img.setImageBitmap(bmp);
			bis.close();
			in.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmp;
	}

	public static PicturesContent getPic_content(String pictures_mobileid,
			int type) {
		String md;
		md = ServerDataInterface.getMD5("1" + pictures_mobileid
				+ "adc5674059f996cc34baf91556cdb2cb");
		String res = ServerDataInterface
				.httpGet("http://app.games.cntv.cn/api.php?op=mobileappcontent&client=1&mobileid="
						+ pictures_mobileid + "&md5=" + md);
		if (res != null) {
			JsonUtils json = new JsonUtils();
			return (PicturesContent) json.parseContent(res, 3);
		}
		return null;
	}

	public static Bitmap read_Big_img(String path) {

		URL url;
		Bitmap bmp = null;
		try {
			url = new URL(path);
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(in);

			BitmapFactory.Options opts = new BitmapFactory.Options();// ���ŵı����������Ǻ��Ѱ�׼���ı����������ŵģ���ֵ�������ŵı�����SDK�н�����ֵ��2��ָ��ֵ,ֵԽ��ᵼ��ͼƬ������
			opts.inSampleSize = 6;
			bmp = BitmapFactory.decodeStream(bis, null, opts);
			bis.close();
			in.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmp;
	}

	public static ArrayList<KindItem> GetPituresData(int catid) {
		String md;
		md = ServerDataInterface.getMD5("1" + catid
				+ "adc5674059f996cc34baf91556cdb2cb");
		String res = ServerDataInterface
				.httpGet("http://app.games.cntv.cn/api.php?op=mobileappcatlist&client=1&catid="
						+ catid + "&md5=" + md);
		JsonUtils json = new JsonUtils();
		if (res != null) {
			return json.parseKindList(res);
		} else {
			return null;
		}
	}

	public static Bitmap read_Small_img1(String path) {

		URL url;
		Bitmap bmp = null;
		try {
			url = new URL(path);
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(in);

			BitmapFactory.Options opts = new BitmapFactory.Options();
			// ���ŵı����������Ǻ��Ѱ�׼���ı����������ŵģ���ֵ�������ŵı�����SDK�н�����ֵ��2��ָ��ֵ,ֵԽ��ᵼ��ͼƬ������
			opts.inSampleSize = 14;
			bmp = BitmapFactory.decodeStream(bis, null, opts);
			// pictures_gv_img.setImageBitmap(bmp);
			bis.close();
			in.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmp;
	}

	public static Bitmap scaleImg(Bitmap bm , Context context) {
		// ͼƬԴ
		
		Bitmap bm1 = BitmapFactory.decodeStream(context.getResources()
				.openRawResource(R.drawable.videos_big));
		// ���ͼƬ�Ŀ��
		if (bm != null) {
			int width = bm.getWidth();
			int height = bm.getHeight();
			int newWidth1=0;
			int newHeight1=0;
			// ������Ҫ�Ĵ�С
			if(bm1 != null){
			newWidth1 = bm1.getWidth();
			newHeight1 = bm1.getDensity();
			}else{
				newWidth1 = 513;
				newHeight1 = 360;
			}
			// �������ű���
			float scaleWidth = ((float) newWidth1) / width;
			float scaleHeight = ((float) newHeight1) / height;
			
			// ȡ����Ҫ���ŵ�matrix����
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			
			// �õ��µ�ͼƬ
			Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
					true);
			return newbm;
		} else {
			return null;
		}
	}
}
