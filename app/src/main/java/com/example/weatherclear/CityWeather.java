package com.example.weatherclear;

import static com.example.weatherclear.utils.Utils.getWeatherDescriptionString;
import static com.example.weatherclear.utils.Utils.getWeatherMainString;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CityWeather extends AppCompatActivity {
    ImageView iFirstDay;
    ImageView iSecondDay;
    ImageView iThirdDay;
    ImageView iView;

    String apiKey = "e0256d5e7ea20a759ddace22c483d6b5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.city_weather);
        callWebService();

        this.iView = findViewById(R.id.icon);
        this.iFirstDay = findViewById(R.id.iconFirstDay);
        this.iSecondDay = findViewById(R.id.iconSecondDay);
        this.iThirdDay = findViewById(R.id.iconThirdDay);
    }

    public void callWebService() {

        // check if the city is a valid input
        Intent intent = getIntent();
        String city = intent.getStringExtra("android.intent.extra.TEXT");
        if (city == null || city.isEmpty()) {
            error();
            return;
        }

        // get the text view to display the city name
        final TextView textViewCityName = findViewById(R.id.cityName);
        final RelativeLayout layout = findViewById(R.id.Layout);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + ",CL&appid=" + apiKey + "&units=metric";
        StringRequest stringRequest = new StringRequest(0, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    String cod = responseJSON.getString("cod");

                    if (cod.equals("200")) {
                        CityWeather.this.setCurrentWeather(responseJSON);

                        JSONArray weatherArray = responseJSON.getJSONArray("weather");
                        JSONObject coord = responseJSON.getJSONObject("coord");
                        String cityName = responseJSON.getString("name");
                        JSONObject weather = weatherArray.getJSONObject(0);

                        layout.setBackgroundResource(getBackgroundIdBasedOnIcon(weather.getString("icon")));

                        String lat = coord.getString("lat");
                        String lon = coord.getString("lon");
                        textViewCityName.setText(cityName);

                        CityWeather.this.forecast(lat, lon);
                    } else {
                        CityWeather.this.error();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    CityWeather.this.error();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CityWeather.this.error();
            }
        });
        queue.add(stringRequest);
    }

    private int getBackgroundIdBasedOnIcon(String icon) {
        if (icon == null || icon.isEmpty()) return 0;

        String resourceName = "background" + icon.toLowerCase();

        int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

        if (resourceId == 0) {
            resourceId = R.drawable.background;
        }

        return resourceId;
    }


    @SuppressLint("SetTextI18n")
    private void setCurrentWeather(JSONObject responseJSON) throws JSONException {
        final TextView textViewTemp = findViewById(R.id.temp);
        final TextView textViewTempMax = findViewById(R.id.temp_max);
        final TextView textViewTempMin = findViewById(R.id.temp_min);
        final TextView textViewWeather = findViewById(R.id.weather);
        final TextView textViewWeatherDesc = findViewById(R.id.weather_desc);

        JSONObject main = responseJSON.getJSONObject("main");
        JSONArray weatherArray = responseJSON.getJSONArray("weather");
        JSONObject weather = weatherArray.getJSONObject(0);

        double temp = main.getDouble("temp");
        double tempMax = main.getDouble("temp_max");
        double tempMin = main.getDouble("temp_min");
        String weatherMain = getWeatherMainString(CityWeather.this, weather.getString("main"));
        String weatherDesc = getWeatherDescriptionString(CityWeather.this, weather.getString("description"));

        textViewTemp.setText(((int) temp) + "°C");
        textViewTempMax.setText(((int) tempMax) + "°C");
        textViewTempMin.setText(((int) tempMin) + "°C");
        textViewWeather.setText(weatherMain);
        textViewWeatherDesc.setText(weatherDesc);

        String icon = weather.getString("icon");
        int iconRes = getResources().getIdentifier("a" + icon, "drawable", getPackageName());
        iView.setImageResource(iconRes);
    }

    public void forecast(String lat, String lon) {
        final TextView textViewTemperaturesFirstDay = (TextView) findViewById(R.id.temperatureFirstDay);
        final TextView textViewWeatherFirstDay = (TextView) findViewById(R.id.weatherFirstDay);
        final TextView textViewFirstDay = (TextView) findViewById(R.id.firstDay);
        final TextView textViewTemperaturesSecondDay = (TextView) findViewById(R.id.temperatureSecondDay);
        final TextView textViewWeatherSecondDay = (TextView) findViewById(R.id.weatherSecondDay);
        final TextView textViewSecondDay = (TextView) findViewById(R.id.SecondDay);
        final TextView textViewTemperaturesThirdDay = (TextView) findViewById(R.id.temperatureThirdDay);
        final TextView textViewWeatherThirdDay = (TextView) findViewById(R.id.weatherThirdDay);
        final TextView textViewThirdDay = (TextView) findViewById(R.id.ThirdDay);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&exclude=minutely,hourly&appid=" + apiKey + "&units=metric";
        StringRequest stringRequest = new StringRequest(0, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    JSONArray list = responseJSON.getJSONArray("list");

                    Map<String, JSONObject> dailyData = new LinkedHashMap<>();

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject item = list.getJSONObject(i);
                        String dtTxt = item.getString("dt_txt");
                        String date = dtTxt.split(" ")[0];

                        if (!dailyData.containsKey(date) && dtTxt.contains("12:00:00")) {
                            dailyData.put(date, item);
                        }
                    }

                    List<String> dates = new ArrayList<>(dailyData.keySet());

                    for (int i = 0; i < Math.min(3, dates.size()); i++) {
                        String date = dates.get(i);
                        JSONObject item = dailyData.get(date);

                        JSONObject main = item.getJSONObject("main");
                        JSONArray weatherArray = item.getJSONArray("weather");
                        JSONObject weather = weatherArray.getJSONObject(0);

                        double tempMax = main.getDouble("temp_max");
                        double tempMin = main.getDouble("temp_min");
                        String weatherMain = getWeatherMainString(CityWeather.this, weather.getString("main"));
                        String icon = weather.getString("icon");
                        String tempStr = ((int) tempMax) + "°/" + ((int) tempMin) + "°";

                        String dayName = getDayOfTheWeek(String.valueOf(item.getLong("dt")));

                        int iconRes = getResources().getIdentifier("a" + icon, "drawable", getPackageName());

                        if (i == 0) {
                            iFirstDay.setImageResource(iconRes);
                            textViewTemperaturesFirstDay.setText(tempStr);
                            textViewWeatherFirstDay.setText(weatherMain);
                            textViewFirstDay.setText(dayName);
                        } else if (i == 1) {
                            iSecondDay.setImageResource(iconRes);
                            textViewTemperaturesSecondDay.setText(tempStr);
                            textViewWeatherSecondDay.setText(weatherMain);
                            textViewSecondDay.setText(dayName);
                        } else {
                            iThirdDay.setImageResource(iconRes);
                            textViewTemperaturesThirdDay.setText(tempStr);
                            textViewWeatherThirdDay.setText(weatherMain);
                            textViewThirdDay.setText(dayName);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error -> forecaste on error response");
                error();
            }
        });
        queue.add(stringRequest);
    }

    public static String truncateTemp(String value, int length) {
        int length2 = value.length() == 5 ? length : length - 1;
        if (value.length() > length2) {
            return value.substring(0, length2);
        }
        return value;
    }

    String getDayOfTheWeek(String dt) throws NumberFormatException {
        int dtParsed = Integer.parseInt(dt);
        Instant date = Instant.ofEpochSecond(dtParsed);
        LocalDate localDate = date.atZone(ZoneOffset.UTC).toLocalDate();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        String result;

        switch (dayOfWeek) {
            case MONDAY:
                result = getString(R.string.monday);
                break;
            case TUESDAY:
                result = getString(R.string.tuesday);
                break;
            case WEDNESDAY:
                result = getString(R.string.wednesday);
                break;
            case THURSDAY:
                result = getString(R.string.thursday);
                break;
            case FRIDAY:
                result = getString(R.string.friday);
                break;
            case SATURDAY:
                result = getString(R.string.saturday);
                break;
            case SUNDAY:
                result = getString(R.string.sunday);
                break;
            default:
                result = "---";
        }

        return result;
    }

    public void goError() {
        Intent intentGoError = new Intent(this, (Class<?>) Error.class);
        startActivity(intentGoError);
    }

    public void error() {
        finish();
        goError();
    }
}