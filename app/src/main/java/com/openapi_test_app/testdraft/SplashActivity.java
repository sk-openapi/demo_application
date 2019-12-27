package com.openapi_test_app.testdraft;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity{
    //preference parameter initialize
    protected SharedPreferences preferences;
    protected SharedPreferences.Editor editor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        preferences = getSharedPreferences("sFile", MODE_PRIVATE);
        editor = preferences.edit();
        String initial_setting = preferences.getString("initial_setting","No_initial_setting");
        String location_setting = preferences.getString("location_setting", "No_location_setting");
        editor.apply();
        Intent intent_main = new Intent(this, MainActivity.class);
        Intent intent_initial_setting = new Intent(this, InitialSettingActivity.class);
        Intent intent_location_setting = new Intent(this, LocationSettingActivity.class);


        if (initial_setting.equals("No_initial_setting")){
            startActivity(intent_initial_setting);
        }
        else if (location_setting.equals("No_location_setting")){
            startActivity(intent_location_setting);
        }
        else
            startActivity(intent_main);

        finish();
    }
}
