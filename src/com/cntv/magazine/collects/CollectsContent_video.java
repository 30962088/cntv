package com.cntv.magazine.collects;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cntv.magazine.R;
import com.cntv.magazine.collects.CollectsIndexActivity.Channel_GridViewAdapter;
import com.cntv.magazine.entity.ListItem;
import com.cntv.magazine.news.NewsMainActivity;
import com.cntv.magazine.pictures.Pictures_Input;
import com.cntv.magazine.pictures.Pictures_Main;
import com.cntv.magazine.utils.CollectedDbAccess;
import com.cntv.magazine.videos.Video_Tools;
import com.cntv.magazine.videos.Videos_Main;

public class CollectsContent_video extends Activity {
	Channel_GridViewAdapter gv;
	private ImageView news_news;//新闻
	private ImageView news_videos;//视频
	private ImageView news_collect;//收藏
	private ImageView news_image;//图片
	private ImageView Video_help;//帮助
	private ImageButton content_fanhui;//返回
	private ListView content_list; //list列表
	private ContentAdapter adapter;//适配器
	private ArrayList<ListItem> newsList = new ArrayList<ListItem>();//所有的视频的列表
	private HashMap<String,Bitmap> map = new HashMap<String,Bitmap>();
	private String index;
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.collects_news);
		
		intView();
		adapter = new ContentAdapter(this);	
		content_list.setAdapter(adapter);
		
		news_collect.setImageResource(R.drawable.news_collects2);	//默认选中
		newsList = CollectedDbAccess.query_All_ListItem(this, 2);
		if(newsList == null || newsList.size() == 0 && newsList.get(0) != null){
			
			Toast.makeText(this, "您没有收藏的視頻！", Toast.LENGTH_SHORT).show();
		}
		
		initLis();//封装各个按钮各自的点击事件
		ListView_lis();//ListView点击事件
		
		
		
	}
	private void initLis() {
		content_fanhui.setOnClickListener(new clickEvent());
		news_news.setOnClickListener(new clickEvent());
		news_videos.setOnClickListener(new clickEvent());
		news_collect.setOnClickListener(new clickEvent());
		news_image.setOnClickListener(new clickEvent());
		Video_help.setOnClickListener(new clickEvent());
	}
	protected void ListView_lis() {
		content_list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 Video_Tools.title = newsList.get(arg2).getTitle();
		         Video_Tools.ctitle = newsList.get(arg2).getCtitle();
		         Video_Tools.thumb = newsList.get(arg2).getThumb();
		         Video_Tools.catid = newsList.get(arg2).getCatid();
		         Video_Tools.inputtime = newsList.get(arg2).getInputtime();
		         Video_Tools.video_mobileid = newsList.get(arg2).getMobileid();
				 Intent videoMore_intent = new Intent();
				 videoMore_intent.putExtra("position", arg2);
				 videoMore_intent.setClass(CollectsContent_video.this, Collects_video_second.class);
				 startActivity(videoMore_intent);
			}		
		});
		content_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int positions, long id) {
				index  = newsList.get(positions).getMobileid();
				dialog();
				return false;
			}
		});
	}
	
	public class ContentAdapter extends BaseAdapter{
		Col_Video holder;
		Context context;
		public ContentAdapter(Context context){
			this.context = context;
		}
		public int getCount() {
			int size;
			if(newsList != null && newsList.size() != 0 ){
				size = newsList.size();
			}else {
				size = 0;
			}
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
			
			if(convertView == null){
				  convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.video_more_item, null);
				  holder = new Col_Video();
				  holder.news_list_image=(ImageView)convertView.findViewById(R.id.videoimage);
				  holder.new_list_title = (TextView)convertView.findViewById(R.id.videotext);
				  holder.new_list_ctitle = (TextView)convertView.findViewById(R.id.video_ctitle);
				  holder.new_list_time = (TextView)convertView.findViewById(R.id.video_inputtime);
				  convertView.setTag(holder);
			}else{
				holder = (Col_Video)convertView.getTag();
			}
			 
		    Long time = Long.parseLong(newsList.get(position).getInputtime());
		    String data = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date(time));
		    holder.new_list_title.setText(newsList.get(position).getTitle());
		    holder.new_list_ctitle.setText(newsList.get(position).getCtitle());
		    holder.new_list_time.setText(data);
		    String path = newsList.get(position).getThumb();
		    if(map.containsKey(path)){
		    	 holder.news_list_image.setImageBitmap(map.get(path));
		    }else{
		    	 holder.news_list_image.setImageResource(R.drawable.news_list_img_loading);
		    	 holder.news_list_image.setTag(path);
		    	 new Col_Video_img().execute(holder.news_list_image);
		    }
		    holder.news_list_image.setDrawingCacheEnabled(true);
		    return convertView;
		    }
		
		}

	//menu各个按钮的监听事件
	class clickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.collects_fanhui:
				finish();
				break;
			case R.id.index_news://新闻
				news_news.setImageResource(R.drawable.news_news2);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures1);
				startActivity(new Intent(CollectsContent_video.this,NewsMainActivity.class));
				break;
			case R.id.index_videos://视频
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos2);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures1);
				startActivity(new Intent(CollectsContent_video.this,Videos_Main.class));
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
				Intent intent3 = new Intent(CollectsContent_video.this,Pictures_Main.class);
				startActivity(intent3);
				break;
			case R.id.help_btn:
				dialog1();
				break;
				}
			}
		}
	//根据id获得控件实体对象
	private void intView() {
		content_fanhui = (ImageButton)findViewById(R.id.collects_fanhui);
		news_news = (ImageView)findViewById(R.id.index_news);
		news_videos = (ImageView)findViewById(R.id.index_videos);
		news_collect = (ImageView)findViewById(R.id.index_collects);
		news_image = (ImageView)findViewById(R.id.index_pic);
		content_list = (ListView)findViewById(R.id.collect_list);
		Video_help = (ImageView)findViewById(R.id.help_btn);
	}
	static class Col_Video{
		private ImageView news_list_image;//listview里面的图片
		private TextView new_list_title;//listview里面的文本 标题
		private TextView new_list_time;//listview里面的文本副 时间
		private TextView new_list_ctitle;//listview里面的文本副 标题
	}
	protected void dialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("您是否要删除此视频");
		builder.setTitle("请确认！ ");  
		builder.setPositiveButton("是的", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			ArrayList<ListItem> listIt = new ArrayList<ListItem>();
			CollectedDbAccess.deleteCollect(CollectsContent_video.this,index);
			if(newsList != null && newsList.size() != 0){
				for(int i=0; i<newsList.size(); i++){
					if(index != newsList.get(i).getMobileid()){
						listIt.add(newsList.get(i));
					}
				}
			}else{
				newsList.clear();
			}
			newsList = listIt;
			adapter = new ContentAdapter(CollectsContent_video.this);	
			content_list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			}
		});
		
		builder.setNegativeButton("不是", new android.content.DialogInterface.OnClickListener() {   
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}	
		});  

		builder.create().show();

	}
	protected void dialog1() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("如果您需要删除收藏内容，请长按列表选项");
		builder.setTitle("帮助 ");  
		builder.setPositiveButton("是的", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();

	}

	class Col_Video_img extends AsyncTask<ImageView,Void,Bitmap>{
		private ImageView gView;
		Bitmap bmp = null;
		@Override
		protected Bitmap doInBackground(ImageView... views) {
			ImageView view = views[0];
			if(view.getTag() != null){
				bmp = Pictures_Input.read_Small_img(view.getTag().toString());
			}
			this.gView = view;
			return bmp;
		}
		protected void onPostExecute(Bitmap bm){
			if(bm != null && map != null){
				map.put(this.gView.getTag().toString(), bm);
				this.gView.setImageBitmap(bm);
				this.gView = null;
			}else if(bm == null){
				this.gView.setImageResource(R.drawable.news_list_img);
			}
		}
	} 
	@Override
	protected void onDestroy() {
		super.onDestroy();
		news_news = null;
		news_videos = null;
		news_collect = null;
		news_image = null;
		content_fanhui = null;
		content_list = null;
		adapter = null;
		newsList = null;
		map = null;

	}
}
