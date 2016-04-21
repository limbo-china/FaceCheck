package com.facecheck.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facecheck.MyClass;
import com.facecheck.StringDefine;
import com.facecheck.adapter.St_MyClassAdapter;
import com.facecheck.config.R;
import com.facecheck.net.GetClass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class St_TableActivity extends Activity {
	
	private ListView listview;
    private List<MyClass> classlist = new ArrayList<MyClass>();
	private Context mContext = null;

	private String semester;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st_myclass);
        
        listview = (ListView)findViewById(R.id.st_myclass_listview);
        mContext = this;
        semester ="2015-2016-2";
        final ProgressDialog dialog = ProgressDialog.show(mContext,"连接中","连接服务器中,请稍候");
		new GetClass(StringDefine.getCachedUserId(mContext), StringDefine
				.getCachedPassword(mContext), semester, StringDefine
				.getCachedUserType(mContext),
				new GetClass.SuccessCallback() {

					@Override
					public void onSuccess(JSONObject res) {
						// TODO Auto-generated method stub
						try {
							JSONArray arr=res.getJSONArray("classes");
							for(int i=0;i<arr.length();i++){
								classlist.add(new MyClass(arr.getJSONObject(i).getString("id"),
										arr.getJSONObject(i).getString("name"),arr.getJSONObject(i).getString("teacherName"),arr.getJSONObject(i).getString("timeLocation")));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						St_MyClassAdapter adapter = new St_MyClassAdapter(St_TableActivity.this,
								R.layout.st_myclass_sub, classlist);
						listview.setAdapter(adapter);
						dialog.dismiss();
						Toast.makeText(St_TableActivity.this,
								"导入课程成功", Toast.LENGTH_LONG).show();

					}
				}, new GetClass.FailCallback() {

					@Override
					public void onFail(int errorStatus) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						switch (errorStatus) {
						case StringDefine.RESULT_STATUS_SEMESTER_ERR:
							Toast.makeText(
									St_TableActivity.this,
									"教务系统暂无该学期的课程内容", Toast.LENGTH_LONG)
									.show();
							break;
						case StringDefine.PERMISSION_ERR:
							Toast.makeText(
									St_TableActivity.this,
									"帐号或者密码错误，请确认后重试",
									Toast.LENGTH_LONG).show();
							break;
						default:
							Toast.makeText(
									St_TableActivity.this,
									"无法链接服务器,检查您的网络", Toast.LENGTH_LONG)
									.show();
							break;
						}
					}
				});   
    }

}