package com.cntv.magazine.collects;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cntv.magazine.R;
import com.cntv.magazine.entity.ListItem;
import com.cntv.magazine.news.NewsMainActivity;
import com.cntv.magazine.pictures.Pictures_Input;
import com.cntv.magazine.pictures.Pictures_Main;
import com.cntv.magazine.pictures.Pictures_details;
import com.cntv.magazine.utils.CollectedDbAccess;
import com.cntv.magazine.videos.Video_Tools;
import com.cntv.magazine.videos.Videos_Main;
public class CollectsContent_Pic extends Activity{
	private String aid;
	private GridView pictures_gv;//ͼƬ����
	private GridViewMyAdapter gv_myadapter;//gridView��������
	private ImageButton news_btn;//���Ű�ť
	private ImageButton video_btn;//��Ƶ��ť
	private ImageButton collects_btn;//�ղذ�ť
	private ImageView Video_Help;//����
	private ImageButton pictures_btn;//ͼƬ��ť
	private  ImageView pictures_fanhui;//���ذ�ť
	private ArrayList<ListItem> picList;//ȡ�õ��ղ�����
	private HashMap<String,Bitmap> map = new HashMap<String,Bitmap>();
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_main);
		
		picList = CollectedDbAccess.query_All_ListItem(this, 3);
		initView();//��ʼ���ؼ�
		initLis();//�����¼�
	}
	//��ʼ���ؼ�
	private void initView() {
		if(picList != null && picList.size() != 0 && picList.get(0) != null){
			picList.size();
		}else {
			Toast.makeText(this, "��û���ղص�ͼƬ��", Toast.LENGTH_SHORT).show();
		}
		pictures_gv = (GridView)findViewById(R.id.pictures_gv);
		gv_myadapter = new GridViewMyAdapter(this);
		pictures_gv.setAdapter(gv_myadapter); // ���GridView������
		
		news_btn = (ImageButton)findViewById(R.id.pictures_bib1);
		video_btn = (ImageButton)findViewById(R.id.pictures_bib2);
		collects_btn = (ImageButton)findViewById(R.id.pictures_bib3);
		pictures_btn = (ImageButton)findViewById(R.id.pictures_bib4);
		pictures_fanhui = (ImageView)findViewById(R.id.pictures_fanhui);
		Video_Help = (ImageView)findViewById(R.id.video_help1);
		
		
		
	}
	private void initLis() {
		news_btn.setOnClickListener(new btn_listen());
		video_btn.setOnClickListener(new btn_listen());
		collects_btn.setOnClickListener(new btn_listen());
		pictures_btn.setOnClickListener(new btn_listen());
		pictures_fanhui.setOnClickListener(new btn_listen());
		Video_Help.setOnClickListener(new btn_listen());
		GridView_Lis();
	}
	protected void GridView_Lis() {
		pictures_gv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				aid = picList.get(position).getMobileid();
				dialog();
				return false;
			}
		});
		pictures_gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
					Intent intent = new Intent();
					 Video_Tools.title = picList.get(position).getTitle();
			         Video_Tools.ctitle = picList.get(position).getCtitle();
			         Video_Tools.thumb = picList.get(position).getThumb();
			         Video_Tools.catid = picList.get(position).getCatid();
			         Video_Tools.inputtime = picList.get(position).getInputtime();
			         Video_Tools.video_mobileid = picList.get(position).getMobileid();
					intent.putExtra("pictures_mobileid", Video_Tools.video_mobileid);
					intent.putExtra("position", position+"");
					Video_Tools.Pic=1;
					intent.setClass(CollectsContent_Pic.this,Pictures_details.class);
					CollectsContent_Pic.this.startActivity(intent);
			}
		});
		
	}
	
	//GridView��������
	private class GridViewMyAdapter extends BaseAdapter{
		
		public GridViewMyAdapter(Activity a) {
		}
		@Override
		public int getCount() {
			int count;
			if(picList == null || picList.size() == 0)
				count = 0;
			else
				count = picList.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			final Pic_ViewHolder holder;
			if(convertView == null){
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pictures_main_gv,null);
				holder = new Pic_ViewHolder();
				holder.pictures_gv_img = (ImageView)convertView.findViewById(R.id.pictures_gv_img);
				convertView.setTag(holder);
			}else{
				holder = (Pic_ViewHolder)convertView.getTag();
			}
			
			String path = picList.get(position).getThumb();
			if(map.containsKey(path)){
				holder.pictures_gv_img.setImageBitmap(map.get(path));
			}else{
				holder.pictures_gv_img.setImageResource(R.drawable.img_small_loading);
				holder.pictures_gv_img.setTag(path);
				new Pic_Main_Small_img().execute(holder.pictures_gv_img);
			}
			holder.pictures_gv_img.setDrawingCacheEnabled(true);
			
			
			return convertView;
		}
	}
	static class Pic_ViewHolder{
	    private ImageView pictures_gv_img;//Gridview�����СͼƬչʾ
	}
	//��ť�����¼�
	private class btn_listen implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.pictures_bib1://���Ű�ť
					news_btn.setBackgroundResource(R.drawable.news_news2);
					video_btn.setBackgroundResource(R.drawable.news_videos1);
					collects_btn.setBackgroundResource(R.drawable.news_collects1);
					pictures_btn.setBackgroundResource(R.drawable.news_pictures1);
					Intent newsIntent = new Intent();
					newsIntent.setClass(CollectsContent_Pic.this,NewsMainActivity.class);
					startActivity(newsIntent);
					break;
				case R.id.pictures_bib2://��Ƶ��ť
					news_btn.setBackgroundResource(R.drawable.news_news1);
					video_btn.setBackgroundResource(R.drawable.news_videos2);
					collects_btn.setBackgroundResource(R.drawable.news_collects1);
					pictures_btn.setBackgroundResource(R.drawable.news_pictures1);
					Intent videoIntent = new Intent();
					videoIntent.setClass(CollectsContent_Pic.this,Videos_Main.class);
					startActivity(videoIntent);
					break;
				case R.id.pictures_bib3://�ղذ�ť
					news_btn.setBackgroundResource(R.drawable.news_news1);
					video_btn.setBackgroundResource(R.drawable.news_videos1);
					collects_btn.setBackgroundResource(R.drawable.news_collects2);
					pictures_btn.setBackgroundResource(R.drawable.news_pictures1);
					Intent intentc = new Intent();
					intentc.setClass(CollectsContent_Pic.this, CollectsIndexActivity.class);
					startActivity(intentc);
					break;
				case R.id.pictures_bib4://ͼƬ��ť
					news_btn.setBackgroundResource(R.drawable.news_news1);
					video_btn.setBackgroundResource(R.drawable.news_videos1);
					collects_btn.setBackgroundResource(R.drawable.news_collects1);
					pictures_btn.setBackgroundResource(R.drawable.news_pictures2);
					Intent intentp = new Intent();
					intentp.setClass(CollectsContent_Pic.this, Pictures_Main.class);
					startActivity(intentp);
					break;
				case R.id.pictures_fanhui://���ذ�ť
					finish();
					break;
				case R.id.video_help1:
					dialog1();
					break;
			}
		}
	}
	//Gridview�첽����ͼƬ
	class Pic_Main_Small_img extends AsyncTask<ImageView, Void, Bitmap> {
        private ImageView gView;

        protected Bitmap doInBackground(ImageView... views) {
            Bitmap bmp = null;
            ImageView view = views[0];// ����iconUrl��ȡͼƬ����Ⱦ��iconUrl��url������view��tag�С�
            if (view.getTag() != null) {
            	bmp = Pictures_Input.read_Small_img1(view.getTag().toString());
            }
            this.gView = view;
            return bmp;
        }

        protected void onPostExecute(Bitmap bm) {
            if (bm != null && map != null) {
                map.put(this.gView.getTag().toString(), bm);                        
                this.gView.setImageBitmap(bm);//�˴�����ͼƬ
                this.gView = null;
            }
            if(bm == null){
            	this.gView.setImageResource(R.drawable.news_list_img);
            }
        }       
	}
	protected void dialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("���Ƿ�Ҫɾ����ͼƬ");
		builder.setTitle("��ȷ�ϣ� ");  
		builder.setPositiveButton("�ǵ�", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ArrayList<ListItem> list = new ArrayList<ListItem>();
				CollectedDbAccess.deleteCollect(CollectsContent_Pic.this,aid);
				if(picList !=null && picList.size()>0 ){
					for(int i=0;i<picList.size();i++){
						if(aid != picList.get(i).getMobileid()){
							list.add(picList.get(i));
						}
					}
				}else{
					picList.clear();
				}
				picList = list;
				gv_myadapter = new GridViewMyAdapter(CollectsContent_Pic.this);
				pictures_gv.setAdapter(gv_myadapter);
				gv_myadapter.notifyDataSetChanged();
			
			}
		});
		
		builder.setNegativeButton("����", new android.content.DialogInterface.OnClickListener() {   
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}	
		});  

		builder.create().show();

	}
	protected void dialog1() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("�������Ҫɾ���ղ����ݣ��볤���б�ѡ��");
		builder.setTitle("���� ");  
		builder.setPositiveButton("�ǵ�", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		map = null;
		pictures_gv = null;
		gv_myadapter = null;
		news_btn = null;
		video_btn = null;
		collects_btn = null;
		pictures_btn = null;
		pictures_fanhui = null;
		System.gc();
	}
}
