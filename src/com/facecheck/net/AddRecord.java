package com.facecheck.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.facecheck.StringDefine;

public class AddRecord {
	
	public AddRecord(String idNumber, String classId,final SuccessCallback successCallback,final FailCallback failCallback){
		new Connection(StringDefine.SERVER_URL,Method.POST,new Connection.SuccessCallback() {
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				try {
					JSONObject res = new JSONObject(result);
					switch (res.getInt(StringDefine.S_PERMISSION)) {
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
				}catch (JSONException e) {
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
			},StringDefine.AC_TYPE,StringDefine.AC_ADDRecord,
			StringDefine.S_CLASSID,classId,
			StringDefine.S_IDNUMBER,idNumber);
		}
		public static interface SuccessCallback{
			void onSuccess();
		}
		public static interface FailCallback{
			void onFail(int errorStatus);
		}
}
