package com.facecheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.facecheck.MyClass;
import com.facecheck.config.R;

public class St_MyClassAdapter extends ArrayAdapter<MyClass> {
	private int reId;

	public St_MyClassAdapter(Context context, int tvReId,List<MyClass> objects) {
		super(context, tvReId, objects);
		reId = tvReId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyClass mclass = getItem(position);// 实例化
		View view = LayoutInflater.from(getContext()).inflate(reId, null);

		TextView id = (TextView) view
				.findViewById(R.id.st_myclass_id);
		TextView name = (TextView) view.findViewById(R.id.st_myclass_name);
		TextView teacher = (TextView) view.findViewById(R.id.st_myclass_teacher);
		TextView timeLocation = (TextView) view.findViewById(R.id.st_myclass_timeLocation);

		id.setText(mclass.getId());
		name.setText(mclass.getName());
		if(mclass.getTimeLocation().equals("")){
			timeLocation.setText("地点未知");
		}
		else{
			timeLocation.setText(mclass.getTimeLocation());
		}
		if(mclass.getTeacherName().equals("")){
			teacher.setText("老师未知");
		}
		else{
			teacher.setText(mclass.getTeacherName());
		}
		return view;
	}
}
