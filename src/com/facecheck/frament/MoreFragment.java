package com.facecheck.frament;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.facecheck.activity.GetFaceActivity;
import com.facecheck.config.R;

public class MoreFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View view = inflater.inflate(R.layout.frament_more, container, false);
		
		view.findViewById(R.id.tv_getFaceDate).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(view.getContext(),GetFaceActivity.class));
				
			}
		});
		
		return view;
	}

}
