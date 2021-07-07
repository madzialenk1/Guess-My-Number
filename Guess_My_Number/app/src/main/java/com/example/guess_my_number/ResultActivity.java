package com.example.guess_my_number;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {

    TextView resultLabel;
    Button dismissButton;
    Boolean won;


    public ResultActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultLabel = findViewById(R.id.resultLabel);
        setWon(false);
        manageIntent();
        setDismissButton();
    }

    public void setDismissButton() {

        dismissButton = findViewById((R.id.dismissButton));
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (won) {
                    Intent mIntent = new Intent();
                    mIntent.putExtra("won", true);
                    setResult(RESULT_OK, mIntent);
                }
                finish();
            }
        });

    }

    public void manageIntent() {
        Intent intent = getIntent();
        int guessedNumber = intent.getIntExtra(MainActivity.EXTRA_NUMBER, 0);
        int generatedNumber = intent.getIntExtra(MainActivity.EXTRA_RANDOM, 0);
        int min = intent.getIntExtra(MainActivity.EXTRA_MIN, 0);
        int max = intent.getIntExtra(MainActivity.EXTRA_MAX, 0);

        if (guessedNumber > generatedNumber) {
            double range = (double) (guessedNumber - generatedNumber + 1) / (double) (max - generatedNumber + 1);
            int percentageOfColor = (int) (range * 255);
            int RGB = android.graphics.Color.argb(percentageOfColor, 220, 20, 60);
            this.findViewById(android.R.id.content).setBackgroundColor(RGB);
            resultLabel.setText("Your guess of " + guessedNumber + " is TOO HIGH");
            setWon(false);

        } else if (guessedNumber == generatedNumber) {
            this.findViewById(android.R.id.content).setBackgroundColor(Color.WHITE);
            resultLabel.setText("Excellent!" + guessedNumber + " is correct!");
            setWon(true);

        } else {
            double range = (double) (guessedNumber - min + 1) / (double) (generatedNumber - min + 1);
            int percentageOfColor = (int) ((1 - range) * 255);
            int RGB = android.graphics.Color.argb(percentageOfColor, 47, 140, 215);
            this.findViewById(android.R.id.content).setBackgroundColor(RGB);
            resultLabel.setText("Your guess of " + guessedNumber + " is TOO LOW");
            setWon(false);
        }

    }

    public void setWon(boolean won) {
        this.won = won;
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(ResultActivity.this, getResources().getString(R.string.dismiss_button), Toast.LENGTH_SHORT).show();

    }
}