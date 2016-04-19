package com.facecheck.activity;
import com.facecheck.config.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class St_NaviActivity extends TabActivity implements OnCheckedChangeListener{
	private TabHost tabHost;
	private RadioGroup radioderGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.st_navi);
		tabHost=this.getTabHost();
		tabHost.addTab(tabHost.newTabSpec("1").setIndicator("1").setContent(new Intent(this,St_TableActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("2").setIndicator("2").setContent(new Intent(this,St_PictureActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("3").setIndicator("3").setContent(new Intent(this,Activity3.class)));

		radioderGroup = (RadioGroup) findViewById(R.id.st_radio);
		radioderGroup.setOnCheckedChangeListener(this);
		radioderGroup.check(R.id.stTabs_class);//默认第一个按钮
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.stTabs_class:
			tabHost.setCurrentTabByTag("1");
			break;
		case R.id.stTabs_face:
			tabHost.setCurrentTabByTag("2");
			break;
		case R.id.stTabs_selfInfo:
			tabHost.setCurrentTabByTag("3");
			break;
		}		
		
	}



}
