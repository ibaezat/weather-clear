package com.example.weatherclear;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class QuickSearchManager {

    private static final String PREFS_NAME = "quick_search_prefs";
    private static final String KEY_CITIES = "quick_search_cities";
    private static final String KEY_LAST_SEARCH = "last_search";
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

    public void setLastSearch(String city) {
        prefs.edit().putString(KEY_LAST_SEARCH, city).apply();
    }

    public boolean hasCity(String city) {
        Set<String> cities = getCities();
        return cities.contains(city);
    }

    public Set<String> getCities() {
        return prefs.getStringSet(KEY_CITIES, new HashSet<>());
    }

    public String getLastSearch() {
        return prefs.getString(KEY_LAST_SEARCH, "CHILLÁN");
    }
}
