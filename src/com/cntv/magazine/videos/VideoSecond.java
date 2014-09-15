package com.cntv.magazine.videos;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cntv.magazine.R;
import com.cntv.magazine.collects.CollectsIndexActivity;
import com.cntv.magazine.entity.ListItem;
import com.cntv.magazine.entity.VideosContent;
import com.cntv.magazine.news.NewsMainActivity;
import com.cntv.magazine.pictures.Pictures_Input;
import com.cntv.magazine.pictures.Pictures_Main;
import com.cntv.magazine.utils.CollectedDbAccess;

public class VideoSecond extends Activity {
	private ImageView video_bofang;//视频播放大按钮
	private TextView videosSecond_title;//缩略图地址
	private WebView videosSecond_content;//正文内容或说明内容，HTML代码
	private ImageView videosSecond_bigimg;//视频大图
	private ImageView return_imageview;//返回按钮
	private String plaurl;//视频播放地址
	private String video_mobileid;//内容ID 
	private String catid;//栏目Id
	private String title;
	private String ctitle;
	private String thumb;
	private String inputtime;
	private VideosContent videosContent;//视频内容对象
	private String path;//图片路径
	private String videosSecond_content_text;//内容路径
	private ArrayList<ListItem> VideoSecond_list;//接口列表数据
	ArrayList<ListItem>  collected_list;//收藏列表
	private Button video_shoucang;//收藏按钮
	private Gallery Video_Gallery;
	private Gallery_Adapter gallery_Adapter;
	private HashMap<String,Bitmap> map = new HashMap<String,Bitmap>();
	private HashMap<String,Bitmap> map1 = new HashMap<String,Bitmap>();
	private int existTag;//判断HTML文件是否存在的标志
	List<ListItem> listPanduan;
	public static Handler mHandler;
	LinearLayout Linear_video;
	ImageButton news;
	ImageButton videos;
	ImageButton collects;
	ImageButton pictures;
	Bitmap newBm ;
	private int flag;
	
	protected void onResume() {
		video_bofang.setImageResource(R.drawable.da_bofang);
		super.onResume();
		
		
		video_mobileid = Video_Tools.video_mobileid;
		listPanduan = CollectedDbAccess.query_All_ListItem(this, 2);
		if(listPanduan != null && video_mobileid != null){
			int count = listPanduan.size();		
			for(int i=0;i<count;i++){
				if(video_mobileid.equals(listPanduan.get(i).getMobileid())){
					video_shoucang.setText("已收藏");
					break;
				}
			}
		}else{
			video_shoucang.setText("收藏");
		}
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.videos_second);
		
		Linear_video = (LinearLayout)findViewById(R.id.video_layout);
		getData();
		videosContent = Videos_Input.getVideo_content(video_mobileid, 2);
		VideoSecond_list = Videos_Input.init_VideoList("2", catid);
		initView();//初始化控件
		initListen();//按钮监听事件
		
