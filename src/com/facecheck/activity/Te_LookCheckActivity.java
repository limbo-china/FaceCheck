package com.facecheck.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facecheck.StringDefine;
import com.facecheck.StuInfo;
import com.facecheck.adapter.StuInfoAdapter;
import com.facecheck.config.R;
import com.facecheck.net.LookCheck;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Te_LookCheckActivity extends Activity{
final private static String TAG = "Te_LookCheckActivity";

	private EditText classId = null;
	private EditText classTime=null;
	private TextView tv1=null;
	private TextView tv2=null;
	private TextView tv3=null;
	private String name=null;
	private String times=null;
	private Context mContext = null;
	private ListView listview;
    private List<StuInfo> stulist = new ArrayList<StuInfo>();
    StuInfoAdapter adapter=null;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.te_lookcheck);
		
		tv1=(TextView)this.findViewById(R.id.name);
        tv2=(TextView)this.findViewById(R.id.times);
        tv3=(TextView)this.findViewById(R.id.textViewlook);
		mContext=this;	
		listview = (ListView)findViewById(R.id.lookcheck_listview);
		classId = (EditText) findViewById(R.id.classIdlook);      
		classTime=(EditText)findViewById(R.id.classTimelook);
		
		findViewById(R.id.butlookchecked).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(classId.getText())){
					Toast.makeText(Te_LookCheckActivity.this,"课程编号不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				if(TextUtils.isEmpty(classTime.getText())){
					Toast.makeText(Te_LookCheckActivity.this,"考勤次数不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				Pattern p = Pattern.compile("[0-9]*"); 
			    Matcher m = p.matcher(classTime.getText().toString()); 
			    if(!m.matches() ){
			    	 Toast.makeText(Te_LookCheckActivity.this,"考勤次数必须为大于0的整数", Toast.LENGTH_LONG).show();
						return;
			     } 
				if( Integer.parseInt(classTime.getText().toString())<=0){
					Toast.makeText(Te_LookCheckActivity.this,"考勤次数必须为大于0的整数", Toast.LENGTH_LONG).show();
					return;
				}
				stulist.clear();
				adapter = new StuInfoAdapter(Te_LookCheckActivity.this,
						R.layout.te_lookcheck_sub, stulist);
				listview.setAdapter(adapter);
				final ProgressDialog dialog = ProgressDialog.show(Te_LookCheckActivity.this,"连接中","连接服务器中,请稍候");
				
				System.out.println("classId:" + classId.getText().toString());
				
				new LookCheck(classId.getText().toString(),classTime.getText().toString(),new LookCheck.SuccessCallback() {
					
					@Override
					public void onSuccess(JSONObject res) {
						// TODO Auto-generated method stub
						try {
							JSONArray arr=res.getJSONArray("students");
							for(int i=0;i<arr.length();i++){
								stulist.add(new StuInfo(arr.getJSONObject(i).getString("idNumber"),
										arr.getJSONObject(i).getString("name"),arr.getJSONObject(i).getString("stuClass"),
											arr.getJSONObject(i).getString("field"),null));
							name =res.getString("className");
							times =res.getString("times");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						adapter = new StuInfoAdapter(Te_LookCheckActivity.this,
								R.layout.te_lookcheck_sub, stulist);
						listview.setAdapter(adapter);
						tv1.setText(name);
						tv2.setText("共进行了"+times+"次签到");
						tv3.setText("第"+classTime.getText().toString()+"次已签到学生:");
						dialog.dismiss();
						Toast.makeText(Te_LookCheckActivity.this,"查询成功", Toast.LENGTH_LONG).show();
						System.out.println("success");
						
					
					}
				}, new LookCheck.FailCallback() {
					
					@Override
					public void onFail(int errorStatus,JSONObject res) {
						// TODO Auto-generated method stub
						String classTime=null;
						try {
							classTime = res.getString("classTime");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dialog.dismiss();
						switch (errorStatus) {
						case StringDefine.PERMISSION_FAIL:
							Toast.makeText(Te_LookCheckActivity.this,"无法链接服务器,检查您的网络", Toast.LENGTH_LONG).show();
							break;
						case StringDefine.PERMISSION_ERR:
							Toast.makeText(Te_LookCheckActivity.this,"不存在此课程或该课程没有考勤信息", Toast.LENGTH_LONG).show();
							break;
						case StringDefine.PERMISSION_TIMEERR:
							Toast.makeText(Te_LookCheckActivity.this,"此课程只进行了"+classTime+"次考勤", Toast.LENGTH_LONG).show();
							break;
						}
					}
				});
			}
		});
		
		findViewById(R.id.butlookret).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Te_LookCheckActivity.this.finish();
			}
		});
	}
}
