package com.cntv.magazine.pictures;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cntv.magazine.R;
import com.cntv.magazine.collects.CollectsIndexActivity;
import com.cntv.magazine.entity.KindItem;
import com.cntv.magazine.entity.ListItem;
import com.cntv.magazine.news.NewsMainActivity;
import com.cntv.magazine.utils.JsonUtils;
import com.cntv.magazine.utils.ServerDataInterface;
import com.cntv.magazine.videos.Video_Tools;
import com.cntv.magazine.videos.Videos_Main;

public class Pictures_Main extends Activity{
	private GridView pictures_gv;//ͼƬ����
	private GridViewMyAdapter gv_myadapter;//gridView��������
	private String pictures_mobileid;//����ID�����ڻ�ȡ����ҳ��ϸ����
	private ImageButton news_btn;//���Ű�ť
	private ImageButton video_btn;//��Ƶ��ť
	private ImageButton collects_btn;//�ղذ�ť
	private ImageButton pictures_btn;//ͼƬ��ť
	private ImageView pictures_fanhui;//���ذ�ť
	private ArrayList<ListItem>  pictures_list1;//ͼƬ�ӿ��б�
	private ArrayList<KindItem> pictures_data_list;//ͼƬ����ӿ�����
	private String res;//�ӿڵ�ַ
	private String catid;//��ĿID
	private String md;
	private String type = "3"; 
	private HashMap<String,Bitmap> map = new HashMap<String,Bitmap>();//���ڻ���bitmap
	
	@Override
	protected void onResume() {
		super.onResume();
		news_btn.setBackgroundResource(R.drawable.news_news1);
		video_btn.setBackgroundResource(R.drawable.news_videos1);
		collects_btn.setBackgroundResource(R.drawable.news_collects1);
		pictures_btn.setBackgroundResource(R.drawable.news_pictures2);
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pictures_main);
		
