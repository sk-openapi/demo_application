package com.openapi_test_app.testdraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

public class MainActivity extends AppCompatActivity {
    private TextView main_text1;
    protected SharedPreferences preferences;
    protected SharedPreferences.Editor editor;
    public String main_address, main_resLon, main_resLat, appKey;
    public MainAsyncTask task;
    public MainGraphAsyncTask task_graph;
    public TextView main_textView_temp, main_textView_temp_yesterday, main_textView_mention;
    public ImageView main_imageView1, main_imageView2, main_imageView3, main_imageView_button, main_imageBg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("sFile", MODE_PRIVATE);
        editor = preferences.edit();
        main_address = preferences.getString("location_address", "No_address");
        main_resLon = preferences.getString("location_resLon", "No_resLon");
        main_resLat = preferences.getString("location_resLat", "No_resLat");

        editor.apply();

        main_text1 = findViewById(R.id.main_location_name);
        main_textView_temp = findViewById(R.id.main_textView_temp);
        main_textView_temp_yesterday = findViewById(R.id.main_textView_temp_yesterday);
        main_textView_mention = findViewById(R.id.main_textView_mention);

        main_imageView1 = findViewById(R.id.main_imageView_cloth_1);
        main_imageView2 = findViewById(R.id.main_imageView_cloth_2);
        main_imageView3 = findViewById(R.id.main_imageView_cloth_3);
        main_imageBg = findViewById(R.id.main_background);


        main_text1.setText(main_address);
        appKey = getResources().getString(R.string.appKey);

        task = new MainAsyncTask(main_textView_temp, main_textView_temp_yesterday, main_textView_mention, main_imageView1, main_imageView2, main_imageView3, main_imageBg);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,main_resLat, main_resLon, appKey);

        LineChart lineChart = findViewById(R.id.main_graph_chart);
        TextView main_textView_graph_title = findViewById(R.id.main_textView_graph_title);

        task_graph = new MainGraphAsyncTask(lineChart, main_textView_graph_title);
        task_graph.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,main_resLat, main_resLon, appKey);

        main_imageView_button = findViewById(R.id.main_icon_location_add);
        main_imageView_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_location_to_location_setting = new Intent(MainActivity.this, LocationSettingActivity.class);
                MainActivity.this.startActivity(intent_location_to_location_setting);
                finish();
            }
        });
    }

}