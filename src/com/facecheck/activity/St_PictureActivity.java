package com.facecheck.activity;

import java.io.ByteArrayOutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facecheck.StringDefine;
import com.facecheck.config.R;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

public class St_PictureActivity extends Activity {

	final private static String TAG = "St_PictureActivity";
	
	private ImageView imageView = null;
	private Bitmap imgbit = null;
	private Button buttonGetimg =null;
	private Button buttonUploadimg=null;
	private TextView textView = null;
	private final int CAPTURE_IMAGE =100;
	private JSONObject faceJson =null;
	private String userIdNumber ;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st_picture);
        userIdNumber = StringDefine.getCachedUserId(this);
        buttonGetimg = (Button)this.findViewById(R.id.butgetimg);
        buttonGetimg.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
		        startActivityForResult(photoPickerIntent, CAPTURE_IMAGE);
			}
		});
        
        textView = (TextView)this.findViewById(R.id.textView1);
        
        buttonUploadimg = (Button)this.findViewById(R.id.butuploadimg);
        buttonUploadimg.setVisibility(View.INVISIBLE);
        
        imageView = (ImageView)this.findViewById(R.id.imageView1);
        imageView.setImageBitmap(imgbit);
        
        buttonUploadimg.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				buttonUploadimg.setVisibility(View.INVISIBLE);
				new Thread(new Runnable() {
						public void run() {
							//replace api_key and api_secret here (note)
							JSONArray jsonarr = null; 
							String faceId =null;
							try {
								faceId = faceJson.getJSONArray("face").getJSONObject(0).getString("face_id");
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							boolean exist_group = false;
							boolean exist_person = false;
							HttpRequests httpRequests = new HttpRequests(StringDefine.S_FACEAPIKEY, StringDefine.S_FACEAPISECRET, true, true);
							try {
								jsonarr =httpRequests.infoGetGroupList().getJSONArray("group");
								for(int i=0;i<jsonarr.length();i++){
									if(jsonarr.getJSONObject(i).getString("group_name").equals(StringDefine.S_GROUPNAME)){
										exist_group = true;
										break;
									}
								}
								if(exist_group==false){
									httpRequests.groupCreate(new PostParameters().setGroupName(StringDefine.S_GROUPNAME));
								}
								
								jsonarr= httpRequests.infoGetPersonList(new PostParameters().setGroupName(StringDefine.S_GROUPNAME)).getJSONArray("person");
								for(int i=0;i<jsonarr.length();i++){
									if(jsonarr.getJSONObject(i).getString("person_name").
											equals(userIdNumber)){
										exist_person = true;
										break;
									}
								}
								if(exist_person==false){  
									httpRequests.personCreate(new PostParameters().setPersonName(userIdNumber));
									httpRequests.groupAddPerson(new PostParameters().
											setGroupName(StringDefine.S_GROUPNAME).setPersonName(userIdNumber));
									httpRequests.personAddFace(new PostParameters().setPersonName(userIdNumber).setFaceId(faceId));
								}
								else{
									httpRequests.personAddFace(new PostParameters().setPersonName(userIdNumber).setFaceId(faceId));
								}
								Message message = new Message();  
					            message.what = 1;  
					            mHandler.sendMessage(message); 
					            httpRequests.trainIdentify(new PostParameters().setGroupName(StringDefine.S_GROUPNAME));
								
							}catch (Exception e) {
								Message message = new Message();  
					            message.what = 2;  
					            mHandler.sendMessage(message); 
								e.printStackTrace();
							} 
						}
				 }).start();
			}
		});
    }
    private Handler mHandler = new Handler(){  
        
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case 1:  
            	Toast.makeText(St_PictureActivity.this,
						"上传图片成功", Toast.LENGTH_LONG).show();
            	textView.setText("上传成功.");
            	break;
            case 2:
            	Toast.makeText(St_PictureActivity.this,
						"上传图片失败，请重新拍照", Toast.LENGTH_LONG).show();
            	textView.setText("上传失败.");
                break;  
            }  
        };  
    };  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	
    	//the image picker callback
    	if (requestCode == CAPTURE_IMAGE) {
    		if (intent != null) {
    			Bundle bundle = intent.getExtras();
    			imgbit = (Bitmap) bundle.get("data");//获取照片，转换为bit
    			textView.setText("请识别人脸");
    			imageView.setImageBitmap(imgbit);
    			
    			textView.setText("连接中 ...");
				
				FaceppDetect faceppDetect = new FaceppDetect();
				faceppDetect.setDetectCallback(new DetectCallback() {
					
					public void detectResult(JSONObject res) {
						//Log.v(TAG, rst.toString());
						faceJson =res;
						//use the green paint
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

							St_PictureActivity.this.runOnUiThread(new Runnable() {
								
								public void run() {
									//show the image
									imageView.setImageBitmap(imgbit);
									if(count==1){
										buttonUploadimg.setVisibility(View.VISIBLE);
										textView.setText("识别出 "+count + "张脸"+"请上传.");
									}
									else if(count==0) {
										textView.setText("不能识别出人脸 ，请重新拍照.");
									}
									else{
										textView.setText("识别出多张脸，请重新拍照.");
									}
								}
							});
							
						} catch (JSONException e) {
							e.printStackTrace();
							St_PictureActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									textView.setText("错误.");
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
						St_PictureActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								textView.setText("网络错误.");
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
}
