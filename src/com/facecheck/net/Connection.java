package com.facecheck.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.facecheck.StringDefine;

import android.os.AsyncTask;

public class Connection {
	
	public Connection(final String url,final Method method,final SuccessCallback successCallback, 
			final FailCallback failCallback,final String ... kvs){
		
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				StringBuffer paramsStr = new StringBuffer(); 
				paramsStr.append("{");
				for(int i = 0; i < kvs.length; i+=2){
					paramsStr.append("\"").append(kvs[i]).append("\"").append(":").append("\"").append(kvs[i+1]).append("\"").append(",");
				}	
				paramsStr =  new StringBuffer(paramsStr.substring(0,paramsStr.length()-1)).append("}");
				try {
					URLConnection uc;
					
					switch(method){
					case POST:
					    uc = new URL(url).openConnection();
					    uc.setConnectTimeout(5 * 1000);   // 单位是毫秒，设置超时时间为5秒
					    uc.setRequestProperty("Content-Type", "application/json");
					    uc.setRequestProperty("Accept-Charset", "utf-8");
					    uc.setRequestProperty("contentType", "utf-8");
					    
						uc.setDoOutput(true);  
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(),StringDefine.CHARSET));
						bw.write(paramsStr.toString());
						bw.flush();
						break;
						default:
							uc = new URL(url+"?"+paramsStr.toString()).openConnection();
							break;
					}
					
					System.out.println("Request url:" + uc.getURL());
					System.out.println("Request data:" + paramsStr);
					System.out.println(uc.getInputStream());
					BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(),StringDefine.CHARSET));
					
					String line = null;
					StringBuffer result = new StringBuffer();
					while((line = br.readLine()) != null){
						result.append(line);
					}
					System.out.println("result:" + result);
				
					br.close();
					return result.toString();
					
					
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block  
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}
			
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				
				if(result != null){
					if(successCallback != null){
						successCallback.onSuccess(result);
					}
				}else{
					if(failCallback != null){
						failCallback.onFail();
					}
				}
				
				super.onPostExecute(result);
			}
		}.execute();
	}
	
	public static interface SuccessCallback{
		void onSuccess(String result);
	}
	public static interface FailCallback{
		void onFail();
	}

}
