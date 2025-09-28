package com.example.weatherclear;

import static com.example.weatherclear.utils.Utils.getWeatherMainString;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
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

    // TODO: Implement Current Location Function using GPS
    // TODO: Improve and Optimize Code

    // TODO: I need a design for Current Location Functionality
    // TODO: I need an Icon for Clouds and Sun
    // TODO: I need a remove Icon.
    // TODO: I need a design for Widget

    QuickSearchManager quickSearch;
    Button addToQuickSearch;
    TextView currentCity;
    ImageView iFirstDay;
    ImageView iSecondDay;
    ImageView iThirdDay;

    String apiKey = BuildConfig.WEATHER_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        super.onCreate(savedInstanceState);

        quickSearch = new QuickSearchManager(this);
        setContentView(R.layout.city_weather);
        callWebService();

        this.addToQuickSearch = findViewById(R.id.btnAddQuickSearch);
        this.currentCity = findViewById(R.id.cityName);
        this.iFirstDay = findViewById(R.id.iconFirstDay);
        this.iSecondDay = findViewById(R.id.iconSecondDay);
        this.iThirdDay = findViewById(R.id.iconThirdDay);
        Intent intentGoHome = new Intent(this, MainActivity.class);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                intentGoHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentGoHome);
                finish();
            }
        });

    }

    public void callWebService() {

        // check if the city is a valid input
        Intent intent = getIntent();
        String city = intent.getStringExtra("android.intent.extra.TEXT");
        if (city == null || city.isEmpty()) {
            goError();
            return;
        }

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

                        JSONObject coord = responseJSON.getJSONObject("coord");
                        String lat = coord.getString("lat");
                        String lon = coord.getString("lon");

                        CityWeather.this.forecast(lat, lon);
                    } else {
                        CityWeather.this.goError();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    CityWeather.this.goError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CityWeather.this.goError();
            }
        });
        queue.add(stringRequest);
    }

    private void configureQuickSearchButton(String cityName) {
        if (quickSearch.hasCity(cityName)) {
            addToQuickSearch.setText(R.string.remove_from_main_screen);
            addToQuickSearch.setOnClickListener(v -> {
                boolean removed = quickSearch.removeCity(cityName);
                int toastTextId = removed ?
                        R.string.city_removed_from_quick_search :
                        R.string.city_was_not_removed_from_quick_search;
                Toast.makeText(this, toastTextId, Toast.LENGTH_SHORT).show();
                addToQuickSearch.setText(R.string.add_to_main_screen);

                configureQuickSearchButton(cityName);
            });
        } else {
            addToQuickSearch.setText(R.string.add_to_main_screen);
            addToQuickSearch.setOnClickListener(v -> {
                boolean added = quickSearch.addCity(cityName);
                int toastTextId = added ?
                        R.string.city_added_to_quick_search :
                        R.string.city_was_not_added_to_quick_search;
                Toast.makeText(this, toastTextId, Toast.LENGTH_SHORT).show();
                addToQuickSearch.setText(R.string.remove_from_main_screen);

                configureQuickSearchButton(cityName);
            });
        }
    }


    private int getBackgroundIdBasedOnIcon(String icon) {
        if (icon == null || icon.isEmpty()) return 0;

        String resourceName = "background" + icon.toLowerCase();

        int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

        if (resourceId == 0) {
            resourceId = R.drawable.background01d;
        }

        return resourceId;
    }


    @SuppressLint("SetTextI18n")
    private void setCurrentWeather(JSONObject responseJSON) throws JSONException {
        final TextView textViewTemp = findViewById(R.id.temp);
        final TextView textCurrentMinAndMax = findViewById(R.id.currentMinAndMax);
        final TextView textViewWeather = findViewById(R.id.weather);
        final RelativeLayout layout = findViewById(R.id.Layout);
        final TextView textViewCityName = findViewById(R.id.cityName);

        JSONObject main = responseJSON.getJSONObject("main");
        JSONArray weatherArray = responseJSON.getJSONArray("weather");
        JSONObject weather = weatherArray.getJSONObject(0);
        String cityName = responseJSON.getString("name");

        configureQuickSearchButton(cityName.toUpperCase());

        double temp = main.getDouble("temp");
        double tempMax = main.getDouble("temp_max");
        double tempMin = main.getDouble("temp_min");

        textViewCityName.setText(cityName.toUpperCase());
        textViewTemp.setText(((int) temp) + "°");
        textCurrentMinAndMax.setText(getMinAndMaxLabel(tempMax, tempMin));
        layout.setBackgroundResource(getBackgroundIdBasedOnIcon(weather.getString("icon")));
        textViewWeather.setText(getWeatherMainString(CityWeather.this, weather.getString("main")));
    }

    private String getMinAndMaxLabel(double tempMax, double tempMin){
        int tempMaxInt = (int) tempMax;
        int tempMinInt = (int) tempMin;
        if (tempMaxInt == tempMinInt) {
            tempMaxInt++;
            tempMinInt--;
        }

        return tempMaxInt + "°/" + tempMinInt + "°";
    }

    public void forecast(String lat, String lon) {
        final TextView textViewTemperaturesFirstDay = (TextView) findViewById(R.id.temperatureFirstDay);
        final TextView textViewFirstDay = (TextView) findViewById(R.id.firstDay);
        final TextView textViewTemperaturesSecondDay = (TextView) findViewById(R.id.temperatureSecondDay);
        final TextView textViewSecondDay = (TextView) findViewById(R.id.secondDay);
        final TextView textViewTemperaturesThirdDay = (TextView) findViewById(R.id.temperatureThirdDay);
        final TextView textViewThirdDay = (TextView) findViewById(R.id.thirdDay);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&exclude=minutely,hourly&appid=" + apiKey + "&units=metric";
        StringRequest stringRequest = new StringRequest(0, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    JSONArray list = responseJSON.getJSONArray("list");

                    Map<String, List<JSONObject>> dailyData = new LinkedHashMap<>();

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject item = list.getJSONObject(i);
                        String dtTxt = item.getString("dt_txt");
                        String date = dtTxt.split(" ")[0];
                        String hour = dtTxt.split(" ")[1];

                        int h = Integer.parseInt(hour.substring(0, 2));
                        if (h >= 3 && h <= 21) {
                            if (!dailyData.containsKey(date)) {
                                dailyData.put(date, new ArrayList<>());
                            }
                            dailyData.get(date).add(item);
                        }
                    }

                    List<String> dates = new ArrayList<>(dailyData.keySet());



                    for (int i = 0; i < Math.min(3, dates.size()); i++) {
                        String date = dates.get(i);
                        List<JSONObject> dayItems = dailyData.get(date);

                        double tempMin = Double.MAX_VALUE;
                        double tempMax = -Double.MAX_VALUE;
                        String icon = null;

                        for (JSONObject item : dayItems) {
                            JSONObject main = item.getJSONObject("main");
                            tempMin = Math.min(tempMin, main.getDouble("temp_min"));
                            tempMax = Math.max(tempMax, main.getDouble("temp_max"));

                            String dtTxt = item.getString("dt_txt");
                            if (dtTxt.contains("15:00:00")) {
                                JSONArray weatherArray = item.getJSONArray("weather");
                                icon = weatherArray.getJSONObject(0).getString("icon");
                            }
                        }

                        if (icon == null && !dayItems.isEmpty()) {
                            JSONArray weatherArray = dayItems.get(0).getJSONArray("weather");
                            icon = weatherArray.getJSONObject(0).getString("icon");
                        }

                        String normalized_icon = getNormalizedIcon(icon);
                        int iconRes = getResources().getIdentifier(normalized_icon, "drawable", getPackageName());
                        String tempStr = getMinAndMaxLabel(tempMax, tempMin);
                        String dayName = getDayOfTheWeek(String.valueOf(dayItems.get(0).getLong("dt")));

                        if (i == 0) {
                            iFirstDay.setImageResource(iconRes);
                            textViewTemperaturesFirstDay.setText(tempStr);
                            textViewFirstDay.setText(dayName);
                        } else if (i == 1) {
                            iSecondDay.setImageResource(iconRes);
                            textViewTemperaturesSecondDay.setText(tempStr);
                            textViewSecondDay.setText(dayName);
                        } else {
                            iThirdDay.setImageResource(iconRes);
                            textViewTemperaturesThirdDay.setText(tempStr);
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
                goError();
            }
        });
        queue.add(stringRequest);
    }

    private String getNormalizedIcon(String icon) {
        System.out.print(icon);
        String result = "";
        switch (icon) {
            case "01d":
            case "01n":
                result = "soleado";
                break;
            case "50n":
            case "50d":
                result = "neblina";
                break;
            case "11n":
            case "11d":
                result = "rayos";
                break;
            case "03n":
            case "03d":
            case "04n":
            case "04d":
                result = "nublado";
                break;
            case "09n":
            case "09d":
                result = "lluvia";
                break;
            case "10n":
            case "10d":
                result = "lluvia_con_sol";
                break;
            case "13n":
            case "13d":
                result = "lluvia_con_sol";
                break;
            default:
                result = "a" + icon;
        }

        return result;
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
        finish();
        Intent intentGoError = new Intent(this, (Class<?>) Error.class);
        startActivity(intentGoError);
    }
}