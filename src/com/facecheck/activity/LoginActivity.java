package com.facecheck.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.facecheck.StringDefine;
import com.facecheck.config.R;
import com.facecheck.net.Login;

public class LoginActivity extends Activity {
	private Context mContext;
	private RadioGroup Switch = null;
	private RadioButton Teacher = null;
	private CheckBox MemoryPassword = null;
	private int isMemory = 0;
	private EditText UserId = null;
	private EditText Password = null;
	private String userIdentity = StringDefine.IDENTITY_UNDERGRADUATE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mContext = this;
		UserId = (EditText) findViewById(R.id.etUserId);
		Password = (EditText) findViewById(R.id.etPassword);
		Switch = (RadioGroup) findViewById(R.id.rg_switch);
		Teacher = (RadioButton) findViewById(R.id.rb_teacher);
		MemoryPassword = (CheckBox) findViewById(R.id.cbMemoryPassword);
		MemoryPassword.setChecked(true);
		
		String mUserId = StringDefine.getCachedUserId(mContext);
		String mPassword = StringDefine.getCachedPassword(mContext);
		String mUserType = StringDefine.getCachedUserType(mContext);
		int mIsMemory = StringDefine.getCachedIsMemory(mContext);

			if(mIsMemory == 1){
				UserId.setText(mUserId);
				Password.setText(mPassword);
				if(userIdentity.equals(StringDefine.IDENTITY_TEACHER)){
					Teacher.setChecked(true);
				}else{
					Teacher.setChecked(false);
				}
			}
		
				
		Switch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == Teacher.getId()){
					userIdentity = StringDefine.IDENTITY_TEACHER;
				}else{
					userIdentity = StringDefine.IDENTITY_UNDERGRADUATE;
				}
				
			}
		});
		
		
		
		findViewById(R.id.btLogin).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(TextUtils.isEmpty(UserId.getText()) || TextUtils.isEmpty(Password.getText())){
					Toast.makeText(LoginActivity.this,"帐号和密码不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				
				final ProgressDialog pd = ProgressDialog.show(LoginActivity.this,"连接中","连接服务器中,请稍候");
				
				new Login(UserId.getText().toString(),Password.getText().toString(),
						userIdentity,new Login.SuccessCallback() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						pd.dismiss();
						Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
						if(MemoryPassword.isChecked()){
							isMemory = 1;
						}
						StringDefine.cacheUserInfo(mContext, UserId.getText().toString(), Password.getText().toString(), userIdentity,isMemory);
						Intent intent;
						if(userIdentity==StringDefine.IDENTITY_TEACHER)
							intent = new Intent(LoginActivity.this,Te_NaviActivity.class);
						else 
							intent = new Intent(LoginActivity.this,St_NaviActivity.class);
						startActivity(intent);
						LoginActivity.this.finish();
					}
				}, new Login.FailCallback() {
					
					@Override
					public void onFail(int errorStatus) {
						// TODO Auto-generated method stub
						pd.dismiss();
						switch (errorStatus) {
						case StringDefine.PERMISSION_FAIL:
							Toast.makeText(LoginActivity.this,"无法链接服务器,检查您的网络", Toast.LENGTH_LONG).show();
							break;
						case StringDefine.PERMISSION_ERR:
							Toast.makeText(LoginActivity.this,"帐号或者密码错误，请确认后重试", Toast.LENGTH_LONG).show();
							break;
						}
						
					}
				});
			}
		});
		
	}
}
