package com.example.weatherclear;

import static com.example.weatherclear.utils.Utils.getWeatherMainString;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherWidget extends AppWidgetProvider {

    private static final String API_KEY = BuildConfig.WEATHER_API_KEY;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_weather);
        QuickSearchManager quickSearchManager = new QuickSearchManager(context);

        String city = quickSearchManager.getLastSearch();
        views.setTextViewText(R.id.widget_city, city);
        views.setTextViewText(R.id.widget_temp, "...");
        views.setTextViewText(R.id.widget_temp_desc, "...");

        Intent intent = new Intent(context, WeatherWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        views.setOnClickPendingIntent(R.id.widget_refresh, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);


        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + ",CL&appid=" + API_KEY + "&units=metric";
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(0, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            JSONObject main = responseJSON.getJSONObject("main");
                            JSONArray weatherArray = responseJSON.getJSONArray("weather");
                            JSONObject weather = weatherArray.getJSONObject(0);

                            double temp = main.getDouble("temp");
                            String tempText = ((int) temp) + "°C";

                            views.setTextViewText(R.id.widget_temp, tempText);
                            views.setTextViewText(R.id.widget_temp_desc, getWeatherMainString(context, weather.getString("main")));
                            appWidgetManager.updateAppWidget(appWidgetId, views);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.print(error);
                    }
                });

        queue.add(stringRequest);
    }
}
