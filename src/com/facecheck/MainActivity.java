package com.facecheck;

import com.facecheck.activity.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	  
        	 startActivity(new Intent(this,LoginActivity.class));  //用户联网,需登录成功才能访问主页面
             finish(); 
	}
}

