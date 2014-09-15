package com.cntv.magazine.collects.bll;

import java.util.ArrayList;
import java.util.List;

import com.cntv.magazine.R;
import com.cntv.magazine.entity.ChannelPerson;

public class CollectsGrallery {
	public static List<ChannelPerson> getCollectsGrallery(){
		List<ChannelPerson> channelList = new ArrayList<ChannelPerson>();
		
		ChannelPerson ap = new ChannelPerson();
		ap.setName("–¬Œ≈");
		ap.setId(1);
		ap.setDrawableId(R.drawable.collects_news);
		channelList.add(ap);
		
		/*ChannelPerson ap1 = new ChannelPerson();
		ap1.setName(" ”∆µ");
		ap1.setId(2);
		ap1.setDrawableId(R.drawable.collects_videos);
		channelList.add(ap1);
		
		ChannelPerson ap2 = new ChannelPerson();
		ap2.setName("Õº∆¨");
		ap2.setId(3);
		ap2.setDrawableId(R.drawable.collects_pictures);
		channelList.add(ap2);*/
		
		return channelList;
	}

}
