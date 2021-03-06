package com.facecheck.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.facecheck.StringDefine;

public class Login {
	
	public Login(String idNumber,String password,String userIdentity,final SuccessCallback successCallback,final FailCallback failCallback){
		new Connection(StringDefine.SERVER_URL,Method.POST,new Connection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				try {
					JSONObject obj = new JSONObject(result);
					switch (obj.getInt(StringDefine.S_PERMISSION)) {
					case StringDefine.PERMISSION_SUCCESS:
						if(successCallback != null){
							successCallback.onSuccess();
						}
						break;
					case StringDefine.PERMISSION_ERR:
						if(failCallback != null){
							failCallback.onFail(StringDefine.PERMISSION_ERR);
						}
					default:
						if(failCallback != null){
							failCallback.onFail(StringDefine.PERMISSION_FAIL);
						}
						break;
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(failCallback != null){
						failCallback.onFail(StringDefine.PERMISSION_FAIL);
					}
				}
			}
		},new Connection.FailCallback() {
			
			@Override
			public void onFail() {
				// TODO Auto-generated method stub
				if(failCallback != null){
					failCallback.onFail(StringDefine.PERMISSION_FAIL);
				}
				
			}
		},StringDefine.AC_TYPE,StringDefine.AC_LOGIN,
		StringDefine.S_IDNUMBER,idNumber,
		StringDefine.S_PASSWORD,password,
		StringDefine.S_USER_IDENTITY,userIdentity);
	}
	
	public static interface SuccessCallback{
		void onSuccess();
	}
	public static interface FailCallback{
		void onFail(int errorStatus);
	}

}