		listPanduan = CollectedDbAccess.query_All_ListItem(this, 2);
		if(listPanduan!=null){
			int count = listPanduan.size();		
			for(int i=0;i<count;i++){
				if(video_mobileid.equals(listPanduan.get(i).getMobileid())){
					video_shoucang.setText("已收藏");
					break;
				}
			}
		}

	}
	private void getData() {
		catid = Video_Tools.catid;
		video_mobileid = Video_Tools.video_mobileid;
		title = Video_Tools.title;
		ctitle = Video_Tools.ctitle;
		thumb = Video_Tools.thumb;
		inputtime = Video_Tools.inputtime;
	}
	private void initListen() {
		return_imageview.setOnClickListener(new ClickEnvent());
		video_bofang.setOnClickListener(new ClickEnvent());
		video_shoucang.setOnClickListener(new ClickEnvent());
		news.setOnClickListener(new ClickEnvent());
		videos.setOnClickListener(new ClickEnvent());
		collects.setOnClickListener(new ClickEnvent());
		pictures.setOnClickListener(new ClickEnvent());
	}
	//控件初始化
	void initView(){
		return_imageview = (ImageView)findViewById(R.id.image_return);
		video_bofang = (ImageView)findViewById(R.id.videos_da_bofang);
		videosSecond_title = (TextView)findViewById(R.id.videoSecond_title);
		videosSecond_bigimg = (ImageView)findViewById(R.id.videos_da_bofang_bg);
		videosSecond_content = (WebView)findViewById(R.id.videoSecond_content);
		
	    Linear_video = (LinearLayout)findViewById(R.id.video_layout);
	    news = (ImageButton)findViewById(R.id.pictures_bib1);
	    videos = (ImageButton)findViewById(R.id.pictures_bib2);
	    collects = (ImageButton)findViewById(R.id.pictures_bib3);
	    pictures = (ImageButton)findViewById(R.id.pictures_bib4);
	    
		if(videosContent != null){
		videosSecond_title.setText(videosContent.getTitle());
		path = videosContent.getBigimg();
		if(path.equals("null")||path==null){
			path = thumb;
		}
		if(map.containsKey(path)){
			videosSecond_bigimg.setImageBitmap(map.get(path));
			
		}else{
			videosSecond_bigimg.setImageResource(R.drawable.videos_big);
			videosSecond_bigimg.setTag(path);
			new Video_Big_ImageTask().execute(videosSecond_bigimg);
		}
		Read_WebContent();
		}
		video_shoucang = (Button)findViewById(R.id.video_shoucang1);
		video_shoucang.setText("收藏");
	    Video_Gallery = (Gallery)findViewById(R.id.video_gallery);
	    gallery_Adapter = new Gallery_Adapter(this);
	    Video_Gallery.setAdapter(gallery_Adapter);
	    Video_Gallery.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Video_Tools.title = VideoSecond_list.get(position).getTitle();
		        Video_Tools.ctitle = VideoSecond_list.get(position).getCtitle();
		        Video_Tools.thumb = VideoSecond_list.get(position).getThumb();
		        Video_Tools.catid = VideoSecond_list.get(position).getCatid();
		        Video_Tools.inputtime = VideoSecond_list.get(position).getInputtime();
		        Video_Tools.video_mobileid = VideoSecond_list.get(position).getMobileid();
				getData();
				String mobileid = VideoSecond_list.get(position).getMobileid();
				videosContent = Videos_Input.getVideo_content(mobileid, 2);
				
				video_shoucang.setText("收藏");
				listPanduan = CollectedDbAccess.query_All_ListItem(VideoSecond.this, 2);
				if(listPanduan!=null){
					int count = listPanduan.size();		
					for(int i=0;i<count;i++){
						if(video_mobileid.equals(listPanduan.get(i).getMobileid())){
							video_shoucang.setText("已收藏");
							break;
						}
					}
				}
				
				if(videosContent != null){
					videosSecond_title.setText(videosContent.getTitle());
					path = videosContent.getBigimg();
					if(path.equals("null")||path==null){
						path = thumb;
					}
					if(map.containsKey(path)){
						videosSecond_bigimg.setImageBitmap(map.get(path));
					}else{
						videosSecond_bigimg.setImageResource(R.drawable.videos_big_loading);
						videosSecond_bigimg.setTag(path);
						new Video_Big_ImageTask().execute(videosSecond_bigimg);
					}
					videosSecond_bigimg.setDrawingCacheEnabled(true);
					Read_WebContent();
				}
			}
	    });
	    
	    mHandler = new Handler(){

			
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case 1:
				   if(videosSecond_content!=null){
					   videosSecond_content.clearCache(true);
					   videosSecond_content.clearHistory();
					   videosSecond_content.clearFormData();
					   videosSecond_content = null;
				   }	
				   break;
				}
				super.handleMessage(msg);
			}
			
		};
	    
	}
	private void Read_WebContent() {
		videosSecond_bigimg.setDrawingCacheEnabled(true);
		videosSecond_content_text = videosContent.getContent();
		WebSettings myWebSettings = videosSecond_content.getSettings();
		myWebSettings.setJavaScriptEnabled(true);
		videosSecond_content.getSettings().setPluginsEnabled(true);
		myWebSettings.setPluginState(PluginState.ON);
		myWebSettings.setSupportZoom(true);
		myWebSettings.setBuiltInZoomControls(true);
		myWebSettings.setDefaultZoom(ZoomDensity.CLOSE);
		myWebSettings.setDefaultTextEncodingName("utf-8");
		myWebSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		String html = "";
			html+="<html>";
			html+="<body>";
			if(videosSecond_content_text != null){
				html += videosSecond_content_text;
			}else{
				Toast.makeText(this, "此网页内容不存在！", Toast.LENGTH_SHORT).show();
		    }
	     	html+="</body>";
	    	html+="</html>";
	    	String temp = video_mobileid + ".html";
	    	int length = this.fileList().length;
	    	for(int i=0;i<length;i++){
		    	if(this.fileList().length != 0 && fileList()[i].equals(video_mobileid+".html")){
		    		videosSecond_content.loadUrl("file:///data/data/com.cntv.magazine/files/"+temp+"");
		    		existTag = 1;
		    	}
		    }
	    	if(existTag==0){
		    	byte[] b = html.getBytes();
			    FileOutputStream fo;
				try {
					fo = openFileOutput(video_mobileid+".html", 0);
					fo.write(b);
					fo.flush();
					fo.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				videosSecond_content.loadUrl("file:///data/data/com.cntv.magazine/files/"+temp+"");
		    }
	}
	
	//异步加载图片
	class Video_Big_ImageTask extends AsyncTask<ImageView, Void, Bitmap> {
        private ImageView gView;
        int gW;
		int gH;
        
        protected Bitmap doInBackground(ImageView... views) {
                Bitmap bmp = null;
                gW = videosSecond_bigimg.getWidth();
                gH = videosSecond_bigimg.getHeight();
                ImageView view = views[0];
                if (view.getTag() != null) {
                	bmp = Pictures_Input.read_Big_img(view.getTag().toString());
                	bmp = Pictures_Input.scaleImg(bmp,VideoSecond.this);
                }
                this.gView = view;
                return bmp;
        }

        protected void onPostExecute(Bitmap bm) {
            if (bm != null && map != null) { //此处更新图片
            	gW = videosSecond_bigimg.getWidth();
                gH = videosSecond_bigimg.getHeight();
            	bm = Pictures_Input.scaleImg(bm,VideoSecond.this);
            	map.put(this.gView.getTag().toString(), bm);
                this.gView.setImageBitmap(bm);
                this.gView = null;
            }else if(bm == null){
            	this.gView.setImageResource(R.drawable.videos_big);
            }
        }       
	}
	class Video_Small_ImageTask1 extends AsyncTask<ImageView, Void, Bitmap> {
        private ImageView gView;
        protected Bitmap doInBackground(ImageView... views) {
                Bitmap bmp = null;
                ImageView view = views[0];
                
                if (view.getTag() != null) { // 根据iconUrl获取图片并渲染，iconUrl的url放在了view的tag中。
                	bmp = Pictures_Input.read_Small_img(view.getTag().toString());
                }
                this.gView = view;
                return bmp;
        }

        protected void onPostExecute(Bitmap bm) {
            if (bm != null && map1 != null) { //此处更新图片
            	map1.put(this.gView.getTag().toString(), bm);
                this.gView.setImageBitmap(bm);
                this.gView = null;
            }else if(bm == null ){
            	this.gView.setImageResource(R.drawable.videos_first);
            }
        }       
	}
	//封装点击事件
	public class ClickEnvent implements OnClickListener{
		int index = 0,a = index,b = a+1,c = b+1;
		
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.image_return://返回按钮
				finish();
				break;
			case R.id.video_shoucang1://收藏按钮
				ListItem listitem = new ListItem();
				listitem.setType(2);
				listitem.setTitle(title);
				listitem.setCtitle(ctitle);
				listitem.setInputtime(inputtime);
				listitem.setMobileid(video_mobileid);
				listitem.setThumb(thumb);
				listitem.setCatid(catid);
				collected_list = CollectedDbAccess.query_All_ListItem(VideoSecond.this, 2);
				
				if(collected_list != null && collected_list.size()!= 0){
					int tagg = 0;
					for(int j = 0;j<collected_list.size();j++){
						if(collected_list.get(j).getMobileid().equals(listitem.getMobileid())){
							tagg = 1;
						}
					}
					switch(tagg){
					case 0:
						CollectedDbAccess.insertDish(VideoSecond.this, listitem);
						Toast.makeText(VideoSecond.this,"收藏视频成功", Toast.LENGTH_SHORT).show();
						video_shoucang.setText("已收藏");
						break;
					case 1:
						Toast.makeText(VideoSecond.this,"您已经收藏了此视频", Toast.LENGTH_SHORT).show();
						video_shoucang.setText("已收藏");
						break;
					}
				}else{
					CollectedDbAccess.insertDish(VideoSecond.this, listitem);
					Toast.makeText(VideoSecond.this,"收藏视频成功", Toast.LENGTH_SHORT).show();
					video_shoucang.setText("已收藏");
				}		
				break;
			case R.id.videos_da_bofang://播放按钮
				video_bofang.setImageResource(R.drawable.da_bofang1);
				if(videosContent !=null ){
				plaurl = videosContent.getPlayurl();
				Intent VideoSecond1_intent = new Intent();
				VideoSecond1_intent.putExtra("plaurl",plaurl);
				VideoSecond1_intent.setClass(VideoSecond.this,VideoViewStart.class);
				startActivity(VideoSecond1_intent);
				}else{
					Toast.makeText(VideoSecond.this,"网络连接失败，请检查网络设置！", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.pictures_bib1:
				startActivity(new Intent(VideoSecond.this,NewsMainActivity.class));
				break;
			case R.id.pictures_bib2:
				startActivity(new Intent(VideoSecond.this,Videos_Main.class));
				break;
			case R.id.pictures_bib3:
				startActivity(new Intent(VideoSecond.this,CollectsIndexActivity.class));
				break;
			case R.id.pictures_bib4:
				startActivity(new Intent(VideoSecond.this,Pictures_Main.class));
				break;	
			}
		}
	}
	class Gallery_Adapter extends BaseAdapter{
		Gallery_Holder holder;
		Context mContext;
		  public Gallery_Adapter(Context context) {
			   this.mContext = context;
		 }
		
		public int getCount() {
			int count;
			if(VideoSecond_list !=null && VideoSecond_list.size() != 0)
				count = VideoSecond_list.size();
			else
				count = 0;
			return count;
		}

		
		public Object getItem(int position) {
			return position;
		}

		
		public long getItemId(int position) {
			return position;
		}

		
		public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.videos_second_item,null);
				holder = new Gallery_Holder();
				holder.videos_xiao_bofang_bg1 = (ImageView)convertView.findViewById(R.id.videos_xiao_bofang_bg);
				holder.videos_xiao_bofang_tv1 = (TextView)convertView.findViewById(R.id.videos_xiao_bofang_tv);
				convertView.setTag(holder);
			}else{
				holder = (Gallery_Holder)convertView.getTag();
			}
			String videos_xiao_bofang_bg_path1 = VideoSecond_list.get(position).getThumb();//视频小图的链接地址 
			if(map1.containsKey(videos_xiao_bofang_bg_path1)){
				holder.videos_xiao_bofang_bg1.setImageBitmap(map1.get(videos_xiao_bofang_bg_path1));
			}else{
				holder.videos_xiao_bofang_bg1.setImageResource(R.drawable.videos_first_loading);
				holder.videos_xiao_bofang_bg1.setTag(videos_xiao_bofang_bg_path1);
				new Video_Small_ImageTask1().execute(holder.videos_xiao_bofang_bg1);
			}
			holder.videos_xiao_bofang_bg1.setDrawingCacheEnabled(true);
			holder.videos_xiao_bofang_tv1.setText(VideoSecond_list.get(position).getTitle());
			return convertView;
		}

	}
	static class Gallery_Holder{
		private TextView videos_xiao_bofang_tv1;//视频小图的标题
		private ImageView videos_xiao_bofang_bg1;//视频小图
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode) {
        case KeyEvent.KEYCODE_MENU:
        	if(flag==0){
        		Linear_video.setVisibility(View.VISIBLE);
            	flag = 1;
        	}else{
        		Linear_video.setVisibility(View.INVISIBLE);
        		flag = 0;
        	}
       	 	break;
        case KeyEvent.KEYCODE_BACK:
        	finish();
        	break;
        }
        return false;
	}
	
	
	protected void onDestroy() {
		super.onDestroy();
		listPanduan = null;
		map = null;
		map1 = null;
		video_bofang = null;
		videosSecond_title = null;
		videosSecond_bigimg = null;
		return_imageview = null;
		plaurl = null;
		video_mobileid = null;
		videosContent = null;
		path = null;
		videosSecond_content_text = null;
		catid = null;
		VideoSecond_list = null;
		video_shoucang = null;
		System.gc(); 
	}
}
