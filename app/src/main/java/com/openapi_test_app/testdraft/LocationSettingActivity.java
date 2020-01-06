package com.openapi_test_app.testdraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationSettingActivity extends AppCompatActivity {

    protected EditText location_editText;
    protected Button location_button1, location_button2;
    protected ListView location_listView;
    public static final int LOAD_SUCCESS = 101;
    protected static List<HashMap<String,String>> dongInfoList = null;
    protected TextView location_textView_error;
    public LocationAsyncTask task;

    protected SharedPreferences preferences;
    protected SharedPreferences.Editor editor;
    public ArrayList<HashMap<String, String>> location_info_list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_setting);

        location_editText = findViewById(R.id.location_edittext);
        location_button1 = findViewById(R.id.location_button1);
        location_button2 = findViewById(R.id.location_button2);
        location_listView = findViewById(R.id.location_listView1);
        location_textView_error = findViewById(R.id.location_text_error);


        dongInfoList = new ArrayList<HashMap<String, String>>();

        location_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location_dong = location_editText.getText().toString();
                String appKey = getResources().getString(R.string.appKey);
                if (location_dong.length() == 0){
                    // No input name_dong!!
                    Toast.makeText(getApplicationContext(), "읍/면/동 을 입력하세요", Toast.LENGTH_LONG).show();
                }else{
                    try {
                        location_info_list = new LocationAsyncTask(location_listView, location_textView_error).execute(location_dong, appKey).get();
                    }catch (Exception e){
                        location_info_list = new ArrayList<>();
                    }
                }

            }
        });



        location_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //location setting 여부 preference 기록

                try {
                    preferences = getSharedPreferences("sFile", MODE_PRIVATE);
                    editor = preferences.edit();
                    editor.putString("location_setting", "location_setting");
                    int itemPosition = location_listView.getCheckedItemPosition();

                    HashMap<String, String> firstHashMap = location_info_list.get(itemPosition);

                    editor.putString("location_address", firstHashMap.get("address"));
                    editor.putString("location_resLat", firstHashMap.get("resLat"));
                    editor.putString("location_resLon", firstHashMap.get("resLon"));
                    editor.apply();
                    MainActivity MA = (MainActivity) MainActivity.activity;
                    MA.finish();

                }catch (Exception e){
                    e.printStackTrace();
                }

                Intent intent_location_to_main = new Intent(LocationSettingActivity.this, MainActivity.class);
                LocationSettingActivity.this.startActivity(intent_location_to_main);
                finish();
            }

        });



    }



}
