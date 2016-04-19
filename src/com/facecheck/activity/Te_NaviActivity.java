package com.facecheck.activity;
import com.facecheck.config.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class Te_NaviActivity extends TabActivity implements OnCheckedChangeListener{
	private TabHost tabHost;
	private RadioGroup radioderGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.te_navi);
		tabHost=this.getTabHost();
		tabHost.addTab(tabHost.newTabSpec("1").setIndicator("1").setContent(new Intent(this,Te_FaceCheckActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("2").setIndicator("2").setContent(new Intent(this,Te_InfoActivity.class)));

		radioderGroup = (RadioGroup) findViewById(R.id.te_radio);
		radioderGroup.setOnCheckedChangeListener(this);
		radioderGroup.check(R.id.teTabs_check);//默认第一个按钮
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.teTabs_check:
			tabHost.setCurrentTabByTag("1");
			break;
		case R.id.teTabs_info:
			tabHost.setCurrentTabByTag("2");
			break;
		}		
		
	}



}
