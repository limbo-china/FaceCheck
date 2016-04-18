package com.facecheck.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.facecheck.config.R;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

public class CameraActivity extends Activity {
	SurfaceView sView;
	SurfaceHolder surfaceHolder;
	int screenWidth, screenHeight;
	// 定义系统所用的照相机
	Camera camera;
	// 是否在预览中
	boolean isPreview = false;
	private HttpRequests httpRequests = new HttpRequests(
			"c86213470260c2e3031ac0ea2e596028",
			"LhUOkbR_DBu3RBhg_IF08N_Xv-oWF5d5", true, true);
	private String result_am_here;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera);
		// 获取窗口管理器
		WindowManager wm = getWindowManager();
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		// 获取屏幕的宽和高
		display.getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		// 获取界面中SurfaceView组件
		sView = (SurfaceView) findViewById(R.id.sv_show);

		// 获得SurfaceView的SurfaceHolder
		surfaceHolder = sView.getHolder();
		// 为surfaceHolder添加一个回调监听器
		surfaceHolder.addCallback(new Callback() {
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// 打开摄像头
				initCamera();
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// 如果camera不为null ,释放摄像头
				if (camera != null) {
					if (isPreview)
					camera.stopPreview();
					camera.release();
					camera = null;
				}
			}
		});

