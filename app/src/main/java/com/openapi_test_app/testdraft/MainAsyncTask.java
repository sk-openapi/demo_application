package com.openapi_test_app.testdraft;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainAsyncTask extends AsyncTask<String, String, Object> {
    TextView textView1, textView2, textView3;
    ImageView imageView1, imageView2, imageView3, imageViewBg;
    String temp, resLon, resLat, tempResult, temp_mention, temp_yesterday, mention, sky;
    JSONObject jsonObject;
    int resId1, resId2, resId3, resIdBg;
    String resIdStr1, resIdStr2, resIdStr3, resIdStrBg;
    protected MainAsyncTask(TextView textView1, TextView textView2, TextView textView3, ImageView imageView1, ImageView imageView2, ImageView imageView3, ImageView imageViewBg) {
        this.textView1 = textView1;
        this.textView2 = textView2;
        this.textView3 = textView3;
        this.imageView1 = imageView1;
        this.imageView2 = imageView2;
        this.imageView3 = imageView3;
        this.imageViewBg = imageViewBg;
    }

    @Override
    protected Object doInBackground(String[] strings) {
        resLat = strings[0];
        resLon = strings[1];
        String appKey = strings[2];
        String item1str = strings[3];
        String item2str = strings[4];

        if (resLat.length() == 0 | resLon.length() == 0){
            return false;
        }
        else{
            try {
                // WeatherPlanet API (minutely) connection
                String url_min = "https://apis.openapi.sk.com/weather/current/minutely";
                JSONObject jsonObject = weatherApiCall(url_min, appKey, resLat, resLon);

                //JsonObject에서 현재 온도값만 String으로 추출

                JSONObject jsonObject_weather = jsonObject.getJSONObject("weather");
                JSONArray jsonArray_minutely = jsonObject_weather.getJSONArray("minutely");
                JSONObject jsonObject_station = jsonArray_minutely.getJSONObject(0);

                JSONObject jsonObject_temp = jsonObject_station.getJSONObject("temperature");
                temp = jsonObject_temp.getString("tc");
                tempResult = temp + "\u2103";

                //어제 날씨 호출
                String url_yes = "https://apis.openapi.sk.com/weather/yesterday";
                JSONObject jsonObject1 = weatherApiCall(url_yes, appKey, resLat, resLon);

                JSONObject jsonObject2_weather = jsonObject1.getJSONObject("weather");
                JSONArray jsonArray_yesterday = jsonObject2_weather.getJSONArray("yesterday");
                JSONObject jsonObject_station2 = jsonArray_yesterday.getJSONObject(0);

                JSONObject jsonObject_day = jsonObject_station2.getJSONObject("day");
                JSONArray jsonArray_hourly = jsonObject_day.getJSONArray("hourly");
                JSONObject jsonObject_station3 = jsonArray_hourly.getJSONObject(0);

                String temp_yester = jsonObject_station3.getString("temperature");

                //어제 날씨와 오늘 날씨 차이 계산
                float temp_yesterFloat = Float.parseFloat(temp_yester);
                float tempFloat = Float.parseFloat(temp);

                float cal_result = temp_yesterFloat - tempFloat;
                Integer int_result = Math.round(cal_result);

                if(int_result <0){
                    int_result = Math.abs(int_result);
                    String result = Integer.toString(int_result);
                    temp_yesterday = "어제보다 " + int_result +"\u2103" + " 높음";
                } else if (int_result >0){
                    String result = Integer.toString(int_result);
                    temp_yesterday = "어제보다 " + int_result +"\u2103" + " 낮음";
                } else{
                    temp_yesterday = "어제와 같음";
                }

                //초기 설정 적용
                tempFloat = tempFloat + 2 - Float.parseFloat(item1str) - Float.parseFloat(item2str);

                if(tempFloat <= 4){
                    mention = "패딩, 두꺼운 코트, 목도리, 기모제품";
                    resId1 = R.drawable.ic_cloth_4_hat;
                    resId2 = R.drawable.ic_cloth_4_sweater;
                    resId3 = R.drawable.ic_cloth_4_padding;
                }else if (tempFloat <= 8){
                    mention = "코트, 가죽자켓, 히트텍, 니트, 레깅스";
                    resId1 = R.drawable.ic_cloth_5_scarf;
                    resId2 = R.drawable.ic_cloth_5_vest;
                    resId3 = R.drawable.ic_cloth_5_coat;
                }else if (tempFloat <= 11){
                    mention = "자켓, 트렌치코트, 야상, 니트, 청바지, 스타킹";
                    resId1 = R.drawable.ic_cloth_9_sweater;
                    resId2 = R.drawable.ic_cloth_9_coat;
                    resId3 = R.drawable.ic_cloth_9_jean;
                }else if (tempFloat <= 16){
                    mention = "자켓, 가디건, 야상, 스타킹, 청바지, 면바지";
                    resId1 = R.drawable.ic_cloth_12_hood;
                    resId2 = R.drawable.ic_cloth_12_jumper;
                    resId3 = R.drawable.ic_cloth_12_pants;
                }else if (tempFloat <= 19){
                    mention = "얇은 니트 맨투맨, 가디건, 청바지";
                    resId1 = R.drawable.ic_cloth_17_tshirt;
                    resId2 = R.drawable.ic_cloth_17_cardigan;
                    resId3 = R.drawable.ic_cloth_17_pants;
                }else if (tempFloat <= 22){
                    mention = "얇은 가디건, 긴팔, 면바지, 청바지";
                    resId1 = R.drawable.ic_cloth_20_tshirt;
                    resId2 = R.drawable.ic_cloth_20_shirt;
                    resId3 = R.drawable.ic_cloth_20_pants;
                }else if (tempFloat <= 27){
                    mention = "반팔, 얇은 셔트, 반바지, 면바지";
                    resId1 = R.drawable.ic_cloth_23_tshirt;
                    resId2 = R.drawable.ic_cloth_23_shirt;
                    resId3 = R.drawable.ic_cloth_23_shorts;
                }else{
                    mention = "민소매, 반팔, 반바지, 원피스";
                    resId1 = R.drawable.ic_cloth_28_sleeveless;
                    resId2 = R.drawable.ic_cloth_28_tshirt;
                    resId3 = R.drawable.ic_cloth_28_shorts;
                }
                temp_mention = "오늘도 출근 화이팅!\n" + mention + "\n를 추천드려요";

                resIdStr1 = Integer.toString(resId1);
                resIdStr2 = Integer.toString(resId2);
                resIdStr3 = Integer.toString(resId3);

                JSONObject jsonObject_sky = jsonObject_station.getJSONObject("sky");
                sky = jsonObject_sky.getString("code");

                switch (sky){
                    case "SKY_A01":
                        resIdBg = R.drawable.sky_a01;
                        break;
                    case "SKY_A02":
                        resIdBg = R.drawable.sky_a02;
                        break;
                    case "SKY_A03":
                        resIdBg = R.drawable.sky_a03;
                        break;
                    case "SKY_A04": case "SKY_A06": case "SKY_A08":
                        resIdBg = R.drawable.sky_a08;
                        break;
                    case "SKY_A05":
                        resIdBg = R.drawable.sky_a05;
                        break;
                    case "SKY_A07":
                        resIdBg = R.drawable.sky_a07;
                        break;
                    case "SKY_A09":
                        resIdBg = R.drawable.sky_a09;
                        break;
                    case "SKY_A10":
                        resIdBg = R.drawable.sky_a10;
                        break;
                    case "SKY_A11":
                        resIdBg = R.drawable.sky_a11;
                        break;
                    case "SKY_A12": case "SKY_A13": default:
                        resIdBg = R.drawable.sky_a12;
                        break;
                }

                resIdStrBg = Integer.toString(resIdBg);

            }catch (Exception e){
                tempResult = e.toString();
            }
            finally {
                publishProgress(tempResult, temp_yesterday, temp_mention, resIdStr1, resIdStr2, resIdStr3, resIdStrBg);
                return true;
            }


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

    protected JSONObject weatherApiCall(String urlstr, String appKey, String resLat, String resLon){
        String weatherUrlString = urlstr + "?appkey=" + appKey + "&version=2&lat=" + resLat + "&lon=" + resLon;
        try{
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
            streamReader.close();
            httpURLConnection.disconnect();

            jsonObject = new JSONObject(jsonStrBuilder.toString());
        }catch (Exception e){
            jsonObject = new JSONObject(e.toString());
        }finally {
            return jsonObject;
        }

    }
    @Override
    protected void onProgressUpdate(String[] values) {
        textView1.setText(values[0]);
        textView2.setText(values[1]);
        textView3.setText(values[2]);

        int viewId1 = Integer.parseInt(values[3]);
        int viewId2 = Integer.parseInt(values[4]);
        int viewId3 = Integer.parseInt(values[5]);
        int viewId4 = Integer.parseInt(values[6]);

        imageView1.setImageResource(viewId1);
        imageView2.setImageResource(viewId2);
        imageView3.setImageResource(viewId3);
        imageViewBg.setImageResource(viewId4);

    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
    }
}
