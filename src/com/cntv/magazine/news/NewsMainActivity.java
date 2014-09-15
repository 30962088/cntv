package com.cntv.magazine.news;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.cntv.magazine.entity.KindItem;
import com.cntv.magazine.entity.ListItem;
import com.cntv.magazine.pictures.Pictures_Main;
import com.cntv.magazine.utils.JsonUtils;
import com.cntv.magazine.utils.ServerDataInterface;
import com.cntv.magazine.videos.Video_Tools;
import com.cntv.magazine.videos.Videos_Main;

public class NewsMainActivity extends Activity {
	String md;//��Կ
	//private int size;//����List�б�ĳ���
	private String news_res;//�ӿ�����
	private String tuijian_res;//�Ƽ���������
	private ImageView news_news;//����
	private ImageView news_videos;//��Ƶ
	private ImageView news_collect;//�ղ�
	private ImageView news_image;//ͼƬ
	private ImageView news_center_img;//ͼƬ
	private TextView news_industry;//��ҵ����
	private TextView news_web_game;//������Ϸ
	private TextView news_single_game;//������Ϸ
	private TextView news_home_game;//������Ϸ
	private TextView news_more_channel;//����Ƶ��
	private TextView news_tuijian_title;//�Ƽ����ŵı���
	private ListView news_list;//װ�����ŵ�list
	private ListViewAdapter adapter;//������
	ArrayList<ListItem> dataList = new ArrayList<ListItem>();//���е����ŵ��б�
	ArrayList<ListItem> picList = new ArrayList<ListItem>();//�Ƽ������б�
	ArrayList<KindItem> padList = new ArrayList<KindItem>();//�Ƽ������б�
	HashMap<String,Bitmap> map = new HashMap<String,Bitmap>();//���ڻ���bitmap
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_main);
		intView();
		
		adapter = new ListViewAdapter(this);	
		news_list.setAdapter(adapter);
		
		String i = String.valueOf(202);
		getData(i);
		
		//Ĭ��ѡ��
		news_industry.setBackgroundResource(R.drawable.news_topmenu_bg2);
		news_industry.setTextColor(Color.WHITE);
		news_web_game.setBackgroundResource(R.drawable.news_topmenu_bg1);
		news_web_game.setTextColor(Color.BLACK);
		news_single_game.setBackgroundResource(R.drawable.news_topmenu_bg1);
		news_single_game.setTextColor(Color.BLACK);
		news_home_game.setBackgroundResource(R.drawable.news_topmenu_bg1);
		news_home_game.setTextColor(Color.BLACK);
		news_news.setImageResource(R.drawable.news_news2);
		
		//��װmenu���Եĵ���¼�
		news_industry.setOnClickListener(new clickEvent());
		news_web_game.setOnClickListener(new clickEvent());
		news_single_game.setOnClickListener(new clickEvent());
		news_home_game.setOnClickListener(new clickEvent());
		news_more_channel.setOnClickListener(new clickEvent());
		news_news.setOnClickListener(new clickEvent());
		news_videos.setOnClickListener(new clickEvent());
		news_collect.setOnClickListener(new clickEvent());
		news_image.setOnClickListener(new clickEvent());
		//��ͼ����¼�		
		news_center_img.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(NewsTools.Tag==1){
					NewsTools.mobileId = picList.get(0).getMobileid();//��ȡ�����ݵ�Id
					startActivity(new Intent(NewsMainActivity.this,NewsTextActivity.class));
				}else{
					Toast.makeText(NewsMainActivity.this, "�Բ�����������ʧ��", Toast.LENGTH_SHORT).show();
				}
			}
		});
		//ListView�ļ����¼�
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
				
				startActivity(new Intent(NewsMainActivity.this,NewsTextActivity.class));
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

	//ȡ�÷��������ݵķ���
	private void getData(String i) {
		String md;
		md = ServerDataInterface.getMD5("11"+i+"adc5674059f996cc34baf91556cdb2cb");
		//��ͼ
		String urlStr = "http://app.games.cntv.cn/api.php?op=mobileapplist&client=1&type=1&pagesize=20&catid="+i+"&ishot=1&md5="+md;
		tuijian_res = ServerDataInterface.httpGet(urlStr);
		JsonUtils js = new JsonUtils();
		news_tuijian_title.setText("������Ϣ");
		news_center_img.setImageResource(R.drawable.news_index_img);
		if(tuijian_res != null){
    	    picList = js.parseListJson(tuijian_res, String.valueOf(i));//�Ƽ����ŵ�List
    	    if(picList != null && picList.size() != 0){
    	    	String thumb1;
    	    	NewsTools.Tag = 1;
        		NewsTools.mobileId = picList.get(0).getMobileid();
        		NewsTools.collect_Mobileid = picList.get(0).getMobileid();//��ȡ�����ݵ�Id
        		NewsTools.collect_Title = picList.get(0).getTitle();//ȡ���Ƽ����ŵı���
    	    	NewsTools.collect_Ctitle = picList.get(0).getCtitle();//ȡ���Ƽ����ŵĸ�����
    	    	NewsTools.collect_Thumb = thumb1 = picList.get(0).getThumb();//ȡ���Ƽ����ŵ�ͼƬ��ַ
				NewsTools.collect_Catid = picList.get(0).getCatid();//ȡ���Ƽ�����Catid
				NewsTools.Collect_inputtime = picList.get(0).getInputtime();//ȡ���Ƽ����ŵ�ʱ���
/*				char[] ch = NewsTools.collect_Title.toCharArray();
				String ss ="";
				for(int t=0;t<ch.length;t++){
					if(ch[t]=='��'){
						ss = ss+"(";
					}else if(ch[t]=='��'){
						ss = ss+")";
					}else{
						ss = ss+ch[t];
					}
				}*/
				
				news_tuijian_title.setText(NewsTools.collect_Title);
        		//�����첽����ͼƬ�ķ���
        		news_center_img.setImageResource(R.drawable.news_index_img_loading);
        		news_center_img.setTag(thumb1);
        		new NewsTools.ImageLoadTask().execute(news_center_img);
        		news_center_img.setDrawingCacheEnabled(true);
    	    }else{
    	    	NewsTools.Tag = 0;
    	    }
        }else{
        	NewsTools.Tag = 0;
        	Toast.makeText(this, "��Ǹ��������������쳣���������磡", Toast.LENGTH_SHORT).show();
        }
		
        //�б�
        news_res = ServerDataInterface.httpGet("http://app.games.cntv.cn/api.php?op=mobileapplist&client=1&type=1&catid="+i+"&md5="+md);
        
        if(news_res != null){
        	//�б�������ʾ�������б�
    	    dataList = js.parseListJson(news_res, String.valueOf(i));
    	    Log.i("news_res", ""+news_res);
        }else{
        	Toast.makeText(this, "��Ǹ��������������쳣���������磡", Toast.LENGTH_SHORT).show();
        }
	}

	//ListView��������
	public class ListViewAdapter extends BaseAdapter{

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
			//�ѻ�ȡ����ʱ���ת��Ϊʱ��
			Long inputtime = Long.parseLong(dataList.get(position).getInputtime())*1000;
		    String data = new java.text.SimpleDateFormat("MM-dd/HH:mm").format(new java.util.Date(inputtime));
		    String thumb = dataList.get(position).getThumb();
			holder.new_list_title.setText(dataList.get(position).getTitle());
			holder.new_list_ctitle.setText(dataList.get(position).getCtitle());
			holder.new_list_time.setText(data);
			if(map.containsKey(thumb)){
				
				holder.news_list_image.setImageBitmap(map.get(thumb));
			}else{
				holder.news_list_image.setImageResource(R.drawable.news_list_img_loading);
				//�����첽����ͼƬ�ķ���
				holder.news_list_image.setTag(thumb);
				new ImageLoadTask().execute(holder.news_list_image);
			}
			holder.news_list_image.setDrawingCacheEnabled(true);
		    return convertView;
		}
		
	}
	
	//�Ż�ListView���¹����Ŀؼ���
	static class ViewHolder{
	    private TextView new_list_title;//ListView������ı� ����
		private TextView new_list_ctitle;//ListView������ı��� ����
		private TextView new_list_time;//ListView������ı��� ʱ��
		private ImageView news_list_image;//ListView�����ͼƬ
	}
	//�첽����ͼƬ
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
				this.mImgView.setImageBitmap(bm); // ����ͼƬ
				this.mImgView = null;
			}else if(bm == null){
				this.mImgView.setImageResource(R.drawable.news_list_img);
			}
		}
		protected void onCancelled(){
			super.onCancelled();
		}
	}
	//menu������ť�ļ����¼�
	class clickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.news_industry://��ҵ����
				news_industry.setBackgroundResource(R.drawable.news_topmenu_bg2);
				news_industry.setTextColor(Color.WHITE);
				news_web_game.setBackgroundResource(R.drawable.news_topmenu_bg1);
				news_web_game.setTextColor(Color.BLACK);
				news_single_game.setBackgroundResource(R.drawable.news_topmenu_bg1);
				news_single_game.setTextColor(Color.BLACK);
				news_home_game.setBackgroundResource(R.drawable.news_topmenu_bg1);
				news_home_game.setTextColor(Color.BLACK);
				news_more_channel.setBackgroundResource(R.drawable.news_topmenu_more_bg1);
				news_more_channel.setTextColor(Color.BLACK);
				getData(String.valueOf(202));
				adapter.notifyDataSetChanged();//ˢ��������
				break;
			case R.id.news_web_game://������Ϸ
				news_industry.setBackgroundResource(R.drawable.news_topmenu_bg1);
				news_industry.setTextColor(Color.BLACK);
				news_web_game.setBackgroundResource(R.drawable.news_topmenu_bg2);
				news_web_game.setTextColor(Color.WHITE);
				news_single_game.setBackgroundResource(R.drawable.news_topmenu_bg1);
				news_single_game.setTextColor(Color.BLACK);
				news_home_game.setBackgroundResource(R.drawable.news_topmenu_bg1);
				news_home_game.setTextColor(Color.BLACK);
				news_more_channel.setBackgroundResource(R.drawable.news_topmenu_more_bg1);
				news_more_channel.setTextColor(Color.BLACK);
				getData(String.valueOf(203));
				adapter.notifyDataSetChanged();
				break;
			case R.id.news_single_game://������Ϸ
				news_industry.setBackgroundResource(R.drawable.news_topmenu_bg1);
				news_industry.setTextColor(Color.BLACK);
				news_web_game.setBackgroundResource(R.drawable.news_topmenu_bg1);
				news_web_game.setTextColor(Color.BLACK);
				news_single_game.setBackgroundResource(R.drawable.news_topmenu_bg2);
				news_single_game.setTextColor(Color.WHITE);
				news_home_game.setBackgroundResource(R.drawable.news_topmenu_bg1);
				news_home_game.setTextColor(Color.BLACK);
				news_more_channel.setBackgroundResource(R.drawable.news_topmenu_more_bg1);
				news_more_channel.setTextColor(Color.BLACK);
				getData(String.valueOf(204));
				adapter.notifyDataSetChanged();
				break;
			case R.id.news_home_game://������Ϸ
				news_industry.setBackgroundResource(R.drawable.news_topmenu_bg1);
				news_industry.setTextColor(Color.BLACK);
				news_web_game.setBackgroundResource(R.drawable.news_topmenu_bg1);
				news_web_game.setTextColor(Color.BLACK);
				news_single_game.setBackgroundResource(R.drawable.news_topmenu_bg1);
				news_single_game.setTextColor(Color.BLACK);
				news_home_game.setBackgroundResource(R.drawable.news_topmenu_bg2);
				news_home_game.setTextColor(Color.WHITE);
				news_more_channel.setBackgroundResource(R.drawable.news_topmenu_more_bg1);
				news_more_channel.setTextColor(Color.BLACK);
				getData(String.valueOf(205));
				adapter.notifyDataSetChanged();
				break;
			case R.id.news_more_channel://����Ƶ��
				startActivity(new Intent(NewsMainActivity.this,NewsChannelActivity.class));
				break;
				
			case R.id.index_news://����
				news_news.setImageResource(R.drawable.news_news2);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures1);
				break;
			case R.id.index_videos://��Ƶ
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos2);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures1);
				Video_Tools.Num = 0;
				startActivity(new Intent(NewsMainActivity.this,Videos_Main.class));
				break;
			case R.id.index_collects://�ղ�
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects2);
				news_image.setImageResource(R.drawable.news_pictures1);
				startActivity(new Intent(NewsMainActivity.this,CollectsIndexActivity.class));
				break;
			case R.id.index_pic://ͼƬ
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures2);
				startActivity(new Intent(NewsMainActivity.this,Pictures_Main.class));
				break;
			}
		}
		
	}
    //����id��ÿؼ���ʵ�����
	private void intView() {
		news_list = (ListView)findViewById(R.id.news_listview);
		news_industry = (TextView)findViewById(R.id.news_industry);
		news_web_game = (TextView)findViewById(R.id.news_web_game);
		news_single_game = (TextView)findViewById(R.id.news_single_game);
		news_home_game = (TextView)findViewById(R.id.news_home_game);
		news_more_channel = (TextView)findViewById(R.id.news_more_channel);
		news_news = (ImageView)findViewById(R.id.index_news);
		news_videos = (ImageView)findViewById(R.id.index_videos);
		news_collect = (ImageView)findViewById(R.id.index_collects);
		news_image = (ImageView)findViewById(R.id.index_pic);
		news_tuijian_title = (TextView)findViewById(R.id.index_tuijian_title);
		news_center_img = (ImageView)findViewById(R.id.index_img);
	}
	//���ٵķ���
	@Override
	protected void onDestroy() {
		super.onDestroy();
		news_industry = null;
		news_web_game = null;
		news_single_game = null;
		news_home_game = null;
		news_more_channel = null;
		news_news = null;
		news_videos = null;
		news_collect = null;
		news_image = null;
		news_list = null;
		news_center_img = null;
		news_tuijian_title = null;
		map = null;
		System.gc();
	}

}
