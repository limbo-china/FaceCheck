package com.facecheck.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facecheck.StringDefine;

public class GetClass {

	public GetClass(String userId,String password,String semester,String userIdentity,final SuccessCallback successCallback,final FailCallback failCallback){
		new NetConnection(StringDefine.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				try {
					JSONObject obj = new JSONObject(result);
					
					switch (obj.getInt(StringDefine.S_PERMISSION)) {
					case StringDefine.PERMISSION_SUCCESS:
						if(successCallback != null){
							
							//JSONObject objUserInfo = obj.getJSONObject("info_of_user");
							//String info_of_classes = obj.getString(Config.KEY_INFO_OF_CLASSES);
							JSONArray ArrayClasses = obj.getJSONArray(StringDefine.S_CLASSES);
							//System.out.println(objClasses);
							successCallback.onSuccess(ArrayClasses);
						}
						break;
					case StringDefine.RESULT_STATUS_SEMESTER_ERR:
						if(failCallback != null){
							failCallback.onFail(StringDefine.RESULT_STATUS_SEMESTER_ERR);
						}
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
						System.out.println("null");
						failCallback.onFail(StringDefine.PERMISSION_FAIL);
					}
				}
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				// TODO Auto-generated method stub
				if(failCallback != null){
					failCallback.onFail(StringDefine.PERMISSION_FAIL);
				}
			}
		},StringDefine.AC_TYPE, StringDefine.AC_GETCLASS,
		StringDefine.S_IDNUMBER,userId,
		StringDefine.S_PASSWORD,password,
		StringDefine.S_SEMESTER,semester,
		StringDefine.S_USER_IDENTITY,userIdentity);
	}

	public static interface SuccessCallback{
		void onSuccess(JSONArray ArrayClasses);
	}
	public static interface FailCallback{
		void onFail(int errorStatus);
	}
}
