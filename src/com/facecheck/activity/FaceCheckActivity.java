package com.facecheck.activity;

import java.io.ByteArrayOutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facecheck.StringDefine;
import com.facecheck.config.R;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facecheck.net.Check;

public class FaceCheckActivity extends Activity {
	
	final private static String TAG = "FaceCheckActivity";
	
	private Context mContext;
	private EditText classId = null;
	private final int CAPTURE_IMAGE =100;
	private ImageView imageView = null;
	private Bitmap imgbit = null;
	private String classinfo=null;
	private String loginfo =null;
	private JSONObject faceJson =null;
	private String pidNumber = null; 
	
	///////////////////////facedetect 类应该新建一个类文件 ，写在外面
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facecheck);
		
		mContext = this;
		classId = (EditText) findViewById(R.id.classId);
		imageView = (ImageView)this.findViewById(R.id.imageView2);
		
		findViewById(R.id.butCheck).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(classId.getText())){
					Toast.makeText(FaceCheckActivity.this,"课程编号不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				final ProgressDialog dialog = ProgressDialog.show(FaceCheckActivity.this,"连接中","连接服务器中,请稍候");
				
				System.out.println("classId:" + classId.getText().toString());
				
				new Check(classId.getText().toString(),new Check.SuccessCallback() {
					
					@Override
					public void onSuccess(JSONObject res) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						try {
							classinfo = "存在此课程\n"+"课程名:"
									+res.getString(StringDefine.S_CLASSNAME)+"\n"+"老师:"+res.getString(StringDefine.S_CLASSTEACHERNAME)+"\n";
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Toast.makeText(FaceCheckActivity.this,classinfo, Toast.LENGTH_SHORT).show();
						Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
				        startActivityForResult(photoPickerIntent, CAPTURE_IMAGE);
						//StringDefine.cacheUserInfo(mContext, etUserId.getText().toString(), etPassword.getText().toString(), userIdentity,isMemory);
						System.out.println("success");
						
						
//						Intent intent;
//						if(userIdentity==StringDefine.IDENTITY_TEACHER)
//							intent = new Intent(LoginActivity.this,Te_NaviActivity.class);
//						else 
//							intent = new Intent(LoginActivity.this,St_NaviActivity.class);
					//	startActivity(intent);
					//	LoginActivity.this.finish();
					}
				}, new Check.FailCallback() {
					
					@Override
					public void onFail(int errorStatus) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						switch (errorStatus) {
						case StringDefine.PERMISSION_FAIL:
							Toast.makeText(FaceCheckActivity.this,"无法链接服务器,检查您的网络", Toast.LENGTH_LONG).show();
							break;
						case StringDefine.PERMISSION_ERR:
							Toast.makeText(FaceCheckActivity.this,"不存在此课程", Toast.LENGTH_LONG).show();
							break;
						}
					}
				});
			}
		});
	}
	
	 protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    	super.onActivityResult(requestCode, resultCode, intent);
	    	
	    	//the image picker callback
	    	if (requestCode == CAPTURE_IMAGE) {
	    		if (intent != null) {
	    			Bundle bundle = intent.getExtras();
	    			imgbit = (Bitmap) bundle.get("data");//获取照片，转换为bit
	    			imageView.setImageBitmap(imgbit);
	    			FaceppDetect faceppDetect = new FaceppDetect();
					faceppDetect.setDetectCallback(new DetectCallback() {
						
						public void detectResult(JSONObject res) {
							//Log.v(TAG, rst.toString());
							//use the green paint
							faceJson =res;
							Paint paint = new Paint();
							paint.setColor(Color.GREEN);
							paint.setStrokeWidth(Math.max(imgbit.getWidth(), imgbit.getHeight()) / 100f);

							//create a new canvas
							Bitmap bitmap = Bitmap.createBitmap(imgbit.getWidth(), imgbit.getHeight(), imgbit.getConfig());
							Canvas canvas = new Canvas(bitmap);
							canvas.drawBitmap(imgbit, new Matrix(), null);
							
							
							try {
								//find out all faces
								final int count = res.getJSONArray("face").length();
								for (int i = 0; i < count; ++i) {
									float x, y, w, h;
									//get the center point
									x = (float)res.getJSONArray("face").getJSONObject(i)
											.getJSONObject("position").getJSONObject("center").getDouble("x");
									y = (float)res.getJSONArray("face").getJSONObject(i)
											.getJSONObject("position").getJSONObject("center").getDouble("y");

									//get face size
									w = (float)res.getJSONArray("face").getJSONObject(i)
											.getJSONObject("position").getDouble("width");
									h = (float)res.getJSONArray("face").getJSONObject(i)
											.getJSONObject("position").getDouble("height");
									
									//change percent value to the real size
									x = x / 100 * imgbit.getWidth();
									w = w / 100 * imgbit.getWidth() * 0.7f;
									y = y / 100 * imgbit.getHeight();
									h = h / 100 * imgbit.getHeight() * 0.7f;

									//draw the box to mark it out
									canvas.drawLine(x - w, y - h, x - w, y + h, paint);
									canvas.drawLine(x - w, y - h, x + w, y - h, paint);
									canvas.drawLine(x + w, y + h, x - w, y + h, paint);
									canvas.drawLine(x + w, y + h, x + w, y - h, paint);
								}
								
								//save new image
								imgbit = bitmap;
								
								new Thread(new Runnable() {
									
									public void run() {
										//show the image
										Message message0 = new Message(); 
										message0.what = 0;  
								        mHandler.sendMessage(message0); 
										if(count==1){ //为1时直接在人脸中识别
											Message message1 = new Message(); 
											message1.what = 1;  
									        mHandler.sendMessage(message1); 
											HttpRequests httpRequests = new HttpRequests(StringDefine.S_FACEAPIKEY, StringDefine.S_FACEAPISECRET, true, false);
											try {
												JSONObject syncRet = httpRequests.trainIdentify(new PostParameters().setGroupName(StringDefine.S_GROUPNAME));
												JSONObject res= httpRequests.getSessionSync(syncRet.getString("session_id"));
												System.out.println(res.getString("status"));
												if(res.getString("status").equals("SUCC")){
													Message message2 = new Message(); 
													message2.what = 2;  
											        mHandler.sendMessage(message2); 
													res =httpRequests.recognitionIdentify(
															new PostParameters().setGroupName(StringDefine.S_GROUPNAME)
															.setKeyFaceId(faceJson.getJSONArray("face").getJSONObject(0).getString("face_id")));
													JSONObject best;
													JSONArray resArray;
													resArray = res.getJSONArray("face");
													if (resArray.length() == 0) {		
															Message message3 = new Message(); 
															message3.what = 3;  
													        mHandler.sendMessage(message3); 
													} else {
														best = resArray
																.getJSONObject(0).getJSONArray("candidate")
																.getJSONObject(0);

														if (best.getDouble("confidence") < 50) {
															Message message4 = new Message(); 
															message4.what = 4;  
													        mHandler.sendMessage(message4); 
														} else {
															pidNumber = best
																	.getString("person_name");
															Message message5 = new Message(); 
															message5.what = 5;  
													        mHandler.sendMessage(message5); 
														}
													}

												} 
												else{
													Message message6 = new Message(); 
													message6.what = 6;  
											        mHandler.sendMessage(message6); 
												}
											}catch (FaceppParseException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												} catch (JSONException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}

										
										}
										else if(count==0) {
											Message message7 = new Message(); 
											message7.what = 7;  
									        mHandler.sendMessage(message7);
										}
										else{
											Message message8 = new Message(); 
											message8.what = 8;  
									        mHandler.sendMessage(message8);
										}
									}
								}).start();
								
							} catch (JSONException e) {
								e.printStackTrace();
								FaceCheckActivity.this.runOnUiThread(new Runnable() {
									public void run() {
										loginfo="Error.";
										Toast.makeText(FaceCheckActivity.this,loginfo, Toast.LENGTH_SHORT).show();
									}
								});
							}
							
						}
					});
					faceppDetect.detect(imgbit);
					
					
	    		}
	    		else {
	    			Log.d(TAG, "idButSelPic Photopicker canceled");
	    		}
	    	}
	    }
	 private class FaceppDetect {
	    	DetectCallback callback = null;
	    	
	    	public void setDetectCallback(DetectCallback detectCallback) { 
	    		callback = detectCallback;
	    	}

	    	public void detect(final Bitmap image) {
	    		
	    		new Thread(new Runnable() {
					
					public void run() {
						HttpRequests httpRequests = new HttpRequests(StringDefine.S_FACEAPIKEY, StringDefine.S_FACEAPISECRET, true, false);
			    		//Log.v(TAG, "image size : " + imgbit.getWidth() + " " + imgbit.getHeight());
			    		
			    		ByteArrayOutputStream stream = new ByteArrayOutputStream();
			    		float scale = Math.min(1, Math.min(600f / imgbit.getWidth(), 600f / imgbit.getHeight()));
			    		Matrix matrix = new Matrix();
			    		matrix.postScale(scale, scale);

			    		Bitmap imgSmall = Bitmap.createBitmap(imgbit, 0, 0, imgbit.getWidth(), imgbit.getHeight(), matrix, false);
			    		//Log.v(TAG, "imgSmall size : " + imgSmall.getWidth() + " " + imgSmall.getHeight());
			    		
			    		imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			    		byte[] array = stream.toByteArray();
			    		
			    		try {
			    			//detect
							JSONObject result = httpRequests.detectionDetect(new PostParameters().setImg(array));
							//finished , then call the callback function
							if (callback != null) {
								callback.detectResult(result);
							}
						} catch (FaceppParseException e) {
							e.printStackTrace();
							FaceCheckActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									loginfo="Network error.";
								}
							});
						}
						
					}
				}).start();
	    	}
	    }

	    interface DetectCallback {
	    	void detectResult(JSONObject rst);
		}
	    
	    private Handler mHandler = new Handler(){  
	        
	        public void handleMessage(Message msg) {  
	            switch (msg.what) {  
	            case 0:
	            	imageView.setImageBitmap(imgbit);
	            	break;
	            case 1:  
	            	loginfo="find 1 face.Please upload.";
					Toast.makeText(FaceCheckActivity.this,loginfo, Toast.LENGTH_SHORT).show();
	            	break;
	            case 2:
	            	loginfo="训练成功，开始识别";
					Toast.makeText(FaceCheckActivity.this,loginfo, Toast.LENGTH_SHORT).show();
	                break;  
	            case 3:
	            	Toast.makeText(FaceCheckActivity.this,
							"识别失败，请重新识别", Toast.LENGTH_SHORT)
							.show();
	            	break;
	            case 4:
	            	Toast.makeText(FaceCheckActivity.this,
							"无相似人脸", Toast.LENGTH_SHORT).show();
	            	break;
	            case 5:
	            	Toast.makeText(FaceCheckActivity.this,
							"识别成功:" + pidNumber,
							Toast.LENGTH_SHORT).show();
	            	break;
	            case 6:
	            	loginfo="训练失败";
					Toast.makeText(FaceCheckActivity.this,loginfo, Toast.LENGTH_SHORT).show();
	            	break;
	            case 7:
	            	loginfo="cannot find face, get image again.";
					Toast.makeText(FaceCheckActivity.this,loginfo, Toast.LENGTH_SHORT).show();
					break;
	            case 8:
	            	loginfo= "find more than one face, get image again.";
					Toast.makeText(FaceCheckActivity.this,loginfo, Toast.LENGTH_SHORT).show();
					break;
	            }  
	        };  
	    };  
}
