package com.openapi_test_app.testdraft;

import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class LocationAsyncTask extends AsyncTask<String, ArrayList<HashMap<String,String>>, ArrayList<HashMap<String, String>>> {

    ListView listView;
    TextView textView;
    ListViewAdapter adapter;
    ArrayList<HashMap<String,String>> location_infoList = new ArrayList<>();
    String appKey;
    String location_str, error;
    JSONObject jsonObject;

    public LocationAsyncTask(ListView listView, TextView textView){

        this.listView = listView;
        this.textView = textView;
    }

    @Override
    //초기화 작업
    protected void onPreExecute() {

        super.onPreExecute();

    }

    @Override
    //스레드 작업 종료
    protected void onPostExecute(ArrayList<HashMap<String, String>> o) {
        super.onPostExecute(o);
    }

    @Override
    //UI 작업
    protected void onProgressUpdate(ArrayList<HashMap<String,String>>[] locationInfoList) {

        ArrayList<HashMap<String, String>> hashMaps = locationInfoList[0];
        ArrayList<HashMap<String, String>> errorArrayList = locationInfoList[1];

        HashMap<String, String> errorHashMap = errorArrayList.get(0);
        String error_result = errorHashMap.get("Error");
        textView.setText(error_result);


        adapter = new ListViewAdapter();
        listView.setAdapter(adapter);



        for (int i = 0; i < hashMaps.size(); i++){
            HashMap<String, String> hashMap = hashMaps.get(i);
            String str_address = hashMap.get("address");
            String str_resLon = hashMap.get("resLon");
            String str_resLat = hashMap.get("resLat");

            adapter.addItem(str_address,str_resLon,str_resLat);
        }


        adapter.notifyDataSetChanged();


    }

    @Override
    //Task 취소됐을 때 호출
    protected void onCancelled(ArrayList<HashMap<String, String>> o) {
        super.onCancelled(o);
    }

    @Override
    //백그라운드 작업 구현
        protected ArrayList<HashMap<String, String>> doInBackground(String[] strings) {
        location_str = strings[0];
        appKey = strings[1];

        if (location_str.length() == 0) {
            return location_infoList;
        } else {

            try {
                String locationString = "https://apis.openapi.sk.com/tmap/poi/findPoiAreaDataByName?appKey=" + appKey + "&version=1&count=200&page=1&area_dong=" + location_str + "&addressType=addressName";
                URL url = new URL(locationString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();

                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                httpURLConnection.disconnect();

                jsonObject = new JSONObject(stringBuilder.toString());
                JSONObject locations = jsonObject.getJSONObject("findPoiAreaDataByNameInfo");
                int jsonCnt = locations.getInt("totalCnt");
                JSONArray dongInfos = locations.getJSONArray("dongInfo");


                for (int i = 0; i < jsonCnt; i++) {
                    JSONObject dongInfo = dongInfos.getJSONObject(i);
                    String address = dongInfo.getString("address");
                    String resLon = dongInfo.getString("resLon");
                    String resLat = dongInfo.getString("resLat");

                    HashMap<String, String> dongInfoMap = new HashMap<>();
                    dongInfoMap.put("address", address);
                    dongInfoMap.put("resLon", resLon);
                    dongInfoMap.put("resLat", resLat);

                    location_infoList.add(dongInfoMap);
                }
                error = "No Error";
            } catch (Exception e) {
                error = e.toString();

            } finally {
                HashMap<String, String> errorHashMap = new HashMap<>();
                errorHashMap.put("Error", error);
                ArrayList<HashMap<String, String>> errorList = new ArrayList<>();
                errorList.add(0, errorHashMap);
                publishProgress(location_infoList, errorList);
            }
            return location_infoList;

        }
    }
}
