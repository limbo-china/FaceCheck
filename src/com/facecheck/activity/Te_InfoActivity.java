package com.facecheck.activity;
 
import java.util.ArrayList;

import com.facecheck.config.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
 
public class Te_InfoActivity extends Activity {
    /** Called when the activity is first created. */
    private ListView mylistview;
    private ArrayList<String> list = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.te_info);
        mylistview = (ListView)findViewById(R.id.listView1);
        list.add("我的课程");
        list.add("加入课程");
        list.add("考勤查询");
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>
                           (this,android.R.layout.simple_list_item_1,list);
        mylistview.setAdapter(myArrayAdapter);
        
        mylistview.setOnItemClickListener(new OnItemClickListener(){
 
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
                if(list.get(arg2).equals("我的课程"))
                {
                    Intent intent = new Intent(Te_InfoActivity.this,Te_MyClassActivity.class);
                    startActivity(intent);
                }
                if(list.get(arg2).equals("加入课程"))
                {
                	Intent intent = new Intent(Te_InfoActivity.this,Te_AddClassActivity.class);
                    startActivity(intent);
                }
                if(list.get(arg2).equals("考勤查询"))
                {
                	Intent intent = new Intent(Te_InfoActivity.this,Te_LookCheckActivity.class);
                    startActivity(intent);
                }
            }
             
        });
    }
}
