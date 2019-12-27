package com.openapi_test_app.testdraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class InitialSettingActivity extends AppCompatActivity {
    protected ArrayList<String> items1, items2;
    protected ListView listView1, listView2;
    protected Button initial_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setting);

        items1 = new ArrayList<>();
        items1.add("더위를 많이 탄다.");
        items1.add("더위를 조금 타는 편이다.");
        items1.add("보통이다.");
        items1.add("추위를 조금 타는 편이다.");
        items1.add("추위를 많이 탄다.");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, items1);

        listView1 = findViewById(R.id.initial_listView1);
        listView1.setAdapter(adapter1);

        items2 = new ArrayList<>();
        items2.add("사무실");
        items2.add("문이 열려있는 실내");
        items2.add("학생(건물별 강의실 이동)");
        items2.add("야외");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, items2);

        listView2 = findViewById(R.id.initial_listView2);
        listView2.setAdapter(adapter2);

        initial_button = findViewById(R.id.initial_button1);

        initial_button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                SharedPreferences preferences = getSharedPreferences("sFile", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putInt("list1position", listView1.getCheckedItemPosition());
                editor.putInt("list2position", listView2.getCheckedItemPosition());
                editor.putString("initial_setting","initial_setting");

                editor.apply();

                Intent intent_initial_to_location = new Intent(getApplicationContext(), LocationSettingActivity.class);
                startActivity(intent_initial_to_location);

                finish();
            }
        });

    }
}
