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
	private int size;//����list�ĳ���
	private ImageView news_news;//����
	private ImageView news_videos;//��Ƶ
	private ImageView news_collect;//�ղ�
	private ImageView news_image;//ͼƬ
	private ImageButton content_help;//����
	private ImageButton content_fanhui;//����
	private ListView content_list; //list�б�
	private ContentAdapter adapter;//������
	private String long_mobileId;//����ListView��ȡ��Id��
	ArrayList<ListItem> newsList = new ArrayList<ListItem>();//���е����ŵ��б�
	HashMap<String,Bitmap> map = new HashMap<String,Bitmap>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.collects_news);
		intView();
		
		//Ĭ��ѡ��
		news_collect.setImageResource(R.drawable.news_collects2);	
		
		newsList = CollectedDbAccess.query_All_ListItem(this, 1);
		if(newsList != null && newsList.size() != 0 && newsList.get(0) != null){
			size = newsList.size();
		}else {
			Toast.makeText(this, "��û���ղص����ţ�", Toast.LENGTH_SHORT).show();
		}
		
		if(newsList != null && size != 0){
			adapter = new ContentAdapter(this);	
			content_list.setAdapter(adapter);
		}else{
			Toast.makeText(this, "��û���ղص����ţ�", Toast.LENGTH_SHORT).show();
		}
		
		//ListView����¼�
		content_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				NewsTools.mobileId = newsList.get(arg2).getMobileid();
				startActivity(new Intent(CollectsContent.this,NewsTextActivity.class));
			}
		});
		//ListView�ĳ����¼�
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
		//��װ������ť���Եĵ���¼�
		content_fanhui.setOnClickListener(new clickEvent());
		content_help.setOnClickListener(new clickEvent());
		news_news.setOnClickListener(new clickEvent());
		news_videos.setOnClickListener(new clickEvent());
		news_collect.setOnClickListener(new clickEvent());
		news_image.setOnClickListener(new clickEvent());
	}
	
	//ListView��������	
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
		    
		    //��ʱ���ת��Ϊʱ��
			Long time = Long.parseLong(newsList.get(position).getInputtime());
		    String data = new java.text.SimpleDateFormat("MM-dd/HH:mm").format(new java.util.Date(time));
		    holder.new_list_time.setText(data);
		    String thumb = newsList.get(position).getThumb();
		    if(thumb != null){
		    	if(map.containsKey(thumb)){
					holder.news_list_image.setImageBitmap(map.get(thumb));
				}else{
					//�����첽����ͼƬ�ķ���
					holder.news_list_image.setImageResource(R.drawable.news_list_img_loading);
					holder.news_list_image.setTag(thumb);
					new ImageLoadTask().execute(holder.news_list_image);
				}
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
	//�Ի���
	 @Override
		protected Dialog onCreateDialog(int a) {
			Dialog dialog=null;
			AlertDialog.Builder builder = new Builder(this);
			if(a == 1){
				builder.setMessage("�������Ҫɾ���ղ����ݣ��볤���б�ѡ��");
				builder.setTitle("���� "); 
				builder.setNegativeButton("ȷ��", new android.content.DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						dismissDialog(1);
					}
				});
				dialog=builder.create();
				}
			if(a == 2){
				builder.setMessage("ȷ��Ҫɾ������������");
				builder.setTitle("��ȷ�ϣ�");
				builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener() {
					
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
				builder.setNegativeButton("ȡ��", new android.content.DialogInterface.OnClickListener() {
					
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
			}
			else if(bm == null){
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
			case R.id.collects_fanhui:
				finish();
				break;
			case R.id.help_btn:
				showDialog(1);
				break;
				
			case R.id.index_news://����
				news_news.setImageResource(R.drawable.news_news2);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures1);
				startActivity(new Intent(CollectsContent.this,NewsMainActivity.class));
				break;
				
			case R.id.index_videos://��Ƶ
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos2);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures1);
				startActivity(new Intent(CollectsContent.this,Videos_Main.class));
				break;
				
			case R.id.index_collects://�ղ�
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects2);
				news_image.setImageResource(R.drawable.news_pictures1);
				break;
				
			case R.id.index_pic://ͼƬ
				news_news.setImageResource(R.drawable.news_news1);
				news_videos.setImageResource(R.drawable.news_videos1);
				news_collect.setImageResource(R.drawable.news_collects1);
				news_image.setImageResource(R.drawable.news_pictures2);
				startActivity(new Intent(CollectsContent.this,Pictures_Main.class));
				break;
				}
			}
		}
	//����id��ÿؼ�ʵ�����
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
