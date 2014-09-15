package com.cntv.magazine.collects;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cntv.magazine.R;
import com.cntv.magazine.collects.bll.CollectsGrallery;
import com.cntv.magazine.entity.ChannelPerson;
import com.cntv.magazine.news.NewsMainActivity;
import com.cntv.magazine.pictures.Pictures_Main;
import com.cntv.magazine.videos.Video_Tools;
import com.cntv.magazine.videos.Videos_Main;

public class CollectsIndexActivity extends Activity{
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
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
		collectsList = null;
		System.gc();
	}
	int id;
	int flag;
	Channel_GridViewAdapter gv;
	private ImageView news_news;//新闻
	private ImageView news_videos;//视频
	private ImageView news_collect;//收藏
	private ImageView news_image;//图片
	private View news_fanhui;//返回
	private GridView channel_gv;//griview
	private ImageView Channel_img;//列表里的图片
	private TextView news_channel_text;//列表里的文字
	List <ChannelPerson> collectsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.collects_index);
		intView();
		collectsList = CollectsGrallery.getCollectsGrallery();//获取数据
		Log.i("feng", "================="+collectsList.size());
		channel_gv = (GridView)findViewById(R.id.news_channel_gridview);
		gv = new  Channel_GridViewAdapter(this);
		channel_gv.setAdapter(gv);
		//默认选择
		news_collect.setImageResource(R.drawable.news_collects2);
		
		news_fanhui.setOnClickListener(new clickEvent());
		news_news.setOnClickListener(new clickEvent());
		news_videos.setOnClickListener(new clickEvent());
		news_collect.setOnClickListener(new clickEvent());
		news_image.setOnClickListener(new clickEvent());
		
		//GridView的点事件
		channel_gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				flag = collectsList.get(arg2).getId();
				if(flag == 1){
					startActivity(new Intent(CollectsIndexActivity.this,CollectsContent.class));
				}
				else if(flag == 2){
					startActivity(new Intent(CollectsIndexActivity.this,CollectsContent_video.class));
				}
				else if(flag == 3){
					startActivity(new Intent(CollectsIndexActivity.this,CollectsContent_Pic.class));
				}
			}
		});
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		news_news.setImageResource(R.drawable.news_news1);
		news_videos.setImageResource(R.drawable.news_videos1);
		news_collect.setImageResource(R.drawable.news_collects2);
		news_image.setImageResource(R.drawable.news_pictures1);
	}
	//menu各个按钮的监听事件
	class clickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.news_fanhui:
				finish();
				break;
				
			case R.id.index_news://新闻
				news_news.setImageResource(R.drawable.news_news2);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures1);
				startActivity(new Intent(CollectsIndexActivity.this,NewsMainActivity.class));
				break;
			case R.id.index_videos://视频
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos2);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures1);
				Video_Tools.Num = 0;
				startActivity(new Intent(CollectsIndexActivity.this,Videos_Main.class));
				break;
			case R.id.index_collects://收藏
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects2);
				news_image.setImageResource(R.drawable.news_pictures1);
				break;
			case R.id.index_pic://图片
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures2);
				Intent intent3 = new Intent(CollectsIndexActivity.this,Pictures_Main.class);
				startActivity(intent3);
				break;
				}
			}
		}
	public class Channel_GridViewAdapter extends BaseAdapter{

		Context context;
		public Channel_GridViewAdapter(Context context){
			this.context = context;
		}
		public int getCount() {
			int count = 0;
			if(collectsList!=null && collectsList.size()!=0 && collectsList.get(0)!=null){
				count = collectsList.size();
			}
			return count;
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
			              inflate(R.layout.collects_index_item, null);
			Channel_img = (ImageView)convertView.findViewById(R.id.news_channel_img);
			news_channel_text = (TextView)convertView.findViewById(R.id.news_channel_text);
			int imv = collectsList.get(position).getDrawableId();
			Channel_img.setBackgroundResource(imv);
			String text = collectsList.get(position).getName();
			news_channel_text.setText(text);
			id = collectsList.get(position).getId();
			return convertView;
		}
		
	}
	private void intView() {
		news_fanhui = (View)findViewById(R.id.news_fanhui);
		news_news = (ImageView)findViewById(R.id.index_news);
		news_videos = (ImageView)findViewById(R.id.index_videos);
		news_collect = (ImageView)findViewById(R.id.index_collects);
		news_image = (ImageView)findViewById(R.id.index_pic);
	}
	
}