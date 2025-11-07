package com.example.weatherclear;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class QuickSearchManager {

    private static final String PREFS_NAME = "quick_search_prefs";
    private static final String KEY_CITIES = "quick_search_cities";
    private static final String KEY_WIDGET_CITY = "widget_city";
    private static final String KEY_WIDGET_TEMPERATURE = "widget_temperature";
    private static final String KEY_WIDGET_ICON = "widget_icon";
    private static final String KEY_WEATHER_DESCRIPTION = "weather_description";
    private static final int MAX_CITIES = 3;

    private SharedPreferences prefs;

    public QuickSearchManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean addCity(String city) {
        Set<String> cities = new HashSet<>(getCities());
        if (cities.size() < MAX_CITIES) {
            boolean added = cities.add(city);
            if(added) {
                prefs.edit().putStringSet(KEY_CITIES, cities).apply();
            }
            return added;
        }
        return false;
    }

    public boolean removeCity(String city) {
        Set<String> cities = new HashSet<>(getCities());
        boolean removed = cities.remove(city);
        if (removed) {
            prefs.edit().putStringSet(KEY_CITIES, cities).apply();
        }
        return removed;
    }
    public boolean hasCity(String city) {
        Set<String> cities = getCities();
        return cities.contains(city);
    }
    public Set<String> getCities() {
        return prefs.getStringSet(KEY_CITIES, new HashSet<>());
    }

    public void setWidgetData(String city, String temperature, String iconName, String description) {
        prefs.edit()
                .putString(KEY_WIDGET_CITY, city)
                .putString(KEY_WIDGET_TEMPERATURE, temperature)
                .putString(KEY_WIDGET_ICON, iconName)
                .putString(KEY_WEATHER_DESCRIPTION, description)
                .apply();
    }

    public WidgetData getWidgetData() {
        String city = prefs.getString(KEY_WIDGET_CITY, "CHILLÁN");
        String temperature = prefs.getString(KEY_WIDGET_TEMPERATURE, "20°C");
        String iconName = prefs.getString(KEY_WIDGET_ICON, "soleado");
        String description = prefs.getString(KEY_WEATHER_DESCRIPTION, "Despejado");
        return new WidgetData(city, temperature, iconName, description);
    }

    public static class WidgetData {
        public final String city;
        public final String temperature;
        public final String iconName;
        public final String description;

        public WidgetData(String city, String temperature, String iconName, String description) {
            this.city = city;
            this.temperature = temperature;
            this.iconName = iconName;
            this.description = description;
        }
    }
}
