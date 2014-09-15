package com.cntv.magazine.start;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cntv.magazine.R;
import com.cntv.magazine.news.NewsMainActivity;
import com.cntv.magazine.news.NewsTextActivity;
import com.cntv.magazine.videos.VideoSecond;


public class StartActivity extends Activity {
	ImageView start;//开始界面背景
	ProgressDialog pg;
	int tag;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        start = (ImageView)findViewById(R.id.start_image);
        pg= ProgressDialog.show(StartActivity.this, "欢迎", "精彩一刻，即将呈现");
        MyThread mt = new MyThread();
        mt.start();
        start.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tag = 1;
				Intent intent = new Intent();
				intent.setClass(StartActivity.this, NewsMainActivity.class);
				startActivity(intent);
				
			}
		});
    }
    
    
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
    	if(tag==1){
    		pg.dismiss();
    		 new AlertDialog.Builder(this)
             .setTitle("提示")
             .setMessage("确定退出吗？")
             .setPositiveButton("确定",
               new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0,
                  int arg1) {
                 // TODO Auto-generated method stub
                finish();

                }
               })
             .setNegativeButton("取消",
               new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0,
                  int arg1) {
                 // TODO Auto-generated method stub

                }
               }).show();
    		
    	}

		super.onResume();
	}



	class MyThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(3000);
				Intent intent = new Intent();
				intent.setClass(StartActivity.this, NewsMainActivity.class);
				tag = 1;
				startActivity(intent);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    }
    
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("ping", "===================");
		start = null;
		pg = null;
		if(NewsTextActivity.mHandler!=null){
			Message msg = new Message();
			msg.what = 1;
			NewsTextActivity.mHandler.sendMessage(msg);
		}
		if(VideoSecond.mHandler!=null){
			Message msg = new Message();
			msg.what = 1;
			VideoSecond.mHandler.sendMessage(msg);
		}
		this.deleteDatabase("webview.db");
		this.deleteDatabase("webviewCache.db");
		int count = this.fileList().length;   
		for(int i =0;i<count;i++){
			deleteFile(this.fileList()[0]); 
		}   
		super.onDestroy();
        android.os.Process
        .killProcess(android.os.Process
          .myPid());
	}
    
    
}