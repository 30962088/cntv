package com.cntv.magazine.pictures;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cntv.magazine.R;
import com.cntv.magazine.collects.CollectsIndexActivity;
import com.cntv.magazine.entity.KindItem;
import com.cntv.magazine.entity.ListItem;
import com.cntv.magazine.entity.PicturesContent;
import com.cntv.magazine.news.NewsMainActivity;
import com.cntv.magazine.utils.CollectedDbAccess;
import com.cntv.magazine.utils.JsonUtils;
import com.cntv.magazine.utils.ServerDataInterface;
import com.cntv.magazine.videos.Video_Tools;
import com.cntv.magazine.videos.Videos_Main;

public class Pictures_details extends Activity{
	private ImageView pictures_left;//页数递减按钮
	private ImageView pictures_right;//页数递增按钮
	private ImageView pictures_fanhui;//返回
	private Button Pic_collect;//图片收藏
	private ImageView pictures_img_big;//大图片
	private TextView pictures_title;//图片标题
	private TextView pictures_finalpage;//总页数
	private TextView pictures_currentpage;//当前页数
	//private int position;//指针
	private String pictures_mobileid;//内容ID，用于获取内容页详细内容
	private Intent intentp;//接收传来的数据
	private ArrayList<KindItem> pictures_data_list;//图片父类接口数据
	public static ArrayList<ListItem>  pictures_list1;//图片接口列表
	public static ArrayList<ListItem>  pictures_list2;//图片接口列表
	private PicturesContent  picturesContent;//图片接口列表
	private String path;//视频播放地址
	private ArrayList<ListItem>  collected_list;//收藏列表
	private String res;//接口地址
	private String catid;//栏目ID
	private String md;
	int Pic_index1;
	private String type = "3"; 
	private HashMap<String,Bitmap> map = new HashMap<String,Bitmap>();//用于缓存bitmap
	private float startX = 0.0f,endX = 0.0f;
	ArrayList<ListItem> listPanduan;
	private LinearLayout  Pic_layout;
	ImageButton news;
	ImageButton videos;
	ImageButton collects;
	ImageButton pictures;
	private int flag;
	int index;
	
