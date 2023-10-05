package com.example.mortgagepayment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MortgageDisplay extends AppCompatActivity {

    TextView textdisplay;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        textdisplay = (TextView) findViewById(R.id.amounttopay);
        button = findViewById(R.id.backbtn);

        Intent intent = getIntent();
        String emi = intent.getStringExtra("emi");

        textdisplay.setText("$" + emi);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }
}