package com.cntv.magazine.news;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cntv.magazine.R;
import com.cntv.magazine.collects.CollectsIndexActivity;
import com.cntv.magazine.entity.ListItem;
import com.cntv.magazine.entity.NewsContent;
import com.cntv.magazine.pictures.Pictures_Main;
import com.cntv.magazine.utils.CollectedDbAccess;
import com.cntv.magazine.utils.JsonUtils;
import com.cntv.magazine.utils.ServerDataInterface;
import com.cntv.magazine.videos.Videos_Main;
/**
 * G14&G12�汾
 **/
public class NewsTextActivity extends Activity {
	private int size;//�б�ĳ���
	private WebView wv; //WebView
	private String date;//ʱ��
	private String text_title;//����
	private String url;//�������ݻ�˵������
	private ImageView news_fanhui;//����
	private ImageButton news_news;//����
	private ImageButton news_videos;//��Ƶ
	private ImageButton news_collect;//�ղ�
	private ImageButton news_image;//ͼƬ
	private Button text_collects;//�����ղ�
	private NewsContent newContent;//���ݵ�ַ;
	private int existTag;//�ж�HTML�ļ��Ƿ���ڵı�־
	private LinearLayout display_layout;//��ť�Ĳ���
	private String id = NewsTools.mobileId;//�Ӿ�̬����ȡ��������ݵ�ID
	public static ListItem collectsList = new ListItem();//�ղ��б�
	ArrayList<ListItem> sizeList;//�����ݿ���ȡ�����б�
	public static Handler mHandler;//��������WebView�����Handler
	private int flag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_text);
		intView();
		List<ListItem> listPanduan = CollectedDbAccess.query_All_ListItem(this,1);
		if(listPanduan != null){
			int count = listPanduan.size();
			for(int i=0;i<count;i++){
				if(id.equals(listPanduan.get(i).getMobileid())){
					text_collects.setText("���ղ�");
					break;
			}
		}
		}
		
		String md =ServerDataInterface.getMD5("1"+id+"adc5674059f996cc34baf91556cdb2cb");
		String urlStr = "http://app.games.cntv.cn/api.php?op=mobileappcontent&client=1&mobileid="+id+"&md5="+md;
		String text_res = ServerDataInterface.httpGet(urlStr);
		//�ж�text_res�Ƿ�Ϊ��
		if(text_res == null){
			Toast.makeText(this, "���������쳣�������ԣ�", Toast.LENGTH_SHORT).show();
		}else{
			JsonUtils ju = new JsonUtils();
			newContent = (NewsContent) ju.parseContent(text_res, 1);
			if(newContent != null){
				text_title = newContent.getTitle();
				Long date_time = Long.parseLong(newContent.getInputtime())*1000;
			    date = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm").format(new java.util.Date(date_time));
				url = newContent.getContent();
			}
		}
		
		WebSettings mWebSettings = wv.getSettings();
	    mWebSettings.setJavaScriptEnabled(true);
	    wv.getSettings().setPluginsEnabled(true);
	    mWebSettings.setPluginState(PluginState.ON);
	    mWebSettings.setBuiltInZoomControls(true);
	    mWebSettings.setDefaultTextEncodingName("utf-8");
	    mWebSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);//WebView�������Ӧ
       //����HTML
	    String html = "";
	    html+="<html>";
	        html+="<body>";
	        html +="<h4>";
	        html +="<center>";
	       //����
	        if(text_title != null){
	    		html+= text_title;
	    	}else{
	    		Toast.makeText(this, "������û�б��⣡", Toast.LENGTH_SHORT).show();
	    	}
	        html +="</center>";
	        html +="</h4>";
	        
	        //ʱ��
	        html +="<center>";
	        if(date != null){
	        	html += date;
	        }else{
	        	Toast.makeText(this, "ʱ�䲻���ڣ�", Toast.LENGTH_SHORT).show();
	        }
	        html +="</center>";
	    	//����
	    	
	    	if(url != null){
	    		html+=url;
	    	}else{
	    		Toast.makeText(this, "����ҳ���ݲ����ڣ�", Toast.LENGTH_SHORT).show();
	    	}
	    	html +="</body>";
    	html+="</html>";
    	String temp = "news"+id+".html";
    	int length = this.fileList().length;
    	for(int i=0;i<length;i++){
	    	if(this.fileList().length != 0 && fileList()[i].equals("news"+id+".html")){
	    		wv.loadUrl("file:///data/data/com.cntv.magazine/files/"+temp+"");
	    		existTag = 1;
	    	}
	    }
    	if(existTag==0){
	    	byte[] b = html.getBytes();
		    FileOutputStream fo;
			try {
				fo = openFileOutput("news"+id+".html", 0);
				fo.write(b);
				fo.flush();
				fo.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			wv.loadUrl("file:///data/data/com.cntv.magazine/files/"+temp+"");
	    }
    	
		//��װ������ť�ĸ��Եļ����¼�
		news_fanhui.setOnClickListener(new clickEvent());
		text_collects.setOnClickListener(new clickEvent());
		news_news.setOnClickListener(new clickEvent());
		news_videos.setOnClickListener(new clickEvent());
		news_collect.setOnClickListener(new clickEvent());
		news_image.setOnClickListener(new clickEvent());
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case 1:
				   if(wv!=null){
					   wv.clearCache(true);
					   wv.clearHistory();
					   wv.clearFormData();
					   wv = null;
				   }	
				   break;
				}
				super.handleMessage(msg);
			}
			
		};
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		List<ListItem> listPanduan = CollectedDbAccess.query_All_ListItem(this,1);
		if(listPanduan != null){
			int count = listPanduan.size();
			for(int i=0;i<count;i++){
				if(id.equals(listPanduan.get(i).getMobileid())){
					text_collects.setText("���ղ�");
					break;
				}
			}
		}else{
			text_collects.setText("�ղ�");
		}
		
	}	
	
	
	//������ť�ļ����¼�
	class clickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.news_fanhui:
				finish();
				break;
			case R.id.index_news://����
				startActivity(new Intent(NewsTextActivity.this,NewsMainActivity.class));
				break;
			case R.id.index_videos://��Ƶ
				startActivity(new Intent(NewsTextActivity.this,Videos_Main.class));
				break;
			case R.id.index_collects://�ղ�
				startActivity(new Intent(NewsTextActivity.this,CollectsIndexActivity.class));
				break;
			case R.id.index_pic://ͼƬ
				startActivity(new Intent(NewsTextActivity.this,Pictures_Main.class));
				break;
				
			case R.id.collects_btn:
				NewsTextActivity.collectsList.setType(1);
				NewsTextActivity.collectsList.setTitle(NewsTools.collect_Title);
				NewsTextActivity.collectsList.setCtitle(NewsTools.collect_Ctitle);
				NewsTextActivity.collectsList.setMobileid(NewsTools.collect_Mobileid);
				NewsTextActivity.collectsList.setThumb(NewsTools.collect_Thumb);
				NewsTextActivity.collectsList.setInputtime(NewsTools.Collect_inputtime);
				NewsTextActivity.collectsList.setCatid(NewsTools.collect_Catid);
				
				sizeList = CollectedDbAccess.query_All_ListItem(NewsTextActivity.this, 1);
				if(sizeList != null && sizeList.size() != 0){
					int Flag = 0;
					size = sizeList.size();
					for(int i=0;i<size;i++){
						if(sizeList.get(i).getMobileid().equals(NewsTools.mobileId))
						{ 
							Flag = 1;
							}
						}
					switch(Flag)
					{
					case 0:
						CollectedDbAccess.insertDish(NewsTextActivity.this, NewsTextActivity.collectsList);
						Toast.makeText(NewsTextActivity.this, "�ղسɹ���", Toast.LENGTH_SHORT).show();
						text_collects.setText("���ղ�");
						break;
					case 1:
						Toast.makeText(NewsTextActivity.this, "���������ղأ�", Toast.LENGTH_SHORT).show();
						break;
					}
				}
				else {
					CollectedDbAccess.insertDish(NewsTextActivity.this, NewsTextActivity.collectsList);
					text_collects.setText("���ղ�");
					Toast.makeText(NewsTextActivity.this, "�ղسɹ���", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	}
	//����id��ÿؼ���ʵ�����
	private void intView() {
		wv = (WebView)findViewById(R.id.myWebView);
		news_fanhui = (ImageView)findViewById(R.id.news_fanhui);
		text_collects = (Button)findViewById(R.id.collects_btn);
		display_layout = (LinearLayout)findViewById(R.id.display_layout);
		news_news = (ImageButton)findViewById(R.id.index_news);
		news_videos = (ImageButton)findViewById(R.id.index_videos);
		news_collect = (ImageButton)findViewById(R.id.index_collects);
		news_image = (ImageButton)findViewById(R.id.index_pic);
		text_collects.setText("�ղ�");
	}
	//��menu����ʾ�ײ���ť
	public boolean onKeyDown(int keyCode,KeyEvent event){
		switch(keyCode){
		case KeyEvent.KEYCODE_MENU:
			if(flag == 0){
				display_layout.setVisibility(View.VISIBLE);
				flag = 1;
			}else{
				display_layout.setVisibility(View.INVISIBLE);
				flag = 0;
			}
		break;
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		}
		return false;
	}
	//���ٵķ���
	@Override
	protected void onDestroy() {
		super.onDestroy();
		url = null;
		news_fanhui = null;
		text_collects = null;
		newContent = null;
		news_news = null;
		news_videos = null;
		news_collect = null;
		news_image = null;
		System.gc();
	}

}
