package com.facecheck;

import java.util.ArrayList;
import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;

public class StringDefine {

	public static final String SERVER_URL = "http://limbo-w.com/action.php"; // 鏈嶅姟鍣ㄥ煙鍚�

	public static final String APP_ID = "com.facecheck"; // APP鍖呭悕
	public static final String CHARSET = "utf-8"; // 缂栫爜鏂瑰紡

	public static final int PERMISSION_FAIL = 0; // 鏈嶅姟鍣ㄨ繑鍥炰俊鎭け璐�
	public static final int PERMISSION_SUCCESS = 1; // 成功
	public static final int PERMISSION_ERR = 2; // 甯愬彿瀵嗙爜閿欒
	public static final int RESULT_STATUS_SEMESTER_ERR = 3; // 鏁欏姟绯荤粺鏃犺瀛︽湡璇剧▼

	public static final String S_FACEAPIKEY="54745e6db7bfc6969746bbae727748c7";
	public static final String S_FACEAPISECRET="gg4_cGBkTtsa30EntegIKeU_Hqn6ish6";
	public static final String S_GROUPNAME ="class_1303";
	
	public static final String S_IDNUMBER = "idNumber";
	public static final String S_PASSWORD = "password";
	public static final String S_USER_IDENTITY = "userIdentity";
	private static final String KEY_IS_MEMORY = "isMemory";
	public static final String AC_TYPE = "ac_type";
	public static final String S_PERMISSION = "permission";
	public static final String S_SEMESTER = "semester";
	public static final String S_CLASSES = "classes";
	public static final String S_CLASSID = "id";
	public static final String S_CLASSNAME = "name";
	public static final String KEY_CLASS_TYPE = "class_type";
	public static final String KEY_CLASS_PERIOD = "class_period";
	public static final String KEY_CLASS_CREDIT = "class_credit";
	public static final String S_CLASSTIMELOCATION = "timeLocation";
	public static final String S_CLASSTEACHERNAME = "teacherName";
	public static final String KEY_CLASS_SEMESTER = "class_semester";
	
	public static final String KEY_USER_NAME = "user_name";
	public static final String KEY_USER_GENDER = "user_gender";
	public static final String KEY_USER_GRADE = "user_grade";
	public static final String KEY_USER_PROFESSION = "user_profession";
	public static final String KEY_USER_CLASS = "user_class";
	
	public static final String KEY_API_KEY = "api_key";
	public static final String KEY_API_SECRET = "api_secret";

	public static final String IDENTITY_UNDERGRADUATE = "undergraduate";
	public static final String IDENTITY_TEACHER = "teacher";

	public static final String AC_LOGIN = "login";
	public static final String AC_GETCLASS = "getclass";
	public static final String AC_CHECK = "check";
	public static final String AC_ADDCLASS= "addclass";
	public static final String AC_ADDRecord ="addrecord";
	public static final String AC_MYCLASS="myclass";

	public static final String ACTION_RESIGTER = "resigter";
	public static final String ACTION_GET_MESSAGE = "getMessage";
	public static final String ACTION_ADD_LESSON = "addLesson";

	

	public static String getCachedUserId(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.getString(S_IDNUMBER, null);
	}

	public static String getCachedPassword(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.getString(S_PASSWORD, null);
	}

	public static String getCachedUserType(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.getString(S_USER_IDENTITY, null);
	}

	public static int getCachedIsMemory(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.getInt(KEY_IS_MEMORY, 0);
	}

	public static String getCachedSemesterChoice(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.getString(S_SEMESTER, "2015-2016-2");
	}
	public static String getCachedUserName(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.getString(KEY_USER_NAME, null);
	}
	public static String getCachedUserClass(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.getString(KEY_USER_CLASS,null);
	}
	public static String getCachedUserProfession(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.getString(KEY_USER_PROFESSION,null);
	}
	public static String getCachedUserGrade(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.getString(KEY_USER_GRADE, null);
	}
	public static String getCachedUserGender(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.getString(KEY_USER_GENDER, null);
	}
	
	
	public static void cacheUserInfo(Context context, String userId,
			String password, String userType, int isMemory) {
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.edit();
		e.putString(StringDefine.S_IDNUMBER, userId);
		e.putString(StringDefine.S_PASSWORD, password);
		e.putString(StringDefine.S_USER_IDENTITY, userType);
		e.putInt(KEY_IS_MEMORY, isMemory);
		e.commit();
	}
	
