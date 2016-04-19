package com.facecheck.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facecheck.MyClass;
import com.facecheck.StringDefine;
import com.facecheck.adapter.MyClassAdapter;
import com.facecheck.config.R;
import com.facecheck.net.GetMyClass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class Te_MyClassActivity extends Activity{
	private ListView listview;
    private List<MyClass> classlist = new ArrayList<MyClass>();
    private Context mContext = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.te_myclass);
        listview = (ListView)findViewById(R.id.myclass_listview);
        mContext=this;
        
        final ProgressDialog dialog = ProgressDialog.show(Te_MyClassActivity.this,"连接中","连接服务器中,请稍候");
        
        new GetMyClass(StringDefine.getCachedUserId(mContext),new GetMyClass.SuccessCallback() {
			
			@Override
			public void onSuccess(JSONObject res) {
				// TODO Auto-generated method stub
				try {
					JSONArray arr=res.getJSONArray("classes");
					for(int i=0;i<arr.length();i++){
						classlist.add(new MyClass(arr.getJSONObject(i).getString("id"),
								arr.getJSONObject(i).getString("name"),null,arr.getJSONObject(i).getString("timeLocation")));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MyClassAdapter adapter = new MyClassAdapter(Te_MyClassActivity.this,
						R.layout.te_myclass_sub, classlist);
				listview.setAdapter(adapter);
				dialog.dismiss();
				Toast.makeText(Te_MyClassActivity.this,"查询成功", Toast.LENGTH_LONG).show();
				System.out.println("success");
			}
		}, new GetMyClass.FailCallback() {
			
			@Override
			public void onFail(int errorStatus) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				switch (errorStatus) {
				case StringDefine.PERMISSION_FAIL:
					Toast.makeText(Te_MyClassActivity.this,"无法链接服务器,检查您的网络", Toast.LENGTH_LONG).show();
					break;
				case StringDefine.PERMISSION_ERR:
					Toast.makeText(Te_MyClassActivity.this,"还未加入课程", Toast.LENGTH_LONG).show();
					break;
				}
			}
		});
        findViewById(R.id.butaddret2).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Te_MyClassActivity.this.finish();
			}
		});
    }
}
