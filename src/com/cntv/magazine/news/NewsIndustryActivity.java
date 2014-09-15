package com.cntv.magazine.news;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cntv.magazine.R;
import com.cntv.magazine.collects.CollectsIndexActivity;
import com.cntv.magazine.entity.ListItem;
import com.cntv.magazine.pictures.Pictures_Main;
import com.cntv.magazine.utils.JsonUtils;
import com.cntv.magazine.utils.ServerDataInterface;
import com.cntv.magazine.videos.Videos_Main;

public class NewsIndustryActivity extends Activity {
	//private int size;//返回List列表的长度
	private String tuijian_res;//推荐新闻数据
	static String mobileId;//内容ID
	private String news_res;//接口数据
	private ImageView news_fanhui;//返回
	private TextView news_require_titleName;//新闻栏目
	private ImageView news_news;//新闻
	private ImageView news_videos;//视频
	private ImageView news_collect;//收藏
	private ImageView news_image;//图片
	private ListView news_list;//装载新闻的list
	private ImageView news_center_img;//图片
	private ListViewAdapter adapter;//适配器
	private TextView news_tuijian_title;//推荐新闻的标题
	ArrayList<ListItem> dataList = new ArrayList<ListItem>();//所有的新闻的列表
	ArrayList<ListItem> picList = new ArrayList<ListItem>();//推荐新闻列表
	HashMap<String,Bitmap> map = new HashMap<String,Bitmap>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_industry);
		intView();
		String i = String.valueOf(NewsTools.channelId);
		getData(i);
		
		news_require_titleName.setText(NewsTools.backName);
		adapter = new ListViewAdapter(this);	
		news_list.setAdapter(adapter);
		
		news_news.setImageResource(R.drawable.news_news2);
		
		
		//封装menu各自的点击事件
		news_fanhui.setOnClickListener(new clickEvent());
		news_news.setOnClickListener(new clickEvent());
		news_videos.setOnClickListener(new clickEvent());
		news_collect.setOnClickListener(new clickEvent());
		news_image.setOnClickListener(new clickEvent());
		
		//ListView的监听事件
		news_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				NewsTools.mobileId = dataList.get(arg2).getMobileid();
				NewsTools.collect_Mobileid = dataList.get(arg2).getMobileid();
				NewsTools.collect_Title = dataList.get(arg2).getTitle();
				NewsTools.collect_Ctitle = dataList.get(arg2).getCtitle();
				NewsTools.collect_Thumb = dataList.get(arg2).getThumb();
				NewsTools.collect_Catid = dataList.get(arg2).getCatid();
				NewsTools.Collect_inputtime = dataList.get(arg2).getInputtime();
				
				startActivity(new Intent(NewsIndustryActivity.this,NewsTextActivity.class));
			}
		});
	}
	
	//取得服务器数据的方法
	private void getData(String i) {
		String md;
		md = ServerDataInterface.getMD5("11"+i+"adc5674059f996cc34baf91556cdb2cb");
		//大图
		String urlStr = "http://app.games.cntv.cn/api.php?op=mobileapplist&client=1&type=1&pagesize=20&catid="+i+"&ishot=1&md5="+md;
		tuijian_res = ServerDataInterface.httpGet(urlStr);
		JsonUtils js = new JsonUtils();
		
		if(tuijian_res != null){
    	    picList = js.parseListJson(tuijian_res, String.valueOf(i));//推荐新闻的List
    	    if(picList != null && picList.size() != 0){
    	    	String title_tuijian = picList.get(0).getTitle();//取得推荐新闻的标题
    	    	String thumb1;
    	    	
        		NewsTools.mobileId = picList.get(0).getMobileid();
        		NewsTools.collect_Mobileid = picList.get(0).getMobileid();//获取的内容的Id
        		NewsTools.collect_Title = picList.get(0).getTitle();//取得推荐新闻的标题
    	    	NewsTools.collect_Ctitle = picList.get(0).getCtitle();//取得推荐新闻的副标题
    	    	NewsTools.collect_Thumb = thumb1 = picList.get(0).getThumb();//取得推荐新闻的图片地址
				NewsTools.collect_Catid = picList.get(0).getCatid();//取得推荐新闻Catid
				NewsTools.Collect_inputtime = picList.get(0).getInputtime();//取得推荐新闻的时间戳
				
        		news_tuijian_title.setText(title_tuijian);
        		//调用异步加载图片的方法
        		news_center_img.setImageResource(R.drawable.news_index_img_loading);
        		news_center_img.setTag(thumb1);
        		new NewsTools.ImageLoadTask().execute(news_center_img);
        		news_center_img.setDrawingCacheEnabled(true);
        		
               if(NewsTools.mobileId != null){
        			news_center_img.setOnClickListener(new View.OnClickListener() {
        				
        				@Override
        				public void onClick(View arg0) {
        					startActivity(new Intent(NewsIndustryActivity.this,NewsTextActivity.class));
        				}
        			});
        		}else{
        			Toast.makeText(this, "抱歉，网络请求出现异常，请检查网络！", Toast.LENGTH_SHORT).show();
        		}
    	    }
        }else{
        	Toast.makeText(this, "抱歉，网络请求出现异常，请检查网络！", Toast.LENGTH_SHORT).show();
        }
		
        //列表
        news_res = ServerDataInterface.httpGet("http://app.games.cntv.cn/api.php?op=mobileapplist&client=1&type=1&catid="+i+"&md5="+md);
        
        if(news_res != null){
        	//列表里面显示的新闻列表
    	    dataList = js.parseListJson(news_res, String.valueOf(i));
    	    Log.i("news_res", ""+news_res);
        }else{
        	Toast.makeText(this, "抱歉，网络请求出现异常，请检查网络！", Toast.LENGTH_SHORT).show();
        }
	}


	//ListView的适配器
	 class ListViewAdapter extends BaseAdapter{

		Activity activity;
		int a;
		public ListViewAdapter(Activity a){
			activity = a;
		}

		@Override
		public int getCount() {	
			int count = 0;
			
			if(dataList != null && dataList.size() != 0 && dataList.get(0) != null){
				count = dataList.size();
			}
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
			ViewHolder holder;
			if(convertView == null){
				convertView = LayoutInflater.from(getApplicationContext()).
	            inflate(R.layout.news_main_item,null);
				holder = new ViewHolder();
				holder.news_list_image = (ImageView)convertView.findViewById(R.id.news_list_img);
				holder.new_list_title = (TextView)convertView.findViewById(R.id.new_list_title);
				holder.new_list_ctitle = (TextView)convertView.findViewById(R.id.new_list_ctitle);
				holder.new_list_time = (TextView)convertView.findViewById(R.id.new_list_time);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			//把获取到的时间戳转化为时间
			Long inputtime = Long.parseLong(dataList.get(position).getInputtime())*1000;
		    String data = new java.text.SimpleDateFormat("MM-dd/HH:mm").format(new java.util.Date(inputtime));
		    String thumb = dataList.get(position).getThumb();
		    
			holder.new_list_title.setText(dataList.get(position).getTitle());
			holder.new_list_ctitle.setText(dataList.get(position).getCtitle());
			holder.new_list_time.setText(data);
			if(map.containsKey(thumb)){
				holder.news_list_image.setImageBitmap(map.get(thumb));
			}else{
				//调用异步加载图片的方法
				holder.news_list_image.setImageResource(R.drawable.news_list_img_loading);
				holder.news_list_image.setTag(thumb);
				new ImageLoadTask().execute(holder.news_list_image);
			}
			holder.news_list_image.setDrawingCacheEnabled(true);
		    return convertView;
		}
		
	} 
	 static class ViewHolder{
		    private TextView new_list_title;//listview里面的文本 标题
			private TextView new_list_ctitle;//listview里面的文本副 标题
			private TextView new_list_time;//listview里面的文本副 时间
			private ImageView news_list_image;//listview里面的图片
		}
	 
	//异步加载图片
		public class ImageLoadTask extends AsyncTask<ImageView,Void,Bitmap>{
			private ImageView mImgView;

			protected Bitmap doInBackground(ImageView... views) {
				Bitmap bmp = null;
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
				}else if(bm == null){
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
			case R.id.news_fanhui:
				finish();
				break;		
				
			case R.id.index_news:
				news_news.setImageResource(R.drawable.news_news2);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures1);
				startActivity(new Intent(NewsIndustryActivity.this,NewsMainActivity.class));
				break;
				
			case R.id.index_videos:
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos2);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures1);
				startActivity(new Intent(NewsIndustryActivity.this,Videos_Main.class));
				break;
				
			case R.id.index_collects:
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects2);
				news_image.setImageResource(R.drawable.news_pictures1);
				startActivity(new Intent(NewsIndustryActivity.this,CollectsIndexActivity.class));
				break;
				
			case R.id.index_pic:
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures2);
				startActivity(new Intent(NewsIndustryActivity.this,Pictures_Main.class));
				break;
			}
		}
		
	}
    //根据id获得控件的实体对象
	private void intView() {
		news_fanhui = (ImageView)findViewById(R.id.news_fanhui);
		news_list = (ListView)findViewById(R.id.news_listview);
		news_news = (ImageView)findViewById(R.id.index_news);
		news_videos = (ImageView)findViewById(R.id.index_videos);
		news_collect = (ImageView)findViewById(R.id.index_collects);
		news_image = (ImageView)findViewById(R.id.index_pic);
		news_center_img = (ImageView)findViewById(R.id.index_img);
		news_tuijian_title = (TextView)findViewById(R.id.index_tuijian_title);
		news_require_titleName = (TextView)findViewById(R.id.news_requiername);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		news_news = null;
		news_videos = null;
		news_collect = null;
		news_image = null;
		news_list = null;
		news_center_img = null;
		news_tuijian_title = null;
		dataList = null;
		picList = null;
		map = null;
		System.gc();
	}
}
	


