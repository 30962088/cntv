package com.cntv.magazine.news;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.cntv.magazine.R;
import com.cntv.magazine.collects.CollectsIndexActivity;
import com.cntv.magazine.entity.KindItem;
import com.cntv.magazine.pictures.Pictures_Main;
import com.cntv.magazine.utils.JsonUtils;
import com.cntv.magazine.utils.ServerDataInterface;
import com.cntv.magazine.videos.Videos_Main;

public class NewsChannelActivity extends Activity {
	private String md;//��Կ
	private int size;//����List�б�ĳ���
	private ImageButton news_fanhui;//����
	private ImageView news_news;//����
	private ImageView news_image;//ͼƬ
	private ImageView news_videos;//��Ƶ
	private ImageView news_collect;//�ղ�
	private GridView channel_gv;//GridView
	private ImageView Channel_img;//�б����ͼƬ
	private TextView news_channel_text;//�б��������
	private Channel_GridViewAdapter gv;//girdView��������
	static String backName; //���������
	static int channelId;//Ƶ����Id��
	ArrayList<KindItem> padList = new ArrayList<KindItem>();//�Ƽ������б�
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_channel);
		intView();
		md = ServerDataInterface.getMD5("11adc5674059f996cc34baf91556cdb2cb");
		String urlStr = "http://app.games.cntv.cn/api.php?op=mobileappcatlist&client=1&catid=1&md5="+md;
		String class_res = ServerDataInterface.httpGet(urlStr);
		JsonUtils js = new JsonUtils();
		if(class_res != null){
			padList = js.parseKindList(class_res);
			if(padList != null && padList.size() != 0 && padList.get(0) != null){
				size = padList.size();
			}else{
				Toast.makeText(this, "��Ǹ��������������쳣���������磡", Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Toast.makeText(this, "��Ǹ��������������쳣���������磡", Toast.LENGTH_SHORT).show();
		}
		
		channel_gv = (GridView)findViewById(R.id.news_channel_gridview);
		gv = new  Channel_GridViewAdapter(this);
		channel_gv.setAdapter(gv);
		//��װ�����ռ�ĵ���¼�
		news_fanhui.setOnClickListener(new clickEvent());
		news_news.setOnClickListener(new clickEvent());
		news_videos.setOnClickListener(new clickEvent());
		news_collect.setOnClickListener(new clickEvent());
		news_image.setOnClickListener(new clickEvent());
		//Ĭ��ѡ��
		news_news.setImageResource(R.drawable.news_news2);
		//GridView�ĵ���¼�
		channel_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				NewsTools.channelId = padList.get(arg2).getCatid();
				NewsTools.backName = padList.get(arg2).getCatname();
				startActivity(new Intent(NewsChannelActivity.this,NewsIndustryActivity.class));
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		news_news.setImageResource(R.drawable.news_news2);
		news_videos.setImageResource(R.drawable.news_videos1);
		news_collect.setImageResource(R.drawable.news_collects1);
		news_image.setImageResource(R.drawable.news_pictures1);
	}
	
	//GridView��������	
	public class Channel_GridViewAdapter extends BaseAdapter{

		Context context;
		public Channel_GridViewAdapter(Context context){
			this.context = context;
		}
		public int getCount() {
			return size;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(getApplicationContext()).
			              inflate(R.layout.news_channel_item, null);
			Channel_img = (ImageView)convertView.findViewById(R.id.news_channel_img);
			news_channel_text = (TextView)convertView.findViewById(R.id.news_channel_text);
			String text = padList.get(position).getCatname();
			news_channel_text.setText(text);
			
			int imvId = padList.get(position).getCatid();
			if(imvId==202){
				Channel_img.setBackgroundResource(R.drawable.news_channel_industry);
			}
			if(imvId==203){
				Channel_img.setBackgroundResource(R.drawable.news_channel_webgame);
			}
			if(imvId==204){
				Channel_img.setBackgroundResource(R.drawable.news_channel_singlegame);
			}
			if(imvId==205){
				Channel_img.setBackgroundResource(R.drawable.news_channel_homegame);
			}
			if(imvId==206){
				Channel_img.setBackgroundResource(R.drawable.news_channel_esports);
			}
			if(imvId==207){
				Channel_img.setBackgroundResource(R.drawable.news_channel_mobilegame);
			}
			if(imvId==208){
				Channel_img.setBackgroundResource(R.drawable.news_channel_bagua);
			}
			return convertView;
		}
	}
	//menu������ť�ļ����¼�
	class clickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.news_fanhui:
				finish();
				break;
				
			case R.id.index_news://����
				news_news.setImageResource(R.drawable.news_news2);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures1);
				startActivity(new Intent(NewsChannelActivity.this,NewsMainActivity.class));
				break;
				
			case R.id.index_videos://��Ƶ
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos2);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures1);
				startActivity(new Intent(NewsChannelActivity.this,Videos_Main.class));
				break;
				
			case R.id.index_collects://�ղ�
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects2);
				news_image.setImageResource(R.drawable.news_pictures1);
				startActivity(new Intent(NewsChannelActivity.this,CollectsIndexActivity.class));
				break;
				
			case R.id.index_pic://ͼƬ
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures2);
				Intent intent3 = new Intent(NewsChannelActivity.this,Pictures_Main.class);
				startActivity(intent3);
				break;
				}
			}
		}
	private void intView() {
		news_fanhui = (ImageButton)findViewById(R.id.news_fanhui);
		news_news = (ImageView)findViewById(R.id.index_news);
		news_videos = (ImageView)findViewById(R.id.index_videos);
		news_collect = (ImageView)findViewById(R.id.index_collects);
		news_image = (ImageView)findViewById(R.id.index_pic);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		gv = null;
		news_news = null;
		news_videos = null;
		news_collect = null;
		news_image = null;
		news_fanhui = null;
		channel_gv = null;
		Channel_img = null;
		news_channel_text = null;
		backName = null;
		System.gc();
	}
}
