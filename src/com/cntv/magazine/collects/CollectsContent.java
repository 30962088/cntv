package com.cntv.magazine.collects;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.cntv.magazine.entity.ListItem;
import com.cntv.magazine.news.NewsMainActivity;
import com.cntv.magazine.news.NewsTextActivity;
import com.cntv.magazine.news.NewsTools;
import com.cntv.magazine.pictures.Pictures_Main;
import com.cntv.magazine.utils.CollectedDbAccess;
import com.cntv.magazine.videos.Videos_Main;

public class CollectsContent extends Activity {
	private int size;//返回list的长度
	private ImageView news_news;//新闻
	private ImageView news_videos;//视频
	private ImageView news_collect;//收藏
	private ImageView news_image;//图片
	private ImageButton content_help;//帮助
	private ImageButton content_fanhui;//返回
	private ListView content_list; //list列表
	private ContentAdapter adapter;//适配器
	private String long_mobileId;//长按ListView获取到Id号
	ArrayList<ListItem> newsList = new ArrayList<ListItem>();//所有的新闻的列表
	HashMap<String,Bitmap> map = new HashMap<String,Bitmap>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.collects_news);
		intView();
		
		//默认选中
		news_collect.setImageResource(R.drawable.news_collects2);	
		
		newsList = CollectedDbAccess.query_All_ListItem(this, 1);
		if(newsList != null && newsList.size() != 0 && newsList.get(0) != null){
			size = newsList.size();
		}else {
			Toast.makeText(this, "您没有收藏的新闻！", Toast.LENGTH_SHORT).show();
		}
		
		if(newsList != null && size != 0){
			adapter = new ContentAdapter(this);	
			content_list.setAdapter(adapter);
		}else{
			Toast.makeText(this, "您没有收藏的新闻！", Toast.LENGTH_SHORT).show();
		}
		
		//ListView点击事件
		content_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				NewsTools.mobileId = newsList.get(arg2).getMobileid();
				startActivity(new Intent(CollectsContent.this,NewsTextActivity.class));
			}
		});
		//ListView的长按事件
		content_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				long_mobileId = newsList.get(arg2).getMobileid();
				showDialog(2);
				return false;
			}
		});
		//封装各个按钮各自的点击事件
		content_fanhui.setOnClickListener(new clickEvent());
		content_help.setOnClickListener(new clickEvent());
		news_news.setOnClickListener(new clickEvent());
		news_videos.setOnClickListener(new clickEvent());
		news_collect.setOnClickListener(new clickEvent());
		news_image.setOnClickListener(new clickEvent());
	}
	
	//ListView的适配器	
	public class ContentAdapter extends BaseAdapter{

		Context context;
		public ContentAdapter(Context context){
			this.context = context;
		}
		public int getCount() {
			int count = 0;
			if(newsList != null && newsList.size() != 0 && newsList.get(0) != null){
				count = newsList.size();
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
			ViewHolder holder;
			if(convertView == null){
				convertView = LayoutInflater.from(getApplicationContext()).
                inflate(R.layout.news_main_item, null);
				holder = new ViewHolder();
				holder.news_list_image = (ImageView)convertView.findViewById(R.id.news_list_img);
				holder.new_list_title = (TextView)convertView.findViewById(R.id.new_list_title);
				holder.new_list_ctitle = (TextView)convertView.findViewById(R.id.new_list_ctitle);
				holder.new_list_time = (TextView)convertView.findViewById(R.id.new_list_time);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
		    holder.new_list_title.setText(newsList.get(position).getTitle());
		    holder.new_list_ctitle.setText(newsList.get(position).getCtitle());
		    
		    //把时间戳转化为时间
			Long time = Long.parseLong(newsList.get(position).getInputtime());
		    String data = new java.text.SimpleDateFormat("MM-dd/HH:mm").format(new java.util.Date(time));
		    holder.new_list_time.setText(data);
		    String thumb = newsList.get(position).getThumb();
		    if(thumb != null){
		    	if(map.containsKey(thumb)){
					holder.news_list_image.setImageBitmap(map.get(thumb));
				}else{
					//调用异步加载图片的方法
					holder.news_list_image.setImageResource(R.drawable.news_list_img_loading);
					holder.news_list_image.setTag(thumb);
					new ImageLoadTask().execute(holder.news_list_image);
				}
		    }
			holder.news_list_image.setDrawingCacheEnabled(true);
		    return convertView;
		    }
		}
	//优化ListView上下滚动的控件类
	static class ViewHolder{
		private TextView new_list_title;//ListView里面的文本 标题
		private TextView new_list_ctitle;//ListView里面的文本副 标题
		private TextView new_list_time;//ListView里面的文本副 时间
		private ImageView news_list_image;//ListView里面的图片
	}
	//对话框
	 @Override
		protected Dialog onCreateDialog(int a) {
			Dialog dialog=null;
			AlertDialog.Builder builder = new Builder(this);
			if(a == 1){
				builder.setMessage("如果您需要删除收藏内容，请长按列表选项");
				builder.setTitle("帮助 "); 
				builder.setNegativeButton("确定", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						dismissDialog(1);
					}
				});
				dialog=builder.create();
				}
			if(a == 2){
				builder.setMessage("确定要删除该条新闻吗？");
				builder.setTitle("请确认！");
				builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						ArrayList<ListItem> list = new ArrayList<ListItem>();
						Log.i("zhuo", "m_id2============="+long_mobileId);
						if(long_mobileId != null){
							CollectedDbAccess.deleteCollect(CollectsContent.this, long_mobileId);
						}
						if(newsList != null && newsList.size() > 0){
							int len = newsList.size();
							Log.i("zhuo", "m_id3============="+long_mobileId);
							for(int i=0;i<len;i++){
								if(long_mobileId != newsList.get(i).getMobileid()){
									list.add(newsList.get(i));
								}
							}
						}else
						{
							newsList.clear();
						}
						newsList = list;
						adapter = new ContentAdapter(CollectsContent.this);
						content_list.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
				});
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						dismissDialog(2);
					}
				});
				dialog=builder.create();
			}
			return dialog;
		}
	
	//异步加载图片
	public class ImageLoadTask extends AsyncTask<ImageView,Void,Bitmap>{
		private ImageView mImgView;
		Bitmap bmp = null;
		protected Bitmap doInBackground(ImageView... views) {
			ImageView view = views[0];
			if(view.getTag() != null){
				bmp = NewsTools.readPic(view.getTag().toString());
			}
			this.mImgView = view;
			return bmp;
		}
		public void onPostExecute(Bitmap bm) {
			if (bm != null && map != null){
				map.put(this.mImgView.getTag().toString(), bm);
				this.mImgView.setImageBitmap(bm); // 更新图片
				this.mImgView = null;
			}
			else if(bm == null){
				this.mImgView.setImageResource(R.drawable.news_list_img);
			}
		}
		protected void onCancelled(){
			super.onCancelled();
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
			case R.id.help_btn:
				showDialog(1);
				break;
				
			case R.id.index_news://新闻
				news_news.setImageResource(R.drawable.news_news2);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures1);
				startActivity(new Intent(CollectsContent.this,NewsMainActivity.class));
				break;
				
			case R.id.index_videos://视频
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos2);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures1);
				startActivity(new Intent(CollectsContent.this,Videos_Main.class));
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
				startActivity(new Intent(CollectsContent.this,Pictures_Main.class));
				break;
				}
			}
		}
	//根据id获得控件实体对象
	private void intView() {
		content_fanhui = (ImageButton)findViewById(R.id.collects_fanhui);
		content_help = (ImageButton)findViewById(R.id.help_btn);
		news_news = (ImageView)findViewById(R.id.index_news);
		news_videos = (ImageView)findViewById(R.id.index_videos);
		news_collect = (ImageView)findViewById(R.id.index_collects);
		news_image = (ImageView)findViewById(R.id.index_pic);
		content_list = (ListView)findViewById(R.id.collect_list);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		news_news = null;
		news_videos = null;
		news_videos = null;
		news_image = null;
		content_fanhui = null;
		content_list = null;
		adapter = null;
		newsList = null;
		map = null;
	}
	 

}
