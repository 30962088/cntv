package com.cntv.magazine.videos;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cntv.magazine.R;
import com.cntv.magazine.entity.KindItem;
import com.cntv.magazine.entity.ListItem;

public class Videos_Expanda_FristGame extends ActivityGroup {
	private ListView video_lv_body;//小标题列表
	private LinearLayout bodyView;//内容界面
	private ArrayList<KindItem>  Video_data_FirstGame;// 取得的数据
	private ArrayList<ListItem> Video_data_FirstGame_lanmu;// 取得的子数据
	private ArrayList<ListItem> Video_data_FirstGame_lanmu1;// 取得的子数据
	private String catid,catid1;//栏目ID
	private Video_lv_body_Adapter video_lv_body_Adapter;//数据列表适配器
	HashMap<String,Bitmap>map = new HashMap<String,Bitmap>();
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videos_body);
		
		video_lv_body = (ListView) findViewById(R.id.video_lv_body);
		switch (Video_Tools.Num){
		case 0:
			Video_data_FirstGame = Videos_Input.GetVideoData1(209);
			break;
		case 1:
			Video_data_FirstGame = Videos_Input.GetVideoData1(210);
			break;
		case 2:
			Video_data_FirstGame = Videos_Input.GetVideoData1(211);
			break;
		}
		video_lv_body_Adapter = new Video_lv_body_Adapter(this);
		video_lv_body.setAdapter(video_lv_body_Adapter);
	}
	
	//父类ListView适配器
	public class Video_lv_body_Adapter extends BaseAdapter {
		public Video_lv_body_Adapter(Activity a) {
		}
		@Override
		public int getCount() {
			int count;
			if (Video_data_FirstGame != null
					&& Video_data_FirstGame.size() != 0)
				count = Video_data_FirstGame.size();
			else
				count = 0;
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
		public View getView(int position, View convertView,
				ViewGroup parent) {
			Video_lv_body_ViewHolder holder;
			if(convertView == null){
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.videos_bodyview, null);
				holder = new Video_lv_body_ViewHolder();
				holder.video_title = (TextView) convertView.findViewById(R.id.video_title);
				holder.video_zqsp_gengduo = (LinearLayout) convertView.findViewById(R.id.video_zqsp_gengduo);
				holder.layout1 = (LinearLayout) convertView.findViewById(R.id.layout1);
				holder.layout2 = (LinearLayout) convertView.findViewById(R.id.layout2);
				
				holder.videoimage1 = (ImageView) convertView.findViewById(R.id.videoimage1);
				holder.videotext1 = (TextView) convertView.findViewById(R.id.videotext1);
				holder.video_ctitle1 = (TextView) convertView.findViewById(R.id.video_ctitle1);
				holder.video_inputtime1 = (TextView) convertView.findViewById(R.id.video_inputtime1);
				
				holder.videoimage2 = (ImageView) convertView.findViewById(R.id.videoimage2);
				holder.videotext2 = (TextView) convertView.findViewById(R.id.videotext2);
				holder.video_ctitle2 = (TextView) convertView.findViewById(R.id.video_ctitle2);
				holder.video_inputtime2 = (TextView) convertView.findViewById(R.id.video_inputtime2);
				
				convertView.setTag(holder);
			}else{
				holder = (Video_lv_body_ViewHolder)convertView.getTag();
			}
			
			holder.video_title.setText(Video_data_FirstGame.get(position).getCatname());
			holder.video_zqsp_gengduo.setTag(position);
			holder.video_zqsp_gengduo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int _position = Integer.parseInt(v.getTag().toString());
					bodyView = Videos_Main.bodyView;
					bodyView.removeAllViews();
					Video_Tools.more_catid = Video_data_FirstGame.get(_position).getCatid();
					Video_Tools.more_title = Video_data_FirstGame.get(_position).getCatname();
					Intent intentm = new Intent();
					intentm.setClass(Videos_Expanda_FristGame.this, Video_More.class);
					Videos_Expanda_FristGame.this.startActivity(intentm);
					
				}
			});
			
			catid = Video_data_FirstGame.get(position).getCatid() + "";
			Video_data_FirstGame_lanmu = Videos_Input.init_VideoList("2", catid);// 获取接口数据
			if(Video_data_FirstGame_lanmu != null && Video_data_FirstGame_lanmu.size()!=0){
				 if(Video_data_FirstGame_lanmu.size() == 1){
				    FF(holder,0);
				    holder.layout2.setVisibility(View.GONE);
				}else if(Video_data_FirstGame_lanmu.size() >= 2){
					FF(holder,0);
					FF1(holder,1);

				}
			}else{
				holder.layout2.setVisibility(View.GONE);
				holder.layout1.setVisibility(View.GONE);
			}
			holder.layout2.setTag(position);
			holder.layout2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int position_ = Integer.parseInt(v.getTag().toString());
						catid1 = Video_data_FirstGame.get(position_).getCatid() + "";
						Video_data_FirstGame_lanmu1 = Videos_Input.init_VideoList("2", catid1);// 获取接口数据
						
						if(Video_data_FirstGame_lanmu1 != null && Video_data_FirstGame_lanmu1.size()!=0){
							Video_Tools.title = Video_data_FirstGame_lanmu1.get(1).getTitle();
				         Video_Tools.ctitle = Video_data_FirstGame_lanmu1.get(1).getCtitle();
				         Video_Tools.thumb = Video_data_FirstGame_lanmu1.get(1).getThumb();
				         Video_Tools.catid = Video_data_FirstGame_lanmu1.get(1).getCatid();
				         Video_Tools.inputtime = Video_data_FirstGame_lanmu1.get(1).getInputtime();
				         Video_Tools.video_mobileid = Video_data_FirstGame_lanmu1.get(1).getMobileid();
						 Intent videoMore_intent2 = new Intent();
						 videoMore_intent2.setClass(Videos_Expanda_FristGame.this, VideoSecond.class);
						 startActivity(videoMore_intent2);
						}
					}
				});
			
			holder.layout1.setTag(position);
			holder.layout1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int ption = Integer.parseInt(v.getTag().toString());
						catid1 = Video_data_FirstGame.get(ption).getCatid() + "";
						Video_data_FirstGame_lanmu1 = Videos_Input.init_VideoList("2", catid1);// 获取接口数据
						
						if(Video_data_FirstGame_lanmu1 != null && Video_data_FirstGame_lanmu1.size()!=0){
						Video_Tools.title = Video_data_FirstGame_lanmu1.get(0).getTitle();
				         Video_Tools.ctitle = Video_data_FirstGame_lanmu1.get(0).getCtitle();
				         Video_Tools.thumb = Video_data_FirstGame_lanmu1.get(0).getThumb();
				         Video_Tools.catid = Video_data_FirstGame_lanmu1.get(0).getCatid();
				         Video_Tools.inputtime = Video_data_FirstGame_lanmu1.get(0).getInputtime();
				         Video_Tools.video_mobileid = Video_data_FirstGame_lanmu1.get(0).getMobileid();
						 Intent videoMore_intent1 = new Intent();
						 videoMore_intent1.setClass(Videos_Expanda_FristGame.this, VideoSecond.class);
						 startActivity(videoMore_intent1);
						}
					}
				});
			return convertView;
		}
		private void FF(Video_lv_body_ViewHolder holder,int i) {
			holder.videotext1.setText(Video_data_FirstGame_lanmu.get(i).getTitle());
			holder.video_ctitle1.setText(Video_data_FirstGame_lanmu.get(i).getCtitle());
			long inputtime = Long.parseLong(Video_data_FirstGame_lanmu.get(i).getInputtime())*1000;
			String data = new java.text.SimpleDateFormat("HH:mm:ss").format(new Date(inputtime));
			holder.video_inputtime1.setText(data);
			String path = Video_data_FirstGame_lanmu.get(i).getThumb();
			if(map.containsKey(path)){
				holder.videoimage1.setImageBitmap(map.get(path));
			}else{
				holder.videoimage1.setImageResource(R.drawable.news_list_img_loading);
				holder.videoimage1.setTag(path);
				new Pic_Main_Small_img().execute(holder.videoimage1);
			}
			holder.videoimage1.setDrawingCacheEnabled(true);
		}
		private void FF1(Video_lv_body_ViewHolder holder,int j) {
			holder.videotext2.setText(Video_data_FirstGame_lanmu.get(j).getTitle());
			holder.video_ctitle2.setText(Video_data_FirstGame_lanmu.get(j).getCtitle());
			long inputtime = Long.parseLong(Video_data_FirstGame_lanmu.get(j).getInputtime())*1000;
			String data = new java.text.SimpleDateFormat("HH:mm:ss").format(new Date(inputtime));
			holder.video_inputtime2.setText(data);
			String path = Video_data_FirstGame_lanmu.get(j).getThumb();
			if(map.containsKey(path)){
				holder.videoimage2.setImageBitmap(map.get(path));
			}else{
				holder.videoimage2.setImageResource(R.drawable.news_list_img_loading);
				holder.videoimage2.setTag(path);
				new Pic_Main_Small_img1().execute(holder.videoimage2);
			}
			holder.videoimage2.setDrawingCacheEnabled(true);
		}
	}
	static class Video_lv_body_ViewHolder{
	    private TextView video_title;//小标题控件
		private LinearLayout video_zqsp_gengduo;//更多控件
		private LinearLayout layout1;//视频第一条目
		private LinearLayout layout2;//视频第二条目
		private TextView videotext1;//内容标题控件1
		private TextView video_ctitle1;//内容副标题控件1
		private TextView video_inputtime1;//时间戳控件1
		private ImageView videoimage1;//缩略图2
		private TextView videotext2;//内容标题控件2
		private TextView video_ctitle2;//内容副标题控件2
		private TextView video_inputtime2;//时间戳控件2
		private ImageView videoimage2;//缩略图2
		
	}
	
	//Gridview异步加载图片
	class Pic_Main_Small_img extends AsyncTask<ImageView, Void, Bitmap> {
        private ImageView gView;

        protected Bitmap doInBackground(ImageView... views) {
                Bitmap bmp = null;
                ImageView view = views[0];
                if (view.getTag() != null) {// 根据iconUrl获取图片并渲染，iconUrl的url放在了view的tag中。
                	bmp = Videos_Input.read_VideoSmall_img((view.getTag().toString()));
                }
                this.gView = view;
                return bmp;
        }

        protected void onPostExecute(Bitmap bm) {
            if (bm != null && map != null) {
            	map.put(this.gView.getTag().toString(), bm);
                this.gView.setImageBitmap(bm);//此处更新图片
                this.gView = null;
            }else if(bm == null){
            	this.gView.setImageResource(R.drawable.news_list_img);
            }
        }       
	}
	class Pic_Main_Small_img1 extends AsyncTask<ImageView, Void, Bitmap> {
        private ImageView gView;

        protected Bitmap doInBackground(ImageView... views) {
            Bitmap bmp = null;
            ImageView view = views[0];
            if (view.getTag() != null) {// 根据iconUrl获取图片并渲染，iconUrl的url放在了view的tag中。
            	bmp = Videos_Input.read_VideoSmall_img((view.getTag().toString()));
            }
            this.gView = view;
            return bmp;
        }

        protected void onPostExecute(Bitmap bm) {
            if (bm != null && map != null) {
            	map.put(this.gView.getTag().toString(), bm);
                this.gView.setImageBitmap(bm);//此处更新图片
                this.gView = null;
            }else if(bm == null){
            	this.gView.setImageResource(R.drawable.news_list_img);
            }
        }       
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		map = null;
		video_lv_body = null;
		bodyView = null;
		Video_data_FirstGame = null;
		Video_data_FirstGame_lanmu = null;
		catid = null;
		video_lv_body_Adapter = null;
		System.gc();
	}
}
