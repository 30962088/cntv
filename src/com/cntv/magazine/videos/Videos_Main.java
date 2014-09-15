package com.cntv.magazine.videos;
import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cntv.magazine.R;
import com.cntv.magazine.collects.CollectsIndexActivity;
import com.cntv.magazine.entity.KindItem;
import com.cntv.magazine.news.NewsMainActivity;
import com.cntv.magazine.pictures.Pictures_Main;

public class Videos_Main extends ActivityGroup{
	private TextView firstGame_textview,weibokong_textview,gamevideo_textview;//最上层的三个选项标签
	private ImageButton news_imageview,videos_imageview,collects_imageview,pictures_imageview;//最下面四个选项按钮
	public static LinearLayout bodyView;//中间内容界面
	private ArrayList<KindItem> Video_data;//获取标题的列表
	@Override
	protected void onResume() {
		super.onResume();
		
		switch (Video_Tools.Num){
		case 0:
			firstGame_textview.setBackgroundResource(R.drawable.videos_toppicked);
			firstGame_textview.setTextColor(Color.WHITE);
			weibokong_textview.setBackgroundResource(R.drawable.videos_topunpicked);
			weibokong_textview.setTextColor(Color.BLACK);
			gamevideo_textview.setBackgroundResource(R.drawable.videos_topunpicked1);
			gamevideo_textview.setTextColor(Color.BLACK);
			break;
		case 1:
			/*weibokong_textview.setBackgroundResource(R.drawable.videos_toppicked);
			weibokong_textview.setTextColor(Color.WHITE);
			firstGame_textview.setBackgroundResource(R.drawable.videos_topunpicked);
			firstGame_textview.setTextColor(Color.BLACK);
			gamevideo_textview.setBackgroundResource(R.drawable.videos_topunpicked1);
			gamevideo_textview.setTextColor(Color.BLACK);*/
			break;
		case 2:
			gamevideo_textview.setBackgroundResource(R.drawable.videos_toppicked1);
			gamevideo_textview.setTextColor(Color.WHITE);
			weibokong_textview.setBackgroundResource(R.drawable.videos_topunpicked);
			weibokong_textview.setTextColor(Color.BLACK);
			firstGame_textview.setBackgroundResource(R.drawable.videos_topunpicked);
			firstGame_textview.setTextColor(Color.BLACK);
			break;
		}
		news_imageview.setBackgroundResource(R.drawable.news_news1);
		videos_imageview.setBackgroundResource(R.drawable.news_videos2);
		collects_imageview.setBackgroundResource(R.drawable.news_collects1);
		pictures_imageview.setBackgroundResource(R.drawable.news_pictures1);
		
		bodyView.removeAllViews();
		bodyView.addView(( getLocalActivityManager()).startActivity("1111",
				new Intent(Videos_Main.this,Video.class)).getDecorView());
		bodyView.removeAllViews();
		bodyView.addView(( getLocalActivityManager()).startActivity("1111",
				new Intent(Videos_Main.this, Videos_Expanda_FristGame.class)).getDecorView());
		
		
	}
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_main);
		
		initView();//初始化控件
		initListen();//各个组件的点击事件
	}
	//初始化各个组件
	void initView(){
		Video_data = Videos_Input.GetVideoData1(2);
		firstGame_textview = (TextView)findViewById(R.id.firstGame);
		weibokong_textview = (TextView)findViewById(R.id.weibokong);
		gamevideo_textview = (TextView)findViewById(R.id.gamevideo);
		if(Video_data != null){
			for(int i=0;i<Video_data.size();i++){
				if(Video_data.get(i).getCatid() == 209){
					firstGame_textview.setText(Video_data.get(i).getCatname());
				}else if(Video_data.get(i).getCatid() == 210){
					weibokong_textview.setText(Video_data.get(i).getCatname());
				}else if(Video_data.get(i).getCatid() == 211){
					gamevideo_textview.setText(Video_data.get(i).getCatname());
				}
			}
		}
	    
		bodyView = (LinearLayout)findViewById(R.id.bodyView);
		bodyView.addView(( getLocalActivityManager()).startActivity("1111",
				new Intent(Videos_Main.this, Videos_Expanda_FristGame.class)).getDecorView());
		
		news_imageview = (ImageButton)findViewById(R.id.pictures_bib1);
		videos_imageview = (ImageButton)findViewById(R.id.pictures_bib2);
		videos_imageview.setBackgroundResource(R.drawable.news_videos2);
		collects_imageview = (ImageButton)findViewById(R.id.pictures_bib3);
		pictures_imageview = (ImageButton)findViewById(R.id.pictures_bib4);
		
	}
	//各个组件的点击事件
	private void initListen() {
		firstGame_textview.setOnClickListener(new ClickEnvent());
		weibokong_textview.setOnClickListener(new ClickEnvent());
		gamevideo_textview.setOnClickListener(new ClickEnvent());
		pictures_imageview.setOnClickListener(new ClickEnvent());
		news_imageview.setOnClickListener(new ClickEnvent());
		videos_imageview.setOnClickListener(new ClickEnvent());
		collects_imageview.setOnClickListener(new ClickEnvent());
	}
	//封装点击事件
	public class ClickEnvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.firstGame://第一游戏按钮
				firstGame_textview.setBackgroundResource(R.drawable.videos_toppicked);
				firstGame_textview.setTextColor(Color.WHITE);
				weibokong_textview.setBackgroundResource(R.drawable.videos_topunpicked);
				weibokong_textview.setTextColor(Color.BLACK);
				gamevideo_textview.setBackgroundResource(R.drawable.videos_topunpicked1);
				gamevideo_textview.setTextColor(Color.BLACK);
				Video_Tools.Num = 0;
				bodyView.removeAllViews();
				bodyView.addView(( getLocalActivityManager()).startActivity("1111",
						new Intent(Videos_Main.this,Video.class)).getDecorView());
				bodyView.removeAllViews();
				bodyView.addView(( getLocalActivityManager()).startActivity("1111",
						new Intent(Videos_Main.this, Videos_Expanda_FristGame.class)).getDecorView());
				break;
			case R.id.weibokong://微博控按钮
				/*weibokong_textview.setBackgroundResource(R.drawable.videos_toppicked);
				weibokong_textview.setTextColor(Color.WHITE);
				firstGame_textview.setBackgroundResource(R.drawable.videos_topunpicked);
				firstGame_textview.setTextColor(Color.BLACK);
				gamevideo_textview.setBackgroundResource(R.drawable.videos_topunpicked1);
				gamevideo_textview.setTextColor(Color.BLACK);
				Video_Tools.Num = 1;*/
				Intent intent = new Intent();
				intent.setClass(Videos_Main.this, Videos_WBK.class);
				Videos_Main.this.startActivity(intent);
				break;
			case R.id.gamevideo://游戏视频按钮
				gamevideo_textview.setBackgroundResource(R.drawable.videos_toppicked1);
				gamevideo_textview.setTextColor(Color.WHITE);
				weibokong_textview.setBackgroundResource(R.drawable.videos_topunpicked);
				weibokong_textview.setTextColor(Color.BLACK);
				firstGame_textview.setBackgroundResource(R.drawable.videos_topunpicked);
				firstGame_textview.setTextColor(Color.BLACK);
				Video_Tools.Num = 2;
				bodyView.removeAllViews();
				bodyView.addView(( getLocalActivityManager()).startActivity("1111",
						new Intent(Videos_Main.this,Video.class)).getDecorView());
				bodyView.removeAllViews();
				bodyView.addView(( getLocalActivityManager()).startActivity("1111",
						new Intent(Videos_Main.this, Videos_Expanda_FristGame.class)).getDecorView());
				break;
			case R.id.pictures_bib4://图片按钮
				news_imageview.setBackgroundResource(R.drawable.news_news1);
				videos_imageview.setBackgroundResource(R.drawable.news_videos1);
				collects_imageview.setBackgroundResource(R.drawable.news_collects1);
				pictures_imageview.setBackgroundResource(R.drawable.news_pictures2);
				Intent intentP = new Intent();
				intentP.setClass(Videos_Main.this, Pictures_Main.class);
				startActivity(intentP);
				break;
			case R.id.pictures_bib1://新闻按钮
				news_imageview.setBackgroundResource(R.drawable.news_news2);
				videos_imageview.setBackgroundResource(R.drawable.news_videos1);
				collects_imageview.setBackgroundResource(R.drawable.news_collects1);
				pictures_imageview.setBackgroundResource(R.drawable.news_pictures1);Intent intentN = new Intent();
				intentN.setClass(Videos_Main.this, NewsMainActivity.class);
				startActivity(intentN);
				break;
			case R.id.pictures_bib2://视频按钮
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
				bodyView.removeAllViews();
				bodyView.addView(( getLocalActivityManager()).startActivity("1111",
						new Intent(Videos_Main.this,Video.class)).getDecorView());
				bodyView.removeAllViews();
				bodyView.addView(( getLocalActivityManager()).startActivity("1111",
						new Intent(Videos_Main.this, Videos_Expanda_FristGame.class)).getDecorView());
				break;
			case R.id.pictures_bib3://收藏按钮
				news_imageview.setBackgroundResource(R.drawable.news_news1);
				videos_imageview.setBackgroundResource(R.drawable.news_videos1);
				collects_imageview.setBackgroundResource(R.drawable.news_collects2);
				pictures_imageview.setBackgroundResource(R.drawable.news_pictures1);
				Intent intentc = new Intent();
				intentc.setClass(Videos_Main.this, CollectsIndexActivity.class);
				startActivity(intentc);
				break;
			}
		}
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
