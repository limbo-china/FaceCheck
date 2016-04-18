package com.facecheck.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facecheck.StringDefine;
import com.facecheck.ic.Message;

public class GetMessage {

	
	public GetMessage(String phoneIMEI,String phoneNumber,String studyNumber,
			final SuccessCallback successCallback,final FailCallback failCallback){
		new NetConnection(StringDefine.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				try {
					JSONObject obj = new JSONObject(result);
					
					switch (obj.getInt(StringDefine.S_PERMISSION)) {
					case StringDefine.PERMISSION_SUCCESS:
						if(successCallback != null){
							List<Message> msgs = new ArrayList<Message>();
							JSONArray msgJsonArray = obj.getJSONArray(StringDefine.KEY_MESSAGE);
							JSONObject msgObj = null;
							for (int i = 0; i < msgJsonArray.length(); i++) {
								msgObj = msgJsonArray.getJSONObject(i);
								msgs.add(new Message(msgObj.getString(StringDefine.KEY_LESSON_NAME)
										,msgObj.getString(StringDefine.KEY_MESSAGE)));
							}
							
							successCallback.onSuccess(msgs);
						}
						break;

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
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				// TODO Auto-generated method stub
				if(failCallback != null){
					failCallback.onFail(StringDefine.PERMISSION_FAIL);
				}
			}
		}, null,StringDefine.AC_TYPE, StringDefine.ACTION_GET_MESSAGE,
		StringDefine.KEY_PHONE_IMEI, phoneIMEI, 
		StringDefine.KEY_PHONE_NUMBER,phoneNumber,
		StringDefine.KEY_STUDY_NUMBER,studyNumber
		);
	}

	public static interface SuccessCallback{
		void onSuccess(List<Message> messages);
	}
	public static interface FailCallback{
		void onFail(int errorCode);
	}
}

