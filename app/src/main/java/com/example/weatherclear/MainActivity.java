package com.example.weatherclear;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder alertBuilder;
    private Button continueToChillan;
    private Button continueToConcepcion;
    private Button continueToSantiago;
    MediaPlayer ground;
    MediaPlayer mc;
    MediaPlayer mp;

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

        this.continueToSantiago = findViewById(R.id.santiagoButton);
        this.continueToChillan = findViewById(R.id.chillanButton);
        this.continueToConcepcion = findViewById(R.id.concepcionButton);
        this.mp = MediaPlayer.create(this, R.raw.open);
        this.mc = MediaPlayer.create(this, R.raw.close);
        this.ground = MediaPlayer.create(this, R.raw.ground);
        EditText inputCityName = findViewById(R.id.inputCityName);

        this.continueToChillan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalStateException {
                MainActivity.this.ground.start();
                MainActivity.this.openChillan();
            }
        });
        this.continueToSantiago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalStateException {
                MainActivity.this.ground.start();
                MainActivity.this.openSantiago();
            }
        });
        this.continueToConcepcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalStateException {
                MainActivity.this.ground.start();
                MainActivity.this.openConcepcion();
            }
        });

        inputCityName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    MainActivity.this.mp.start();
                    MainActivity.this.mc.start();
                    MainActivity.this.searchCity();
                    return true;
                }
                return false;
            }
        });
    }

    public void openChillan() {
        Intent intentChillan = new Intent(this, (Class<?>) CityWeather.class);
        intentChillan.putExtra("android.intent.extra.TEXT", "Chillan");
        startActivity(intentChillan);
    }

    public void openSantiago() {
        Intent intentSantiago = new Intent(this, (Class<?>) CityWeather.class);
        intentSantiago.putExtra("android.intent.extra.TEXT", "Santiago");
        startActivity(intentSantiago);
    }

    public void openConcepcion() {
        Intent intentConcepcion = new Intent(this, (Class<?>) CityWeather.class);
        intentConcepcion.putExtra("android.intent.extra.TEXT", "Concepcion");
        startActivity(intentConcepcion);
    }

    public void searchCity() {
        Intent intentSearchCity = new Intent(this, (Class<?>) CityWeather.class);
        EditText textToPass = (EditText) findViewById(R.id.inputCityName);
        String cityName = textToPass.getText().toString();
        intentSearchCity.putExtra("android.intent.extra.TEXT", cityName);
        startActivity(intentSearchCity);
    }
}