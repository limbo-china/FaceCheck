package com.facecheck.activity;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.facecheck.net.AddRecord;
import com.facecheck.net.Check;

public class Te_FaceCheckActivity extends Activity {
	
	final private static String TAG = "Te_FaceCheckActivity";
	
	private EditText classId = null;
	private EditText classTime=null;
	private final int CAPTURE_IMAGE =100;
	private ImageView imageView = null;
	private Bitmap imgbit = null;
	private String classinfo=null;
	private String loginfo =null;
	private JSONObject faceJson =null;
	private String pidNumber = null; 
	private String s_classId=null;
	private String s_classTime=null;
	private Context mContext = null;
	private String name =null;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.te_facecheck);
		 
		mContext=this;
		classId = (EditText) findViewById(R.id.classId);
		classTime=(EditText)findViewById(R.id.classTime);
		imageView = (ImageView)this.findViewById(R.id.imageView2);
		
		
		findViewById(R.id.butCheck).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				s_classId =classId.getText().toString();
				s_classTime =classTime.getText().toString();
				if(TextUtils.isEmpty(classId.getText())){
					Toast.makeText(Te_FaceCheckActivity.this,"课程编号不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				if(TextUtils.isEmpty(classTime.getText())){
					Toast.makeText(Te_FaceCheckActivity.this,"考勤次数不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				Pattern p = Pattern.compile("[0-9]*"); 
			    Matcher m = p.matcher(classTime.getText().toString()); 
			    if(!m.matches() ){
			    	 Toast.makeText(Te_FaceCheckActivity.this,"考勤次数必须为大于0的整数", Toast.LENGTH_LONG).show();
						return;
			     } 
				if( Integer.parseInt(classTime.getText().toString())<=0){
					Toast.makeText(Te_FaceCheckActivity.this,"考勤次数必须为大于0的整数", Toast.LENGTH_LONG).show();
					return;
				}
				final ProgressDialog dialog = ProgressDialog.show(Te_FaceCheckActivity.this,"连接中","连接服务器中,请稍候");
				
				System.out.println("classId:" + s_classId);
				
				new Check(StringDefine.getCachedUserId(mContext),s_classId,s_classTime,new Check.SuccessCallback() {
					
					@Override
					public void onSuccess(JSONObject res) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						try {							classinfo = "存在此课程\n"+"课程名:"
									+res.getString(StringDefine.S_CLASSNAME)+"\n"+"老师:"+res.getString(StringDefine.S_CLASSTEACHERNAME)+"\n";
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Toast.makeText(Te_FaceCheckActivity.this,classinfo, Toast.LENGTH_SHORT).show();
						Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
				        startActivityForResult(photoPickerIntent, CAPTURE_IMAGE);
					}
				}, new Check.FailCallback() {
					
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
							Toast.makeText(Te_FaceCheckActivity.this,"无法链接服务器,检查您的网络...", Toast.LENGTH_LONG).show();
							break;
						case StringDefine.PERMISSION_ERR:
							Toast.makeText(Te_FaceCheckActivity.this,"你的课程中没有此课程,请先加入课程", Toast.LENGTH_LONG).show();
							break;
						case StringDefine.PERMISSION_TIMEERR:
							Toast.makeText(Te_FaceCheckActivity.this,"此课程只进行了"+classTime+"次考勤", Toast.LENGTH_LONG).show();
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
	    			s_classId =classId.getText().toString();
	    			s_classTime=classTime.getText().toString();
	    			Bundle bundle = intent.getExtras();
	    			imgbit = (Bitmap) bundle.get("data");//获取照片，转换为bit
	    			imageView.setImageBitmap(imgbit);
	    			
	    			Toast.makeText(Te_FaceCheckActivity.this,"正在识别人脸，请稍候...", Toast.LENGTH_LONG).show();
	            	Te_FaceCheckActivity.this.findViewById(R.id.butCheck).setEnabled(false);
	            	
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
								Message message0 = new Message(); 
								message0.what = 0;  
						        mHandler.sendMessage(message0); 
						        
						        
						        
								new Thread(new Runnable() {
									
									public void run() {
										//show the image
										
										if(count==1){ //为1时直接在人脸中识别
											Message message1 = new Message(); 
											message1.what = 1;  
									        mHandler.sendMessage(message1); 
											HttpRequests httpRequests = new HttpRequests(StringDefine.S_FACEAPIKEY, StringDefine.S_FACEAPISECRET, true, false);
											try {
												JSONObject res =httpRequests.recognitionIdentify(
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
															new AddRecord(pidNumber,s_classId,s_classTime,new AddRecord.SuccessCallback() {
																
																@Override
																public void onSuccess(JSONObject res) {
																	// TODO Auto-generated method stub
																	try {
																		name =res.getString("name");
																	} catch (JSONException e) {
																		// TODO Auto-generated catch block
																		e.printStackTrace();
																	}
																	Message message5 = new Message(); 
																	message5.what = 5;  
															        mHandler.sendMessage(message5);
																}
															}, new AddRecord.FailCallback() {
																
																@Override
																public void onFail(int errorStatus) {
																	// TODO Auto-generated method stub
																	switch (errorStatus) {
																	case StringDefine.PERMISSION_FAIL:
																		Message message9 = new Message(); 
																		message9.what = 9;  
																        mHandler.sendMessage(message9); 
																		break;
																	case StringDefine.PERMISSION_ERR:
																		Message message10 = new Message(); 
																		message10.what = 10;  
																        mHandler.sendMessage(message10); 
																		break;
																	}
																}
															});
													        
														}
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
								Te_FaceCheckActivity.this.runOnUiThread(new Runnable() {
									public void run() {
										loginfo="Error.";
										Toast.makeText(Te_FaceCheckActivity.this,loginfo, Toast.LENGTH_SHORT).show();
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
							Te_FaceCheckActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									loginfo="网络错误.";
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
	            	loginfo="图中找到  1 张脸.开始识别,请等待签到...";
					Toast.makeText(Te_FaceCheckActivity.this,loginfo, Toast.LENGTH_SHORT).show();
	            	break;
	            case 3:
	            	Toast.makeText(Te_FaceCheckActivity.this,
							"识别失败，请重新拍照", Toast.LENGTH_SHORT)
							.show();
	            	Te_FaceCheckActivity.this.findViewById(R.id.butCheck).setEnabled(true);
	            	break;
	            case 4:
	            	Toast.makeText(Te_FaceCheckActivity.this,
							"无相似人脸,请重新拍照", Toast.LENGTH_SHORT).show();
	            	Te_FaceCheckActivity.this.findViewById(R.id.butCheck).setEnabled(true);
	            	break;
	            case 5:
	            	Toast.makeText(Te_FaceCheckActivity.this,
							"识别成功:" + pidNumber+","+name+",签到成功",
							Toast.LENGTH_SHORT).show();
	            	Te_FaceCheckActivity.this.findViewById(R.id.butCheck).setEnabled(true);
	            	break;
	            case 7:
	            	loginfo="图中找不到脸，请重新拍照.";
					Toast.makeText(Te_FaceCheckActivity.this,loginfo, Toast.LENGTH_SHORT).show();
					Te_FaceCheckActivity.this.findViewById(R.id.butCheck).setEnabled(true);
					break;
	            case 8:
	            	loginfo= "图中找到多张脸，请重新拍照.";
					Toast.makeText(Te_FaceCheckActivity.this,loginfo, Toast.LENGTH_SHORT).show();
					Te_FaceCheckActivity.this.findViewById(R.id.butCheck).setEnabled(true);
					break;
	            case 9:
//	            	Toast.makeText(Te_FaceCheckActivity.this,
//							"识别成功:" + pidNumber+",未签到成功，请检查网络",
//							Toast.LENGTH_SHORT).show();
	            	Te_FaceCheckActivity.this.findViewById(R.id.butCheck).setEnabled(true);
	            	break;
	            case 10:
	            	Toast.makeText(Te_FaceCheckActivity.this,
							"识别成功:" + pidNumber+",不能重复签到",
							Toast.LENGTH_SHORT).show();
	            	Te_FaceCheckActivity.this.findViewById(R.id.butCheck).setEnabled(true);
	            	break;
	            }  
	        };  
	    };  
}
