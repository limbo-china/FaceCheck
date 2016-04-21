package com.facecheck.activity;

import org.json.JSONException;
import org.json.JSONObject;


import com.facecheck.StringDefine;
import com.facecheck.config.R;
import com.facecheck.net.MyInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class St_MyInfoActivity extends Activity{


	private Context mContext = null;
	private TextView tv2=null;
	private TextView tv4=null;
	private TextView tv6=null;
	private TextView tv8=null;
	private TextView tv10=null;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st_myinfo);

        mContext=this;
        tv2=(TextView)this.findViewById(R.id.myinfotv2);
        tv4=(TextView)this.findViewById(R.id.myinfotv4);
    	tv6=(TextView)this.findViewById(R.id.myinfotv6);
    	tv8=(TextView)this.findViewById(R.id.myinfotv8);
    	tv10=(TextView)this.findViewById(R.id.myinfotv10);
        
        final ProgressDialog dialog = ProgressDialog.show(St_MyInfoActivity.this,"连接中","连接服务器中,请稍候");
        
        new MyInfo(StringDefine.getCachedUserId(mContext),new MyInfo.SuccessCallback() {
			
			@Override
			public void onSuccess(JSONObject res) {
				// TODO Auto-generated method stub
				try {
					tv2.setText(res.getString("name"));
					tv4.setText(res.getString("idNumber"));
					tv6.setText(res.getString("gender"));
					tv8.setText(res.getString("field"));
					tv10.setText(res.getString("stuclass"));			
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dialog.dismiss();
				Toast.makeText(St_MyInfoActivity.this,"查看成功", Toast.LENGTH_LONG).show();
			}
		}, new MyInfo.FailCallback() {
			
			@Override
			public void onFail(int errorStatus) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				switch (errorStatus) {
				case StringDefine.PERMISSION_FAIL:
					Toast.makeText(St_MyInfoActivity.this,"无法链接服务器,检查您的网络", Toast.LENGTH_LONG).show();
					break;
				case StringDefine.PERMISSION_ERR:
					Toast.makeText(St_MyInfoActivity.this,"未查询到信息", Toast.LENGTH_LONG).show();
					break;
			}
		}
		});
    }
}
