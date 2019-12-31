package com.openapi_test_app.testdraft;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainGraphAsyncTask extends AsyncTask<String, Object, Object> {
    String resLat, resLon, temperature;
    LineChart lineChart;
    TextView textView;
    JSONObject jsonObject;
    String temp4hour, temp7hour, temp10hour, temp13hour, temp16hour, temp19hour, temp22hour;
    String Error;

    public MainGraphAsyncTask(LineChart lineChart, TextView textView) {
        this.lineChart = lineChart;
        this.textView = textView;
    }

    @Override
    protected Object doInBackground(String[] strings) {
        resLat = strings[0];
        resLon = strings[1];
        String appKey = strings[2];

        if (resLat.length() == 0 | resLon.length() == 0){
            return false;
        }
        else{
            try {
                //WeatherPlanet API (hourly) connection
                String weatherUrlString = "https://apis.openapi.sk.com/weather/forecast/3days?appKey=" + appKey + "&lat=" + resLat + "&lon=" + resLon;
                URL url = new URL(weatherUrlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;

                if (responseStatusCode == HttpURLConnection.HTTP_OK){
                    inputStream = httpURLConnection.getInputStream();
                }else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder jsonStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null){
                    jsonStrBuilder.append(inputStr);
                }

                jsonObject = new JSONObject(jsonStrBuilder.toString());

                JSONObject jsonObject_weather = jsonObject.getJSONObject("weather");
                JSONArray jsonArray_forecast3days = jsonObject_weather.getJSONArray("forecast3days");
                JSONObject jsonObject_fcst = jsonArray_forecast3days.getJSONObject(0);
                JSONObject jsonObject_fcst3hour = jsonObject_fcst.getJSONObject("fcst3hour");
                JSONObject jsonObject_temperature = jsonObject_fcst3hour.getJSONObject("temperature");

                temp4hour = jsonObject_temperature.getString("temp4hour");
                temp7hour = jsonObject_temperature.getString("temp7hour");
                temp10hour = jsonObject_temperature.getString("temp10hour");
                temp13hour = jsonObject_temperature.getString("temp13hour");
                temp16hour = jsonObject_temperature.getString("temp16hour");
                temp19hour = jsonObject_temperature.getString("temp19hour");
                temp22hour = jsonObject_temperature.getString("temp22hour");

                temperature = jsonObject_temperature.toString();
                Error = "날씨 예보";

            }catch (Exception e){
                Error = e.toString();
                return false;
            }finally {
                publishProgress(Error, temp4hour, temp7hour, temp10hour,temp13hour,temp16hour,temp19hour,temp22hour);
            }

            return true;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        String error = (String) values[0];
        String temp4 = (String) values[1];
        String temp7 = (String) values[2];
        String temp10 = (String) values[3];
        String temp13 = (String) values[4];
        String temp16 = (String) values[5];
        String temp19 = (String) values[6];
        String temp22 = (String) values[7];
        ArrayList<Entry> val = new ArrayList<>();
        float temp4hour_float = Float.parseFloat(temp4);
        float temp7hour_float = Float.parseFloat(temp7);
        float temp10hour_float = Float.parseFloat(temp10);
        float temp13hour_float = Float.parseFloat(temp13);
        float temp16hour_float = Float.parseFloat(temp16);
        float temp19hour_float = Float.parseFloat(temp19);
        float temp22hour_float = Float.parseFloat(temp22);
        val.add(new Entry(4, temp4hour_float));
        val.add(new Entry(7, temp7hour_float));
        val.add(new Entry(10, temp10hour_float));
        val.add(new Entry(13, temp13hour_float));
        val.add(new Entry(16,temp16hour_float));
        val.add(new Entry(19, temp19hour_float));
        val.add(new Entry(22, temp22hour_float));


        LineDataSet lineDataSet = new LineDataSet(val, null);
        LineData lineData = new LineData(lineDataSet);

        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setDrawHighlightIndicators(false);
        lineChart.setDescription(null);
        lineChart.getLegend().setEnabled(false);

        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);

        lineData.setValueTextSize(12);
        lineData.setValueTextColor(Color.WHITE);

        Paint paint = lineChart.getRenderer().getPaintRender();

        int height = lineChart.getHeight();
        LinearGradient linGrad = new LinearGradient(0,0,0,height, 0xFFFF0000, 0xBA0000FF, Shader.TileMode.REPEAT);
        paint.setShader(linGrad);

        lineChart.setData(lineData);
        lineChart.getAxisLeft().setTextColor(Color.WHITE);
        lineChart.getXAxis().setTextColor(Color.WHITE);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.invalidate();
        textView.setText(error);
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
    }
}