		init();//ȡ������
		initView();//��ʼ���ؼ�
		init_Lis();//��������
	}
	//��������
	private void init_Lis() {
		pictures_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				pictures_mobileid = pictures_list1.get(arg2).getMobileid();
				intent.putExtra("pictures_mobileid", pictures_mobileid);
				intent.putExtra("position", arg2+"");
				Video_Tools.Pic=0;
				intent.setClass(Pictures_Main.this,Pictures_details.class);
				startActivity(intent);
			}
		
		});
	}
	//��ʼ���ؼ�
	private void initView() {
		pictures_gv = (GridView)findViewById(R.id.pictures_gv);
		gv_myadapter = new GridViewMyAdapter(this);
		pictures_gv.setAdapter(gv_myadapter); // ���GridView������
		news_btn = (ImageButton)findViewById(R.id.pictures_bib1);
		video_btn = (ImageButton)findViewById(R.id.pictures_bib2);
		collects_btn = (ImageButton)findViewById(R.id.pictures_bib3);
		pictures_btn = (ImageButton)findViewById(R.id.pictures_bib4);
		pictures_fanhui = (ImageView)findViewById(R.id.pictures_fanhui);
		news_btn.setOnClickListener(new btn_listen());
		video_btn.setOnClickListener(new btn_listen());
		collects_btn.setOnClickListener(new btn_listen());
		pictures_btn.setOnClickListener(new btn_listen());
		pictures_fanhui.setOnClickListener(new btn_listen());
	}
	//ȡ������
	private void init() {
		pictures_data_list = Pictures_Input.GetPituresData(3);
		if(pictures_data_list != null && pictures_data_list.size() != 0 ){
			catid = pictures_data_list.get(0).getCatid()+"";
			md = ServerDataInterface.getMD5("1"+type+catid+"adc5674059f996cc34baf91556cdb2cb");
			res = ServerDataInterface.httpGet("http://app.games.cntv.cn/api.php?op=mobileapplist&client=1&type="+type+"&catid="+catid+"&pagesize=20&md5="+md);
			if(res == null){
				Toast.makeText(Pictures_Main.this,"��������ʧ�ܣ������������ã�", Toast.LENGTH_SHORT).show();
			}else{
			JsonUtils json = new JsonUtils();
			pictures_list1 = json.parseListJson(res,catid);
		}
			
		}else{
			Toast.makeText(Pictures_Main.this,"��������ʧ�ܣ������������ã�", Toast.LENGTH_SHORT).show();
		}
	}
	
	//GridView��������
	private class GridViewMyAdapter extends BaseAdapter{
		Pic_ViewHolder holder;
		public GridViewMyAdapter(Activity a) {
		}
		@Override
		public int getCount() {
			int count;
			if(pictures_list1 == null || pictures_list1.size() == 0)
				count = 0;
			else
				count = pictures_list1.size();
			return count;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pictures_main_gv,null);
				holder = new Pic_ViewHolder();
				holder.pictures_gv_img = (ImageView)convertView.findViewById(R.id.pictures_gv_img);
				convertView.setTag(holder);
			}else{
				holder = (Pic_ViewHolder)convertView.getTag();
			}
			String path = pictures_list1.get(position).getThumb();
			if(map.containsKey(path)){
				holder.pictures_gv_img.setImageBitmap(map.get(path));
			}else{
				holder.pictures_gv_img.setImageResource(R.drawable.img_small_loading);								
				holder.pictures_gv_img.setTag(path);
				
				new Pic_Main_Small_img().execute(holder.pictures_gv_img);
			}
			holder.pictures_gv_img.setDrawingCacheEnabled(true);
			return convertView;
		}
	}
	static class Pic_ViewHolder{
	    private ImageView pictures_gv_img;//Gridview�����СͼƬչʾ
	}
	//��ť�����¼�
	private class btn_listen implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.pictures_bib1://���Ű�ť
					news_btn.setBackgroundResource(R.drawable.news_news2);
					video_btn.setBackgroundResource(R.drawable.news_videos1);
					collects_btn.setBackgroundResource(R.drawable.news_collects1);
					pictures_btn.setBackgroundResource(R.drawable.news_pictures1);
					Intent newsIntent = new Intent();
					newsIntent.setClass(Pictures_Main.this,NewsMainActivity.class);
					startActivity(newsIntent);
					break;
				case R.id.pictures_bib2://��Ƶ��ť
					news_btn.setBackgroundResource(R.drawable.news_news1);
					video_btn.setBackgroundResource(R.drawable.news_videos2);
					collects_btn.setBackgroundResource(R.drawable.news_collects1);
					pictures_btn.setBackgroundResource(R.drawable.news_pictures1);
					Video_Tools.Num = 0;
					Intent videoIntent = new Intent();
					videoIntent.setClass(Pictures_Main.this,Videos_Main.class);
					startActivity(videoIntent);
					break;
				case R.id.pictures_bib3://�ղذ�ť
					news_btn.setBackgroundResource(R.drawable.news_news1);
					video_btn.setBackgroundResource(R.drawable.news_videos1);
					collects_btn.setBackgroundResource(R.drawable.news_collects2);
					pictures_btn.setBackgroundResource(R.drawable.news_pictures1);
					Intent intentc = new Intent();
					intentc.setClass(Pictures_Main.this, CollectsIndexActivity.class);
					startActivity(intentc);
					break;
				case R.id.pictures_bib4://ͼƬ��ť
					news_btn.setBackgroundResource(R.drawable.news_news1);
					video_btn.setBackgroundResource(R.drawable.news_videos1);
					collects_btn.setBackgroundResource(R.drawable.news_collects1);
					pictures_btn.setBackgroundResource(R.drawable.news_pictures2);
					break;
				case R.id.pictures_fanhui://���ذ�ť
					finish();
					break;
			}
		}
	}
	//Gridview�첽����ͼƬ
	class Pic_Main_Small_img extends AsyncTask<ImageView, Void, Bitmap> {
        private ImageView gView;
        Bitmap bmp = null;
        protected Bitmap doInBackground(ImageView... views) {         
            ImageView view = views[0];
            if (view.getTag() != null) {// ����iconUrl��ȡͼƬ����Ⱦ��iconUrl��url������view��tag�С�
            	bmp = Pictures_Input.read_Small_img1(view.getTag().toString());
            }
            this.gView = view;
            return bmp;
        }

        protected void onPostExecute(Bitmap bm) {
            if (bm != null && map != null) {
            	map.put(this.gView.getTag().toString(), bm);
                this.gView.setImageBitmap(bm); //�˴�����ͼƬ
                this.gView = null;
            }else if(bm == null){
            	this.gView.setImageResource(R.drawable.news_list_img);
            }
        }       
	}
	
	@Override
	//��������
	protected void onDestroy() {
		super.onDestroy();
		pictures_gv = null;
		gv_myadapter = null;
		pictures_mobileid = null;
		news_btn = null;
		video_btn = null;
		collects_btn = null;
		pictures_btn = null;
		pictures_fanhui = null;
		pictures_data_list = null;
		pictures_list1 = null;
		res = null;
		catid = null;
		md = null;
		type = null;
		map = null;
		System.gc();
	}
}