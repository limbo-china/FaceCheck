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

public class MyClassAdapter extends ArrayAdapter<MyClass> {
	private int reId;

	public MyClassAdapter(Context context, int tvReId,List<MyClass> objects) {
		super(context, tvReId, objects);
		reId = tvReId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyClass mclass = getItem(position);// 实例化
		View view = LayoutInflater.from(getContext()).inflate(reId, null);

		TextView id = (TextView) view
				.findViewById(R.id.myclass_id);
		TextView name = (TextView) view.findViewById(R.id.myclass_name);
		TextView timeLocation = (TextView) view.findViewById(R.id.myclass_timeLocation);

		id.setText(mclass.getId());
		name.setText(mclass.getName());
		timeLocation.setText(mclass.getTimeLocation());
		return view;
	}
}
