package com.facecheck.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.facecheck.StringDefine;
import com.facecheck.config.R;
import com.facecheck.net.AddClass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Te_AddClassActivity extends Activity{
final private static String TAG = "Te_AddClassActivity";

	private EditText classId = null;
	private String classinfo=null;
	private Context mContext = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.te_addclass);
		
		mContext=this;
		
		classId = (EditText) findViewById(R.id.classIdadd);
		
		findViewById(R.id.butaddclass).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(classId.getText())){
					Toast.makeText(Te_AddClassActivity.this,"课程编号不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				final ProgressDialog dialog = ProgressDialog.show(Te_AddClassActivity.this,"连接中","连接服务器中,请稍候");
				
				System.out.println("classId:" + classId.getText().toString());
				
				new AddClass(StringDefine.getCachedUserId(mContext),classId.getText().toString(),new AddClass.SuccessCallback() {
					
					@Override
					public void onSuccess(JSONObject res) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						try {
							classinfo = "成功加入此课程\n"+"课程名:"
									+res.getString(StringDefine.S_CLASSNAME)+"\n"+"老师:"+res.getString(StringDefine.S_CLASSTEACHERNAME)+"\n";
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Toast.makeText(Te_AddClassActivity.this,classinfo, Toast.LENGTH_SHORT).show();
						//StringDefine.cacheUserInfo(mContext, etUserId.getText().toString(), etPassword.getText().toString(), userIdentity,isMemory);
						System.out.println("success");
						
					
					}
				}, new AddClass.FailCallback() {
					
					@Override
					public void onFail(int errorStatus) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						switch (errorStatus) {
						case StringDefine.PERMISSION_FAIL:
							Toast.makeText(Te_AddClassActivity.this,"无法链接服务器,检查您的网络", Toast.LENGTH_LONG).show();
							break;
						case StringDefine.PERMISSION_ERR:
							Toast.makeText(Te_AddClassActivity.this,"不存在此课程或已经加过此课程", Toast.LENGTH_LONG).show();
							break;
						}
					}
				});
			}
		});
		
		findViewById(R.id.butaddret).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Te_AddClassActivity.this.finish();
			}
		});
	}
}
