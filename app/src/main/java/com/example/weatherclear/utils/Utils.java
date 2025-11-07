package com.example.weatherclear.utils;


import android.content.Context;

import com.example.weatherclear.R;

public class Utils {
    public static String getWeatherMainString(Context context, String main) {
        if (main == null) return "";

        switch (main.toLowerCase()) {
            case "thunderstorm": return context.getString(R.string.thunderstorm);
            case "drizzle":      return context.getString(R.string.drizzle);
            case "rain":         return context.getString(R.string.rain);
            case "snow":         return context.getString(R.string.snow);
            case "mist":         return context.getString(R.string.mist);
            case "smoke":        return context.getString(R.string.smoke);
            case "haze":         return context.getString(R.string.haze);
            case "dust":         return context.getString(R.string.dust);
            case "fog":          return context.getString(R.string.fog);
            case "sand":         return context.getString(R.string.sand);
            case "ash":          return context.getString(R.string.ash);
            case "squall":       return context.getString(R.string.squall);
            case "tornado":      return context.getString(R.string.tornado);
            case "clear":        return context.getString(R.string.clear);
            case "clouds":       return context.getString(R.string.clouds);
            default:             return main;
        }
    }

    // Legacy method - Get the weather description from the string variable
    public static String getWeatherDescriptionString(Context context, String description) {
        if (description == null) return "";

        switch (description.toLowerCase()) {
            case "clear sky": return context.getString(R.string.clear_sky);
            case "few clouds": return context.getString(R.string.few_clouds);
            case "scattered clouds": return context.getString(R.string.scattered_clouds);
            case "broken clouds": return context.getString(R.string.broken_clouds);
            case "overcast clouds": return context.getString(R.string.overcast_clouds);
            case "light rain": return context.getString(R.string.light_rain);
            case "moderate rain": return context.getString(R.string.moderate_rain);
            case "heavy intensity rain": return context.getString(R.string.heavy_intensity_rain);
            case "very heavy rain": return context.getString(R.string.very_heavy_rain);
            case "extreme rain": return context.getString(R.string.extreme_rain);
            case "freezing rain": return context.getString(R.string.freezing_rain);
            case "light intensity shower rain": return context.getString(R.string.light_intensity_shower_rain);
            case "shower rain": return context.getString(R.string.shower_rain);
            case "heavy intensity shower rain": return context.getString(R.string.heavy_intensity_shower_rain);
            case "ragged shower rain": return context.getString(R.string.ragged_shower_rain);
            case "light snow": return context.getString(R.string.light_snow);
            case "snow": return context.getString(R.string.snow);
            case "heavy snow": return context.getString(R.string.heavy_snow);
            case "sleet": return context.getString(R.string.sleet);
            case "light shower sleet": return context.getString(R.string.light_shower_sleet);
            case "shower sleet": return context.getString(R.string.shower_sleet);
            case "light rain and snow": return context.getString(R.string.light_rain_and_snow);
            case "rain and snow": return context.getString(R.string.rain_and_snow);
            case "light shower snow": return context.getString(R.string.light_shower_snow);
            case "shower snow": return context.getString(R.string.shower_snow);
            case "heavy shower snow": return context.getString(R.string.heavy_shower_snow);
            case "mist": return context.getString(R.string.mist);
            case "smoke": return context.getString(R.string.smoke);
            case "haze": return context.getString(R.string.haze);
            case "sand/ dust whirls": return context.getString(R.string.sand_dust_whirls);
            case "fog": return context.getString(R.string.fog);
            case "sand": return context.getString(R.string.sand);
            case "dust": return context.getString(R.string.dust);
            case "volcanic ash": return context.getString(R.string.volcanic_ash);
            case "squalls": return context.getString(R.string.squalls);
            case "tornado": return context.getString(R.string.tornado);
            case "light intensity drizzle": return context.getString(R.string.light_intensity_drizzle);
            case "drizzle": return context.getString(R.string.drizzle);
            case "heavy intensity drizzle": return context.getString(R.string.heavy_intensity_drizzle);
            case "light intensity drizzle rain": return context.getString(R.string.light_intensity_drizzle_rain);
            case "drizzle rain": return context.getString(R.string.drizzle_rain);
            case "heavy intensity drizzle rain": return context.getString(R.string.heavy_intensity_drizzle_rain);
            case "shower rain and drizzle": return context.getString(R.string.shower_rain_and_drizzle);
            case "heavy shower rain and drizzle": return context.getString(R.string.heavy_shower_rain_and_drizzle);
            case "shower drizzle": return context.getString(R.string.shower_drizzle);
            case "thunderstorm": return context.getString(R.string.thunderstorm);
            case "thunderstorm with light rain": return context.getString(R.string.thunderstorm_with_light_rain);
            case "thunderstorm with rain": return context.getString(R.string.thunderstorm_with_rain);
            case "thunderstorm with heavy rain": return context.getString(R.string.thunderstorm_with_heavy_rain);
            case "light thunderstorm": return context.getString(R.string.light_thunderstorm);
            case "heavy thunderstorm": return context.getString(R.string.heavy_thunderstorm);
            case "ragged thunderstorm": return context.getString(R.string.ragged_thunderstorm);
            case "thunderstorm with light drizzle": return context.getString(R.string.thunderstorm_with_light_drizzle);
            case "thunderstorm with drizzle": return context.getString(R.string.thunderstorm_with_drizzle);
            case "thunderstorm with heavy drizzle": return context.getString(R.string.thunderstorm_with_heavy_drizzle);
            default: return description;
        }
    }

    public static String getNormalizedIcon(String icon) {
        String result = "";
        switch (icon) {
            case "01d":
            case "01n":
                result = "soleado";
                break;
            case "02d":
            case "02n":
                result = "nubes_con_sol";
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
                result = "nieve";
                break;
            default:
                result = "a" + icon;
        }

        return result;
    }

    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        str = str.trim();
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase()
                + str.substring(1).toLowerCase();
    }

}
