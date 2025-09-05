package com.example.weatherclear;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Error extends AppCompatActivity {
    MediaPlayer exp;
    MediaPlayer zombie;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws IllegalStateException {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error);
        this.zombie = MediaPlayer.create(this, R.raw.zombie);
        this.exp = MediaPlayer.create(this, R.raw.exp);
        this.zombie.start();
        Button button = findViewById(R.id.error);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalStateException {
                Error.this.exp.start();
                Error.this.finish();
                Error.this.goHomeIntent();
            }
        });
    }

    public void goHomeIntent() {
        Intent intentGoHome = new Intent(this, (Class<?>) MainActivity.class);
        startActivity(intentGoHome);
    }
}