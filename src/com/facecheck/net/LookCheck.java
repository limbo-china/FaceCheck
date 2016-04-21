package com.facecheck.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.facecheck.StringDefine;

public class LookCheck {
	
	JSONObject res;
	
	public LookCheck(String classId,String classTime,final SuccessCallback successCallback,final FailCallback failCallback){
		new Connection(StringDefine.SERVER_URL,Method.POST,new Connection.SuccessCallback() {
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				try {
					res = new JSONObject(result);
					switch (res.getInt(StringDefine.S_PERMISSION)) {
					case StringDefine.PERMISSION_SUCCESS:
						if(successCallback != null){
							successCallback.onSuccess(res);
						}
						break;
					case StringDefine.PERMISSION_ERR:
						if(failCallback != null){
							failCallback.onFail(StringDefine.PERMISSION_ERR,res);
						}
						break;
					case StringDefine.PERMISSION_TIMEERR:
						if(failCallback != null){
							failCallback.onFail(StringDefine.PERMISSION_TIMEERR,res);
						}
						break;
					default:
						if(failCallback != null){
							failCallback.onFail(StringDefine.PERMISSION_FAIL,res);
						}
						break;
					}
				}catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(failCallback != null){
						failCallback.onFail(StringDefine.PERMISSION_FAIL,res);
					}
				}
			}
			},new Connection.FailCallback() {
				
				@Override
				public void onFail() {
					// TODO Auto-generated method stub
					if(failCallback != null){
						failCallback.onFail(StringDefine.PERMISSION_FAIL,res);
					}
					
				}
			},StringDefine.AC_TYPE,StringDefine.AC_LOOKCHECK,
			StringDefine.S_CLASSID,classId,
			StringDefine.S_CLASSTIME,classTime);
		}
		public static interface SuccessCallback{
			void onSuccess(JSONObject res);
		}
		public static interface FailCallback{
			void onFail(int errorStatus,JSONObject res);
		}
}