//		findViewById(R.id.btn_return_to_iqclass).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				startActivity(new Intent(CameraActivity.this,IQClassActivity.class));
//				//finish();
//			}
//		});

		findViewById(R.id.btn_start).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				findViewById(R.id.btn_start).setVisibility(View.GONE);
				System.out.println("hh");
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						// 对group进行训练
						while (true) {
							// if (start_tag) {
							try {
								JSONObject result = httpRequests
										.trainIdentify(new PostParameters()
												.setGroupName("ustb_group"));
								System.out.println("trainIdentify---" + result);
								String identify_session_id = result
										.getString("session_id");
								String identify_status = null;
								do {
									result = httpRequests
											.infoGetSession(new PostParameters()
													.setSessionId(identify_session_id));
									System.out.println("infoGetSession---"
											+ result);
									identify_status = result
											.getString("status");
								} while (identify_status.equals("INQUEUE"));

								if (identify_status.equals("FAIL")) {
									// break;
									continue; // 该次训练失败，重新再来
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								CameraActivity.this
										.runOnUiThread(new Runnable() {
											public void run() {
												Toast.makeText(
														CameraActivity.this,
														"网络异常，请确认后重试",
														Toast.LENGTH_SHORT)
														.show();
											}
										});
								return;
							} catch (FaceppParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								CameraActivity.this
										.runOnUiThread(new Runnable() {
											public void run() {
												Toast.makeText(
														CameraActivity.this,
														"网络异常，请确认后重试",
														Toast.LENGTH_SHORT)
														.show();
											}
										});
								return;
							}
							// 训练成功
							while (true) {
								if (camera != null) {
									// 控制摄像头自动对焦后才拍照
									camera.autoFocus(new MyAutoFocusCallback()); // ④
								}
								try {
									Thread.sleep(6000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}).start();

			}
		});
	}

	private void initCamera() {
		if (!isPreview) {
			// 此处默认打开后置摄像头。
			// 通过传入参数可以打开前置摄像头
			camera = Camera.open(0); // ①/
			camera.setDisplayOrientation(90);
		}
		if (camera != null && !isPreview) {
			try {
				Camera.Parameters parameters = camera.getParameters();
				// 设置预览照片的大小
				parameters.setPreviewSize(screenWidth, screenHeight);
				// 设置预览照片时每秒显示多少帧的最小值和最大值
				parameters.setPreviewFpsRange(4, 10);
				// 设置图片格式
				parameters.setPictureFormat(ImageFormat.JPEG);
				// 设置JPG照片的质量
				parameters.set("jpeg-quality", 85);
				// 设置照片的大小
				parameters.setPictureSize(screenWidth, screenHeight);
				// 通过SurfaceView显示取景画面
				camera.setPreviewDisplay(surfaceHolder); // ②
				// 开始预览
				camera.startPreview(); // ③
			} catch (Exception e) {
				e.printStackTrace();
			}
			isPreview = true;
		}
	}

	private class MyAutoFocusCallback implements AutoFocusCallback {
		// 当自动对焦时激发该方法
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			System.out.println("success---->" + success);
			if (success) {
				// takePicture()方法需要传入3个监听器参数
				// 第1个监听器：当用户按下快门时激发该监听器
				// 第2个监听器：当相机获取原始照片时激发该监听器
				// 第3个监听器：当相机获取JPG照片时激发该监听器
				camera.takePicture(new ShutterCallback() {
					public void onShutter() {
						// 按下快门瞬间会执行此处代码
					}
				}, new PictureCallback() {
					public void onPictureTaken(byte[] data, Camera c) {
						// 此处代码可以决定是否需要保存原始照片信息
						System.out.println("picture--PictureCallback");
					}
				}, myJpegCallback); // ⑤
			}
		}
	};

	PictureCallback myJpegCallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// 根据拍照所得的数据创建位图
			Bitmap img = BitmapFactory.decodeByteArray(data, 0, data.length);
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			float scale = Math.min(1,
					Math.min(600f / img.getWidth(), 600f / img.getHeight()));
			Matrix matrix = new Matrix();
			//matrix.postScale(scale, scale);
			matrix.setRotate(90, scale, scale);
			Bitmap imgSmall = Bitmap.createBitmap(img, 0, 0, img.getWidth(),
					img.getHeight(), matrix, false);
			((ImageView)CameraActivity.this.findViewById(R.id.iv)).setImageBitmap(imgSmall);
			imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			final byte[] array = stream.toByteArray();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					result_am_here = null;
					try {
						JSONObject result_of_identify = null;
						result_of_identify = httpRequests
								.recognitionIdentify(new PostParameters()
										.setImg(array).setGroupName(
												"ustb_group"));
						System.out.println("recognitionIdentify---"
								+ result_of_identify);
						Log.i("hhh", "recognitionIdentify---"
								+ result_of_identify);
						
						
						JSONArray array_face = result_of_identify
								.getJSONArray("face");
						if (array_face.length() == 0) {
							CameraActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(CameraActivity.this,
											"该次拍照未识别到人脸", Toast.LENGTH_SHORT)
											.show();
								}
							});
							return;
						}
						result_am_here = "该次拍照共识别到" + array_face.length()
								+ "张人脸";
						for (int i = 0; i < array_face.length(); i++) {
							JSONObject face = array_face.getJSONObject(i);
							JSONArray candidate = face
									.getJSONArray("candidate");
							if (candidate.length() == 0) {
								result_am_here = result_am_here + "第" + (i + 1)
										+ "位同学无法识别其身份;";
								continue;
							}
							String confidence = candidate.getJSONObject(0)
									.getString("confidence");
							String person_name = candidate.getJSONObject(0)
									.getString("person_name");
							String person_tag = candidate.getJSONObject(0)
									.getString("tag");
							result_am_here = result_am_here + "第" + (i + 1)
									+ "位同学的信息为" + person_tag + ";";
							System.out.println("result_am_here-->"+result_am_here);
							
							Log.i("hhh", "result_am_here-->"+result_am_here);
						}
						CameraActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(CameraActivity.this,
										result_am_here, Toast.LENGTH_SHORT)
										.show();
							}
						});

					} catch (FaceppParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						CameraActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(CameraActivity.this,
										"网络异常，请确认后重试", Toast.LENGTH_SHORT)
										.show();
							}
						});
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}).start();

			// 重新浏览
			camera.stopPreview();
			camera.startPreview();
			isPreview = true;
		}
	};

	/**
	 * 返回摄取照片的文件名
	 * 
	 * @return 文件名
	 * */
	protected String getFileNmae() {
		// TODO Auto-generated method stub
		String fileName;
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "监测到你的手机没有插入SD卡，请插入SD卡后再试", Toast.LENGTH_LONG)
					.show();
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss",
				Locale.getDefault());
		fileName = Environment.getExternalStorageDirectory() + File.separator
				+ sdf.format(new Date()) + ".JPG";
		return fileName;
	}

}
