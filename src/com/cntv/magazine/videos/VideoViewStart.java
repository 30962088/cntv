package com.cntv.magazine.videos;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

import com.cntv.magazine.R;

public class VideoViewStart extends Activity {
	private String path,path1;
	private WebView mVideoView;
	int VW,VH;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.videos_videoview);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);	
		VW = dm.widthPixels;
		VH = dm.heightPixels;
		Intent VideoViewStart_intent = getIntent();
		path1 = VideoViewStart_intent.getStringExtra("plaurl"); 
		if(VW==800){
			VW = 540;
			VH = 300;
		}else if(VW==960){
			VW = 640;
			VH = 350;
		}
		path = path1+"&height="+VH+"&width="+VW;
		
		mVideoView = (WebView) findViewById(R.id.surface_view);
		mVideoView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		 WebSettings mWebSettings = mVideoView.getSettings();
		    mWebSettings.setJavaScriptEnabled(true);
		    mVideoView.getSettings().setPluginsEnabled(true);
		    mWebSettings.setPluginState(PluginState.ON);
		    mWebSettings.setBuiltInZoomControls(true);
		    mWebSettings.setDefaultTextEncodingName("utf-8");
		    mWebSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		    mWebSettings.setLoadWithOverviewMode(true);
		    mVideoView.loadUrl(path);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		path = null;
		mVideoView.destroy();
		mVideoView = null;
		System.gc();
	}
}
