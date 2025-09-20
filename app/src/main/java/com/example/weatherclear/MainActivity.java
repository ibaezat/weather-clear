package com.example.weatherclear;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Calendar;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    QuickSearchManager quickSearch;
    private Button continueToCity1;
    private Button continueToCity2;
    private Button continueToCity3;
    MediaPlayer closeSound;
    MediaPlayer openSound;

    private static final int START_NIGHT = 20;
    private static final int END_NIGHT = 6;

    private String[] backgrounds = {
            "background01",
            "background02",
            "background03",
            "background04",
            "background09",
            "background10",
            "background11",
            "background13",
            "background50"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quickSearch = new QuickSearchManager(this);

        RelativeLayout rootLayout = findViewById(R.id.rootLayout);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        Random rand = new Random();

        String resourceName;
        int index = rand.nextInt(backgrounds.length);

        if (hour >= START_NIGHT || hour < END_NIGHT) {
            resourceName = backgrounds[index] + 'n';
        } else {
            resourceName = backgrounds[index] + 'd';
        }

        int drawableResId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
        rootLayout.setBackgroundResource(drawableResId);

        this.continueToCity3 = findViewById(R.id.city3Button);
        this.continueToCity1 = findViewById(R.id.city1Button);
        this.continueToCity2 = findViewById(R.id.city2Button);
        this.openSound = MediaPlayer.create(this, R.raw.open);
        this.closeSound = MediaPlayer.create(this, R.raw.close);
        EditText inputCityName = findViewById(R.id.inputCityName);

        configureQuickSearchButtons();

        inputCityName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    MainActivity.this.openSound.start();
                    MainActivity.this.searchCity();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        MainActivity.this.closeSound.start();
        configureQuickSearchButtons();
    }

    @SuppressLint("SetTextI18n")
    public void configureQuickSearchButtons(){
        Set<String> cities = quickSearch.getCities();

        List<String> savedCityList = new ArrayList<>(cities);

        Button[] buttons = {
                continueToCity1,
                continueToCity2,
                continueToCity3
        };

        for (int i = 0; i < buttons.length; i++) {
            if (i < savedCityList.size()) {
                String cityName = savedCityList.get(i);
                buttons[i].setText(cityName);
                buttons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) throws IllegalStateException {
                        MainActivity.this.openSound.start();
                        MainActivity.this.openQuickSearchCity(cityName);
                    }
                });
                buttons[i].setBackgroundResource(R.drawable.fast_search_button);
            } else {
                buttons[i].setText(R.string.add_city_slot);
                buttons[i].setBackgroundResource(R.drawable.empty_quick_search_slot);
                buttons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText searchInput = findViewById(R.id.inputCityName);
                        searchInput.requestFocus();

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }
                });
            }
        }
    }

    public void openQuickSearchCity(String city) {
        Intent intentCity = new Intent(this, (Class<?>) CityWeather.class);
        intentCity.putExtra("android.intent.extra.TEXT", city);
        startActivity(intentCity);
    }

    public void searchCity() {
        Intent intentSearchCity = new Intent(this, (Class<?>) CityWeather.class);
        EditText textToPass = (EditText) findViewById(R.id.inputCityName);
        String cityName = textToPass.getText().toString();
        intentSearchCity.putExtra("android.intent.extra.TEXT", cityName);
        startActivity(intentSearchCity);
    }
}