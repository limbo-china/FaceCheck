package com.facecheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.facecheck.StuInfo;
import com.facecheck.config.R;

public class StuInfoAdapter extends ArrayAdapter<StuInfo> {
	private int reId;

	public StuInfoAdapter(Context context, int tvReId,List<StuInfo> objects) {
		super(context, tvReId, objects);
		reId = tvReId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StuInfo stuinfo = getItem(position);// 实例化
		View view = LayoutInflater.from(getContext()).inflate(reId, null);

		TextView idNumber = (TextView) view
				.findViewById(R.id.stuinfo_idNumber);
		TextView name = (TextView) view.findViewById(R.id.stuinfo_name);
		TextView stuClass = (TextView) view.findViewById(R.id.stuinfo_stuclass);
		TextView field = (TextView) view.findViewById(R.id.stuinfo_field);

		idNumber.setText(stuinfo.getIdNumber());
		name.setText(stuinfo.getName());
		stuClass.setText(stuinfo.getStuClass());
		field.setText(stuinfo.getField());
		return view;
	}
}