	public static void cacheSemesterChoice(Context context,String semester){
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.edit();
		e.putString(StringDefine.S_SEMESTER, semester);
		e.commit();
	}
	
	public static void cachedUserMoreInfo(Context context,String user_name,String user_gender,
			String user_grade,String user_profession,String user_class){
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.edit();
		e.putString(StringDefine.KEY_USER_NAME, user_name);
		e.putString(StringDefine.KEY_USER_GENDER, user_gender);
		e.putString(StringDefine.KEY_USER_GRADE, user_grade);
		e.putString(StringDefine.KEY_USER_PROFESSION, user_profession);
		e.putString(StringDefine.KEY_USER_CLASS, user_class);
		e.commit();
	}
	
	


	public static String getClassTime(String segemenceth) {
		char segemence = segemenceth.charAt(1);
		String time = null;
		switch (segemence) {
		case '1':
			time = "8:00-9:35";
			break;
		case '2':
			time = "9:55-11:30";
			break;
		case '3':
			time = "13:30-15:05";
			break;
		case '4':
			time = "15:20-16:55";
			break;
		case '5':
			time = "17:10-18:45";
			break;
		default:
			time = "19:30-21:05";
			break;
		}
		return time;
	}
	public static ArrayList<ArrayList<String>> parseClassTimeLocation(String class_time_location){
		ArrayList<String> lesson = new ArrayList<String>();
		ArrayList<String> operate = new ArrayList<String>();
		ArrayList<ArrayList<String>> arrayList = new ArrayList<ArrayList<String>>();
		//System.out.println(class_time_location+"-----");
		if(class_time_location.equals("+")){
			
			return null;
		}
		if(class_time_location.indexOf("+") == class_time_location.length()-1){
			StringBuffer buffer = new StringBuffer(class_time_location);
			do{
				String delBuffer = buffer.substring(buffer.indexOf("(鍛�")+1, buffer.indexOf(") "));
				System.out.println(delBuffer);
				lesson.add(delBuffer);
				buffer = buffer.delete(buffer.indexOf("(鍛�"), buffer.indexOf(") ")+1);
				//System.out.println("buffer====>"+buffer);
			}while(buffer.indexOf("(")>=0);
			arrayList.add(lesson);
			//System.out.println(class_time_location+"-----"+lesson+"---"+operate);
			return arrayList;
		}
		
		String[] lesson_operation = class_time_location.split("\\+");
		
		StringBuffer lessBuffer = new StringBuffer(lesson_operation[0]);
		do{
			String delBuffer = lessBuffer.substring(lessBuffer.indexOf("(鍛�")+1, lessBuffer.indexOf(") "));
			lesson.add(delBuffer);
			lessBuffer = lessBuffer.delete(lessBuffer.indexOf("(鍛�"), lessBuffer.indexOf(") ")+1);
			//System.out.println(delBuffer+"----"+lessBuffer.length()+"----"+lessBuffer+lesson);
		}while(lessBuffer.indexOf("(")>=0);
		
		StringBuffer operBuffer = new StringBuffer(lesson_operation[1]);
		do{
			String delBuffer = operBuffer.substring(operBuffer.indexOf("(鍛�")+1, operBuffer.indexOf(") "));
			operate.add(delBuffer);
			operBuffer = operBuffer.delete(operBuffer.indexOf("(鍛�"), operBuffer.indexOf(") ")+1);
		}while(operBuffer.indexOf("(")>=0);
		//System.out.println(class_time_location+"-----"+lesson+"---"+operate);
		arrayList.add(lesson);
		arrayList.add(operate);
		return arrayList;
	}
}
