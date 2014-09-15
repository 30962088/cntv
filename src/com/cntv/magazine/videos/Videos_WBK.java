package com.cntv.magazine.videos;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cntv.magazine.R;
import com.cntv.magazine.collects.CollectsIndexActivity;
import com.cntv.magazine.entity.KindItem;
import com.cntv.magazine.entity.ListItem;
import com.cntv.magazine.news.NewsMainActivity;
import com.cntv.magazine.pictures.Pictures_Main;

public class Videos_WBK extends Activity{
	private TextView firstGame_textview,weibokong_textview,gamevideo_textview;//���ϲ������ѡ���ǩ
	private ImageButton news_imageview,videos_imageview,collects_imageview,pictures_imageview;//�������ĸ�ѡ�ť
	private ArrayList<KindItem> Video_data;//��ȡ������б�
	private ArrayList<ListItem> Videos_list;//�ӿڲ����б�
	private ListView video_bodyView_more_lv;//��Ƶ���ݲ����б�
	private VideoAdapter va1;//�б�������
	private String type = "2",title;//��������ĿID
	private LinearLayout ll;
	int catid;
	private TextView video_title;//����ؼ�
	private HashMap<String,Bitmap> map = new HashMap<String,Bitmap>();
	
	@Override
	protected void onResume() {
		super.onResume();
		/*switch (Video_Tools.Num){
		case 0:
			firstGame_textview.setBackgroundResource(R.drawable.videos_toppicked);
			firstGame_textview.setTextColor(Color.WHITE);
			weibokong_textview.setBackgroundResource(R.drawable.videos_topunpicked);
			weibokong_textview.setTextColor(Color.BLACK);
			gamevideo_textview.setBackgroundResource(R.drawable.videos_topunpicked1);
			gamevideo_textview.setTextColor(Color.BLACK);
			break;
		case 1:
			weibokong_textview.setBackgroundResource(R.drawable.videos_toppicked);
			weibokong_textview.setTextColor(Color.WHITE);
			firstGame_textview.setBackgroundResource(R.drawable.videos_topunpicked);
			firstGame_textview.setTextColor(Color.BLACK);
			gamevideo_textview.setBackgroundResource(R.drawable.videos_topunpicked1);
			gamevideo_textview.setTextColor(Color.BLACK);
			break;
		case 2:
			gamevideo_textview.setBackgroundResource(R.drawable.videos_toppicked1);
			gamevideo_textview.setTextColor(Color.WHITE);
			weibokong_textview.setBackgroundResource(R.drawable.videos_topunpicked);
			weibokong_textview.setTextColor(Color.BLACK);
			firstGame_textview.setBackgroundResource(R.drawable.videos_topunpicked);
			firstGame_textview.setTextColor(Color.BLACK);
			break;
		}*/
		news_imageview.setBackgroundResource(R.drawable.news_news1);
		videos_imageview.setBackgroundResource(R.drawable.news_videos2);
		collects_imageview.setBackgroundResource(R.drawable.news_collects1);
		pictures_imageview.setBackgroundResource(R.drawable.news_pictures1);
		
		
	}
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_more1);
		
		initView();//��ʼ���ؼ�
		initListen();//��������ĵ���¼�
	}
	//��ʼ���������
	void initView(){
		Video_data = Videos_Input.GetVideoData1(2);
		firstGame_textview = (TextView)findViewById(R.id.firstGame);
		weibokong_textview = (TextView)findViewById(R.id.weibokong);
		gamevideo_textview = (TextView)findViewById(R.id.gamevideo);
		ll = (LinearLayout)findViewById(R.id.ll);
		if(Video_data != null){
			for(int i=0;i<Video_data.size();i++){
				if(Video_data.get(i).getCatid() == 209){
					firstGame_textview.setText(Video_data.get(i).getCatname());
				}else if(Video_data.get(i).getCatid() == 210){
					weibokong_textview.setText(Video_data.get(i).getCatname());
					catid = Video_data.get(i).getCatid();
				}else if(Video_data.get(i).getCatid() == 211){
					gamevideo_textview.setText(Video_data.get(i).getCatname());
				}
			}
		}
		Video_data = Videos_Input.GetVideoData1(210);
		if(Video_data != null && Video_data.size() != 0){
		title = Video_data.get(0).getCatname();
		catid = Video_data.get(0).getCatid();
		}
		news_imageview = (ImageButton)findViewById(R.id.pictures_bib1);
		videos_imageview = (ImageButton)findViewById(R.id.pictures_bib2);
		videos_imageview.setBackgroundResource(R.drawable.news_videos2);
		collects_imageview = (ImageButton)findViewById(R.id.pictures_bib3);
		pictures_imageview = (ImageButton)findViewById(R.id.pictures_bib4);
		
		video_title = (TextView)findViewById(R.id.video_title);
		video_title.setText(title);
		
		Videos_list = Videos_Input.init_VideoList(type, catid+"") ;//��ȡ�ӿ�����
		
		
		video_bodyView_more_lv = (ListView)findViewById(R.id.bodyView_more_listview);
		va1 = new VideoAdapter(this);
		video_bodyView_more_lv.setAdapter(va1); // ���ListView������
	}
	//��������ĵ���¼�
	private void initListen() {
		firstGame_textview.setOnClickListener(new ClickEnvent());
		weibokong_textview.setOnClickListener(new ClickEnvent());
		gamevideo_textview.setOnClickListener(new ClickEnvent());
		pictures_imageview.setOnClickListener(new ClickEnvent());
		news_imageview.setOnClickListener(new ClickEnvent());
		videos_imageview.setOnClickListener(new ClickEnvent());
		collects_imageview.setOnClickListener(new ClickEnvent());
		video_bodyView_more_lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 Video_Tools.title = Videos_list.get(arg2).getTitle();
		         Video_Tools.ctitle = Videos_list.get(arg2).getCtitle();
		         Video_Tools.thumb = Videos_list.get(arg2).getThumb();
		         Video_Tools.catid = Videos_list.get(arg2).getCatid();
		         Video_Tools.inputtime = Videos_list.get(arg2).getInputtime();
		         Video_Tools.video_mobileid = Videos_list.get(arg2).getMobileid();
				 Intent videoMore_intent = new Intent();
				 videoMore_intent.setClass(Videos_WBK.this, VideoSecond.class);
				 startActivity(videoMore_intent);
			}		
		});
		ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Video_Tools.more_catid = catid;
				Video_Tools.more_title = title;
				Video_Tools.Num = 1;
				Intent intentm = new Intent();
				intentm.setClass(Videos_WBK.this, Video_More.class);
				Videos_WBK.this.startActivity(intentm);
			}
		});
	}
	//��װ����¼�
	public class ClickEnvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.firstGame://��һ��Ϸ��ť
				firstGame_textview.setBackgroundResource(R.drawable.videos_toppicked);
				firstGame_textview.setTextColor(Color.WHITE);
				weibokong_textview.setBackgroundResource(R.drawable.videos_topunpicked);
				weibokong_textview.setTextColor(Color.BLACK);
				gamevideo_textview.setBackgroundResource(R.drawable.videos_topunpicked1);
				gamevideo_textview.setTextColor(Color.BLACK);
				Video_Tools.Num = 0;
				Intent intent_f = new Intent();
				intent_f.setClass(Videos_WBK.this, Videos_Main.class);
				Videos_WBK.this.startActivity(intent_f);
				break;
			case R.id.weibokong://΢���ذ�ť
				weibokong_textview.setBackgroundResource(R.drawable.videos_toppicked);
				weibokong_textview.setTextColor(Color.WHITE);
				firstGame_textview.setBackgroundResource(R.drawable.videos_topunpicked);
				firstGame_textview.setTextColor(Color.BLACK);
				gamevideo_textview.setBackgroundResource(R.drawable.videos_topunpicked1);
				gamevideo_textview.setTextColor(Color.BLACK);
				//Video_Tools.Num = 1;
				Intent intent_w = new Intent();
				intent_w.setClass(Videos_WBK.this, Videos_WBK.class);
				Videos_WBK.this.startActivity(intent_w);
				break;
			case R.id.gamevideo://��Ϸ��Ƶ��ť
				gamevideo_textview.setBackgroundResource(R.drawable.videos_toppicked1);
				gamevideo_textview.setTextColor(Color.WHITE);
				weibokong_textview.setBackgroundResource(R.drawable.videos_topunpicked);
				weibokong_textview.setTextColor(Color.BLACK);
				firstGame_textview.setBackgroundResource(R.drawable.videos_topunpicked);
				firstGame_textview.setTextColor(Color.BLACK);
				Video_Tools.Num = 2;
				Intent intent_g = new Intent();
				intent_g.setClass(Videos_WBK.this, Videos_Main.class);
				Videos_WBK.this.startActivity(intent_g);
				break;
			case R.id.pictures_bib4://ͼƬ��ť
				news_imageview.setBackgroundResource(R.drawable.news_news1);
				videos_imageview.setBackgroundResource(R.drawable.news_videos1);
				collects_imageview.setBackgroundResource(R.drawable.news_collects1);
				pictures_imageview.setBackgroundResource(R.drawable.news_pictures2);
				Intent intentP = new Intent();
				intentP.setClass(Videos_WBK.this, Pictures_Main.class);
				startActivity(intentP);
				break;
			case R.id.pictures_bib1://���Ű�ť
				news_imageview.setBackgroundResource(R.drawable.news_news2);
				videos_imageview.setBackgroundResource(R.drawable.news_videos1);
				collects_imageview.setBackgroundResource(R.drawable.news_collects1);
				pictures_imageview.setBackgroundResource(R.drawable.news_pictures1);Intent intentN = new Intent();
				intentN.setClass(Videos_WBK.this, NewsMainActivity.class);
				startActivity(intentN);
				break;
			case R.id.pictures_bib2://��Ƶ��ť
				news_imageview.setBackgroundResource(R.drawable.news_news1);
				videos_imageview.setBackgroundResource(R.drawable.news_videos2);
				collects_imageview.setBackgroundResource(R.drawable.news_collects1);
				pictures_imageview.setBackgroundResource(R.drawable.news_pictures1);
				
				firstGame_textview.setBackgroundResource(R.drawable.videos_toppicked);
				firstGame_textview.setTextColor(Color.WHITE);
				weibokong_textview.setBackgroundResource(R.drawable.videos_topunpicked);
				weibokong_textview.setTextColor(Color.BLACK);
				gamevideo_textview.setBackgroundResource(R.drawable.videos_topunpicked1);
				gamevideo_textview.setTextColor(Color.BLACK);
				Video_Tools.Num = 0;
				Intent intent_s = new Intent();
				intent_s.setClass(Videos_WBK.this, Videos_Main.class);
				Videos_WBK.this.startActivity(intent_s);
				break;
			case R.id.pictures_bib3://�ղذ�ť
				news_imageview.setBackgroundResource(R.drawable.news_news1);
				videos_imageview.setBackgroundResource(R.drawable.news_videos1);
				collects_imageview.setBackgroundResource(R.drawable.news_collects2);
				pictures_imageview.setBackgroundResource(R.drawable.news_pictures1);
				Intent intentc = new Intent();
				intentc.setClass(Videos_WBK.this, CollectsIndexActivity.class);
				startActivity(intentc);
				break;
			}
		}
	}
	
	//listview �Զ����������
		public  class VideoAdapter extends BaseAdapter {
			Video_ViewHolder holder;
		     public VideoAdapter(Activity a){
		    }
		     @Override
		     public int getCount() {
		    	int count;
		    	if(Videos_list == null || Videos_list.size()==0)
					count = 0;
				else
					count = Videos_list.size();
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
		    		convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.video_more_item,null);
				    holder = new Video_ViewHolder();
		    		holder.videoImage = (ImageView)convertView.findViewById(R.id.videoimage);
		    		holder.videoText = (TextView)convertView.findViewById(R.id.videotext);
		    		holder.video_ctitle = (TextView)convertView.findViewById(R.id.video_ctitle);
		    		holder.video_inputtime = (TextView)convertView.findViewById(R.id.video_inputtime);
		    		convertView.setTag(holder);
		    	}else{
		    		holder = (Video_ViewHolder)convertView.getTag();
		    	}
		         String path = Videos_list.get(position).getThumb();
		         if(map.containsKey(path)){
		        	 holder.videoImage.setImageBitmap(map.get(path));
		         }else{
		        	 holder.videoImage.setImageResource(R.drawable.news_list_img_loading);
		        	 holder.videoImage.setTag(path);
		        	 new Pic_Main_Small_img2().execute(holder.videoImage);
		         }
		         holder.videoImage.setDrawingCacheEnabled(true);
		         Videos_list.get(position).getMobileid();
		         holder.videoText.setText(Videos_list.get(position).getTitle());
		         holder.video_ctitle.setText(Videos_list.get(position).getCtitle());
		         long inputtime = Long.parseLong(Videos_list.get(position).getInputtime());
		         String data = new java.text.SimpleDateFormat("HH:mm:ss").format(new Date(inputtime));
		         holder.video_inputtime.setText(data);
		         return convertView;  
		    }
		 }
		class Pic_Main_Small_img2 extends AsyncTask<ImageView, Void, Bitmap> {
	        private ImageView gView;

	        protected Bitmap doInBackground(ImageView... views) {
	                Bitmap bmp = null;
	                ImageView view = views[0];
	                if (view.getTag() != null) {// ����iconUrl��ȡͼƬ����Ⱦ��iconUrl��url������view��tag�С�
	                	bmp = Videos_Input.read_VideoSmall_img((view.getTag().toString()));
	                }
	                this.gView = view;
	                return bmp;
	        }

	        protected void onPostExecute(Bitmap bm) {
	            if (bm != null && map != null) {
	            	map.put(this.gView.getTag().toString(), bm);
	                this.gView.setImageBitmap(bm);//�˴�����ͼƬ
	                this.gView = null;
	            }else if(bm == null){
	            	this.gView.setImageResource(R.drawable.news_list_img);
	            }
	        }       
		}
		static class Video_ViewHolder{
			private ImageView videoImage;//����ͼ�ؼ�
			private TextView videoText;//����ؼ�
			private TextView video_ctitle;//����������ͼ
			private TextView video_inputtime;//ʱ��ؼ�
		}
		
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			switch(keyCode) {
			case KeyEvent.KEYCODE_BACK:
				/*Video_Tools.Num = 0;
				Intent intent_s = new Intent();
				intent_s.setClass(Video_More.this, Videos_Main.class);
				Video_More.this.startActivity(intent_s);*/
				finish();
				break;
	        }
	        return false;
		}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		firstGame_textview = null;
		weibokong_textview = null;
		gamevideo_textview = null;
		news_imageview = null;
		videos_imageview = null;
		collects_imageview = null;
		pictures_imageview = null;
		Video_data = null;
		System.gc();
	}
	
}
