package com.facecheck;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class StringDefine {

	public static final String SERVER_URL = "http://limbo-w.com/action.php";
	public static final String APP_ID = "com.facecheck";
	public static final String CHARSET = "utf-8"; 
	public static final int PERMISSION_FAIL = 0;
	public static final int PERMISSION_SUCCESS = 1; 
	public static final int PERMISSION_ERR = 2; 
	public static final int RESULT_STATUS_SEMESTER_ERR=3;
	public static final int PERMISSION_TIMEERR =4;
	public static final String S_FACEAPIKEY="54745e6db7bfc6969746bbae727748c7";
	public static final String S_FACEAPISECRET="gg4_cGBkTtsa30EntegIKeU_Hqn6ish6";
	public static final String S_GROUPNAME ="class_1303";
	public static final String S_IDNUMBER = "idNumber";
	public static final String S_PASSWORD = "password";
	public static final String S_USER_IDENTITY = "userIdentity";
	private static final String IS_MEMORY = "isMemory";
	public static final String AC_TYPE = "ac_type";
	public static final String S_PERMISSION = "permission";
	public static final String S_SEMESTER = "semester";
	public static final String S_CLASSES = "classes";
	public static final String S_CLASSID = "id";
	public static final String S_CLASSNAME = "name";
	public static final String S_CLASSTIMELOCATION = "timeLocation";
	public static final String S_CLASSTEACHERNAME = "teacherName";
	public static final String S_CLASSTIME="classTime";

	public static final String IDENTITY_UNDERGRADUATE = "undergraduate";
	public static final String IDENTITY_TEACHER = "teacher";

	public static final String AC_LOGIN = "login";
	public static final String AC_GETCLASS = "getclass";
	public static final String AC_CHECK = "check";
	public static final String AC_ADDCLASS= "addclass";
	public static final String AC_ADDRecord ="addrecord";
	public static final String AC_MYCLASS="myclass";
	public static final String AC_LOOKCHECK="lookcheck";
	public static final String AC_MYINFO="myinfo";

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
				.getInt(IS_MEMORY, 0);
	}

	public static String getCachedSemesterChoice(Context context){
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.getString(S_SEMESTER, "2015-2016-2");
	}
	
	
	public static void cacheUserInfo(Context context, String userId,
			String password, String userType, int isMemory) {
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.edit();
		e.putString(StringDefine.S_IDNUMBER, userId);
		e.putString(StringDefine.S_PASSWORD, password);
		e.putString(StringDefine.S_USER_IDENTITY, userType);
		e.putInt(IS_MEMORY, isMemory);
		e.commit();
	}
	
	public static void cacheSemesterChoice(Context context,String semester){
		Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE)
				.edit();
		e.putString(StringDefine.S_SEMESTER, semester);
		e.commit();
	}
}