	@Override
	protected void onResume() {
		super.onResume();
		
		intentp = getIntent();
		pictures_mobileid = intentp.getStringExtra("pictures_mobileid");
		Video_Tools.position = Integer.parseInt((intentp.getStringExtra("position")).toString());
		pictures_data_list = Pictures_Input.GetPituresData(3);
		if(pictures_data_list != null && pictures_data_list.size() != 0 ){
			catid = pictures_data_list.get(0).getCatid()+"";
			md = ServerDataInterface.getMD5("1"+type+catid+"adc5674059f996cc34baf91556cdb2cb");
			res = ServerDataInterface.httpGet("http://app.games.cntv.cn/api.php?op=mobileapplist&client=1&type="+type+"&catid="+catid+"&pagesize=20&md5="+md);
			if(res == null){
				Toast.makeText(Pictures_details.this,"网络连接失败，请检查网络设置！", Toast.LENGTH_SHORT).show();
			}else{
			JsonUtils json = new JsonUtils();
			pictures_list2 = json.parseListJson(res,catid);
			}
			
		}else{
			Toast.makeText(Pictures_details.this,"网络连接失败，请检查网络设置！", Toast.LENGTH_SHORT).show();
		} 
		listPanduan = CollectedDbAccess.query_All_ListItem(this, 3);
		switch(Video_Tools.Pic){
			case 0:
				pictures_list1 = pictures_list2;
				break;
			case 1:
				pictures_list1 = listPanduan;
				break;
		}
		
		pictures_mobileid = pictures_list1.get(Video_Tools.position).getMobileid();
		listPanduan = null;
		listPanduan = CollectedDbAccess.query_All_ListItem(this, 3);
		if(listPanduan!=null){
			int count = listPanduan.size();	
			Pic_collect.setText("收藏");
			for(int i=0;i<count;i++){
				if(pictures_mobileid.equals(listPanduan.get(i).getMobileid())){
					Pic_index1=1;
					Pic_collect.setText("已收藏");
					break;
				}
			}
		}
	}
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.pictures_details);		
		intentp = getIntent();
		pictures_mobileid = intentp.getStringExtra("pictures_mobileid");
		Video_Tools.position = Integer.parseInt((intentp.getStringExtra("position")).toString());
		pictures_data_list = Pictures_Input.GetPituresData(3);
		if(pictures_data_list != null && pictures_data_list.size() != 0 ){
			catid = pictures_data_list.get(0).getCatid()+"";
			md = ServerDataInterface.getMD5("1"+type+catid+"adc5674059f996cc34baf91556cdb2cb");
			res = ServerDataInterface.httpGet("http://app.games.cntv.cn/api.php?op=mobileapplist&client=1&type="+type+"&catid="+catid+"&pagesize=20&md5="+md);
			if(res == null){
				Toast.makeText(Pictures_details.this,"网络连接失败，请检查网络设置！", Toast.LENGTH_SHORT).show();
			}else{
			JsonUtils json = new JsonUtils();
			pictures_list2 = json.parseListJson(res,catid);
			}
			
		}else{
			Toast.makeText(Pictures_details.this,"网络连接失败，请检查网络设置！", Toast.LENGTH_SHORT).show();
		} 
		listPanduan = CollectedDbAccess.query_All_ListItem(this, 3);
		switch(Video_Tools.Pic){
			case 0:
				pictures_list1 = pictures_list2;
				break;
			case 1:
				pictures_list1 = listPanduan;
				break;
		}
		
		//初始化控件
		pictures_left = (ImageView)findViewById(R.id.pictures_left);
		pictures_right = (ImageView)findViewById(R.id.pictures_right);
		pictures_fanhui = (ImageView)findViewById(R.id.collects_fanhui);
		pictures_title = (TextView)findViewById(R.id.pictures_title);
		pictures_currentpage = (TextView)findViewById(R.id.pictures_currentpage);
		pictures_finalpage = (TextView)findViewById(R.id.pictures_finalpage);
		pictures_img_big = (ImageView)findViewById(R.id.pictures_img_big);
		Pic_collect = (Button)findViewById(R.id.Pic_collect);
		
		Pic_layout = (LinearLayout)findViewById(R.id.Pic_Layout);
	    news = (ImageButton)findViewById(R.id.pictures_bib1);
	    videos = (ImageButton)findViewById(R.id.pictures_bib2);
	    collects = (ImageButton)findViewById(R.id.pictures_bib3);
	    pictures = (ImageButton)findViewById(R.id.pictures_bib4);
	    
	    Pic_collect.setText("收藏");
	    if(listPanduan!=null){
			int count = listPanduan.size();		
			for(int i=0;i<count;i++){
				if(pictures_mobileid.equals(listPanduan.get(i).getMobileid())){
					Pic_index1=1;
					Pic_collect.setText("已收藏");
					break;
				}
			}
		}
	    
	    //四个按钮的点击事件
	    news.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Pictures_details.this,NewsMainActivity.class));
				
			}
		});
	    videos.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Pictures_details.this,Videos_Main.class));
				
			}
		});
	    collects.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Pictures_details.this,CollectsIndexActivity.class));
				
			}
		});
	    pictures.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Pictures_details.this,Pictures_Main.class));
			
			}
		});
		pictures_img_big.setScaleType(ImageView.ScaleType.FIT_CENTER);
		
		picturesContent = Pictures_Input.getPic_content(pictures_mobileid, 3);
		if(picturesContent!=null){
			path = picturesContent.getPicurl();
			
			pictures_img_big.setImageResource(R.drawable.img_big_loading);
			pictures_img_big.setTag(path);
			new CanvasBigImageTask().execute(pictures_img_big);			
			pictures_title.setText(picturesContent.getTitle());
			pictures_currentpage.setText(Video_Tools.position+1+"");
			pictures_finalpage.setText("/"+pictures_list1.size()+"");
		}

		
		listen();

		pictures_img_big.setOnTouchListener(new MulitPointTouchListener ()); 
	}
	//按钮监听事件
	private void  listen(){
		
		//页数递增按钮监听事件
		pictures_right.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Video_Tools.position++;
				if(Video_Tools.position == pictures_list1.size() ){
					Video_Tools.position = 0;
				}
				getPictures_big_img(Video_Tools.position);
			}
			
			
		});
		pictures_fanhui.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
		//页数递减按钮监听事件
		pictures_left.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				
				Video_Tools.position --;
				if(Video_Tools.position < 0){
					Video_Tools.position = pictures_list1.size()-1;
				}
				getPictures_big_img(Video_Tools.position);
			}
		});

		Pic_collect.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				collected_list = CollectedDbAccess.query_All_ListItem(Pictures_details.this, 3);
				ListItem listitem = new ListItem();//现在要插得数据
				
				if(collected_list != null && collected_list.size()!= 0){//判断现在要插得数据收藏列表中是否已存在
					int tagg = 0 ;
					for(int i=0;i<collected_list.size();i++){
						if(collected_list.get(i).getMobileid().equals(pictures_mobileid)){
							tagg = 1;
						}
					}
					switch(tagg){
					case 0:
						collectPic(listitem);
						Toast.makeText(Pictures_details.this,"收藏图片成功", Toast.LENGTH_SHORT).show();
						Pic_collect.setText("已收藏");
						break;
					case 1:
						Toast.makeText(Pictures_details.this,"您已经收藏了此图片", Toast.LENGTH_SHORT).show();
						Pic_collect.setText("已收藏");
						break;
					}
							
				}else{
					collectPic(listitem);
					Toast.makeText(Pictures_details.this,"收藏图片成功", Toast.LENGTH_SHORT).show();
					Pic_collect.setText("已收藏");
				}
					
					
			}

			private void collectPic(ListItem listitem) {
				if(pictures_list1 != null && pictures_list1.size() !=0){
					for(int i=0;i<pictures_list1.size();i++){
						if(pictures_list1.get(i).getMobileid().equals(pictures_mobileid) ){
							listitem.setType(3);
							listitem.setTitle(pictures_list1.get(i).getTitle());
							listitem.setCtitle(pictures_list1.get(i).getCtitle());
							listitem.setInputtime(pictures_list1.get(i).getInputtime());
							listitem.setMobileid(pictures_list1.get(i).getMobileid());
							listitem.setThumb(pictures_list1.get(i).getThumb());
							listitem.setCatid(pictures_list1.get(i).getCatid());
							CollectedDbAccess.insertDish(Pictures_details.this, listitem);
						}
					}
				}
			}
		});
	}
		
	
	public void getPictures_big_img(int position) {
		pictures_mobileid = pictures_list1.get(Video_Tools.position).getMobileid();
		listPanduan = CollectedDbAccess.query_All_ListItem(this, 3);
		if(listPanduan!=null){
			int count = listPanduan.size();	
			Pic_collect.setText("收藏");
			for(int i=0;i<count;i++){
				if(pictures_mobileid.equals(listPanduan.get(i).getMobileid())){
					
					Pic_index1=1;
					Pic_collect.setText("已收藏");
					break;
				}
			}
		}
		
		picturesContent = Pictures_Input.getPic_content(pictures_mobileid, 3);
		if(picturesContent != null){
		path = picturesContent.getPicurl();
		if(map.containsKey(path)){
			pictures_img_big.setImageBitmap(map.get(path));
		}else{
			pictures_img_big.setImageResource(R.drawable.img_big_loading);								
			pictures_img_big.setTag(path);
			new CanvasBigImageTask().execute(pictures_img_big);
		}
		pictures_img_big.setDrawingCacheEnabled(true);
		pictures_title.setText(picturesContent.getTitle());
		pictures_currentpage.setText(position+1+"");
		}else{
			Toast.makeText(Pictures_details.this,"网络连接失败，请检查网络设置！", Toast.LENGTH_SHORT).show();
		}
		
	}
	//异步加载图片
	class CanvasBigImageTask extends AsyncTask<ImageView, Void, Bitmap> {
        private ImageView gView;

        protected Bitmap doInBackground(ImageView... views) {
                Bitmap bmp = null;
                ImageView view = views[0];
                // 根据iconUrl获取图片并渲染，iconUrl的url放在了view的tag中。
                if (view.getTag() != null) {
                	bmp = Pictures_Input.read_Big_img(view.getTag().toString());
                }
                this.gView = view;
                return bmp;
        }

        protected void onPostExecute(Bitmap bm) {
                if (bm != null && map != null) { //此处更新图片
                	map.put(this.gView.getTag().toString(), bm);
                	pictures_img_big.setScaleType(ImageView.ScaleType.FIT_CENTER);//每次调用该方法时，都重新回置图片自适应居中属性
	                this.gView.setImageBitmap(bm);
	                this.gView = null;
                }else if(bm == null){
                	this.gView.setImageResource(R.drawable.img_big);
                }
        }       
	}
	
	public boolean onTouchEvent(MotionEvent event){
		pictures_img_big.setScaleType(ImageView.ScaleType.FIT_CENTER);
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			 startX = event.getX();
			 break;
		case MotionEvent.ACTION_UP:
			 endX = event.getX();
			 if(endX > startX){
				 Video_Tools.position--;
					if(Video_Tools.position < 0){
						Video_Tools.position = pictures_list1.size()-1;
					}
					getPictures_big_img(Video_Tools.position);


			}else{
				Video_Tools.position ++;
					if(Video_Tools.position == pictures_list1.size() ){
						Video_Tools.position = 0;
					}
					getPictures_big_img(Video_Tools.position);
			 }			 
			 break;
		}
		
		
		return true;
		
	}
	
		public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode) {
        case KeyEvent.KEYCODE_MENU:
        	if(flag==0){
        		Pic_layout.setVisibility(View.VISIBLE);
            	flag = 1;
        	}else{
        		Pic_layout.setVisibility(View.INVISIBLE);
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
		pictures_left = null;
		pictures_right = null;
		pictures_fanhui = null;
		pictures_img_big = null;
		pictures_title = null;
		pictures_finalpage = null;
		pictures_currentpage = null;
		//Video_Tools.position = 0;
		map = null;
		pictures_mobileid = null;
		intentp = null;
		pictures_list1 = null;
		Pic_collect = null;
		intentp = null;
		pictures_data_list = null;
		picturesContent = null;
		path = null;
		collected_list = null;
		res = null;
		catid = null;
		md = null;
		type = null; 
		System.gc();
	}
}
