package com.example.guess_my_number;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_NUMBER = "EXTRA_NUMBER";
    public static final String EXTRA_RANDOM = "EXTRA_RANDOM";
    public static final String EXTRA_MIN = "EXTRA_MIN";
    public static final String EXTRA_MAX = "EXTRA_MAX";
    private static final int REQUEST_CODE = 101;

    RadioGroup radioGroup;
    Button generateButton, evaluateButton, hintButton;
    TextView scoreLabel;
    EditText upperBoundText, lowerBoundText, numberGuessedText;
    SeekBar lowerBoundBar, upperBoundBar;
    int randomNum, attempts;

    ConstraintLayout layout;


    long delay = 1000;
    long last_text_edit = 0;
    Handler handler;
    boolean lowerEdited;
    int upperNumber = 1000;
    int lowerNumber = 0;

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {

                try {

                    lowerNumber = Integer.parseInt(lowerBoundText.getText().toString());
                } catch (Exception e) {

                }

                try {
                    upperNumber = Integer.parseInt(upperBoundText.getText().toString());
                } catch (Exception e){

                }

                if (getLowerEdited() == false) {
                    try {

                        if (upperNumber < lowerNumber) {
                            upperBoundBar.setProgress(lowerNumber);
                            upperBoundText.setText(String.valueOf(lowerNumber));
                        } else if (upperNumber > 1000) {
                            upperBoundText.setText("1000");
                        }  else {
                            upperBoundBar.setProgress(upperNumber);
                        }
                        try {
                            int min = Integer.parseInt(lowerBoundText.getText().toString());
                            int score = upperNumber - min;
                            scoreLabel.setText(String.valueOf(score));

                        } catch (Exception e) {

                        }
                    } catch (Exception e) {
                        upperBoundBar.setProgress(0);
                    }
                } else {
                    try {

                        if (upperNumber < lowerNumber) {
                            System.out.println(upperNumber);
                            lowerBoundBar.setProgress(upperNumber);
                            lowerBoundText.setText(String.valueOf(upperNumber));
                        }
                        else if (lowerNumber > 1000) {
                            lowerBoundText.setText("1000");
                        }
                        else {
                            System.out.println(lowerNumber);
                            lowerBoundBar.setProgress(lowerNumber);
                        }
                    } catch (Exception e) {

                    }

                }
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.mainLayout);
        radioGroup = findViewById(R.id.rbHint);
        scoreLabel = findViewById(R.id.scoreNumber);
        numberGuessedText = findViewById(R.id.numberEdit);
        numberGuessedText.setEnabled(false);
        handler = new Handler();

        setHintButton();
        setGenerateButton();
        setUpperBoundText();
        setEvaluateButton();
        setLowerBoundBar();
        setLowerBoundText();
        setUpperBoundBar();

        setLowerEdited(true);

    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getAttempts() {
        return attempts;
    }

    public boolean getLowerEdited(){
        return lowerEdited;
    }

    public void setLowerEdited(boolean lowerEdited){
        this.lowerEdited = lowerEdited;
    }

    public void setLowerBoundText() {

        lowerBoundText = findViewById(R.id.lowerBoundEdit);

        lowerBoundText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(input_finish_checker);
                setLowerEdited(true);

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                } else {

                }

            }
        });
    }

    public void setUpperBoundBar() {

        upperBoundBar = findViewById(R.id.upperSeek);
        upperBoundBar.setMax(1000);

        upperBoundBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                upperBoundText.setText(String.valueOf(seekBar.getProgress()));
            }
        });
    }

    public void setLowerBoundBar() {

        lowerBoundBar = findViewById(R.id.lowerSeek);
        lowerBoundBar.setMax(1000);

        lowerBoundBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lowerBoundText.setText(String.valueOf(seekBar.getProgress()));
            }
        });
    }

    public void setUpperBoundText() {
        upperBoundText = findViewById(R.id.upperBoundEdit);

        upperBoundText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(input_finish_checker);
                setLowerEdited(false);

            }


            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                } else {

                }
            }
        });
    }

    public void setEvaluateButton() {
        evaluateButton = findViewById(R.id.evaluateButton);
        evaluateButton.setEnabled(false);

        evaluateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int min = Integer.parseInt(lowerBoundText.getText().toString());
                    int max = Integer.parseInt(upperBoundText.getText().toString());
                    int guessedNumber = Integer.parseInt(numberGuessedText.getText().toString());

                    int score = Integer.parseInt(scoreLabel.getText().toString());
                    if (guessedNumber == randomNum) {
                        int newScore = score + 5;
                        scoreLabel.setText(String.valueOf(newScore));
                        setAttempts(getAttempts() + 1);
                        openSecondActivity();
                    } else if (guessedNumber < min || guessedNumber > max) {
                        Toast.makeText(MainActivity.this, "Your number is not in the range [" + min + "," + max + "]!", Toast.LENGTH_SHORT).show();

                    } else {
                        setAttempts(getAttempts() + 1);
                        String points = getPoints(1);
                        scoreLabel.setText(points);
                        openSecondActivity();
                    }


                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.number_first), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    public void createSnackbar(int points) {
        Snackbar snackbar = Snackbar
                .make(layout, "It will cost you " + points + " points!", Snackbar.LENGTH_LONG)
                .setAction(getResources().getString(R.string.want), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        manageHint();
                    }
                });

        snackbar.show();

    }

    public int points() {
        int radioBtnID = radioGroup.getCheckedRadioButtonId();
        switch (radioBtnID) {
            case 2131230831:

                return 1;

            case 2131230933:

                return 10;

            case 2131230826:

                return 5;

            case 2131230825:
                return 3;
        }
        return 0;
    }

    public void manageHint() {

        int radioBtnID = radioGroup.getCheckedRadioButtonId();

        switch (radioBtnID) {
            case 2131230831:
                getDivisibilityHint();
                break;
            case 2131230825:
                getDigitProductHint();
                break;
            case 2131230826:
                getDigitSumHint();
                break;
            case 2131230933:
                getPrimeNumberHint();
                break;

        }
    }

    public void setRandomNum(int randomNum) {
        this.randomNum = randomNum;
    }

    public void setGenerateButton() {
        generateButton = findViewById(R.id.buttonGenerate);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int min = Integer.parseInt(lowerBoundText.getText().toString());
                    int max = Integer.parseInt((upperBoundText.getText().toString()));
                    setRandomNum(ThreadLocalRandom.current().nextInt(min, max + 1));
                    System.out.println("wchodzi ");
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.secret_number), Toast.LENGTH_SHORT).show();
                    evaluateButton.setEnabled(true);
                    hintButton.setEnabled(true);
                    numberGuessedText.setEnabled(true);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.bounds), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setHintButton() {

        hintButton = findViewById(R.id.hintButton);
        hintButton.setEnabled(false);

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.hint_first), Toast.LENGTH_SHORT).show();
                } else {
                    createSnackbar(points());

                }

            }
        });
    }

    public String getPoints(int minusPoints) {

        int score = Integer.parseInt(scoreLabel.getText().toString());
        int points = score - minusPoints;
        return String.valueOf(points);
    }

    public int getRandomNum() {
        return randomNum;
    }

    public void getDivisibilityHint() {

        String points = getPoints(1);
        scoreLabel.setText(points);
        int max = Integer.parseInt(upperBoundText.getText().toString()) - 1;
        boolean isDivided = false;

        while (isDivided == false) {
            int random = ThreadLocalRandom.current().nextInt(2, max);
            if (getRandomNum() % random == 0) {
                isDivided = true;
                Toast.makeText(this, getResources().getString(R.string.number_divided) + random, Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void getPrimeNumberHint() {
        String points = getPoints(10);
        scoreLabel.setText(points);
        boolean isItPrime = true;

        if (getRandomNum() <= 1) {
            isItPrime = false;
        } else {
            for (int i = 2; i <= getRandomNum() / 2; i++) {
                if ((getRandomNum() % i) == 0) {
                    isItPrime = false;
                    break;
                }
            }
        }
        if (isItPrime == true) {
            Toast.makeText(this, getResources().getString(R.string.number_generated_is_prime), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.number_generated_is_not_prime), Toast.LENGTH_SHORT).show();
        }


    }

    public void getDigitSumHint() {
        String points = getPoints(5);
        scoreLabel.setText(points);
        int n, sum = 0;
        int m = getRandomNum();
        while (m > 0) {
            n = m % 10;
            sum = sum + n;
            m = m / 10;
        }

        Toast.makeText(this, getResources().getString(R.string.digit) + sum, Toast.LENGTH_SHORT).show();

    }

    public void getDigitProductHint() {
        String points = getPoints(3);
        scoreLabel.setText(points);
        int product = 1;
        int temporary = getRandomNum();

        while (temporary != 0) {
            product = product * (temporary % 10);
            temporary = temporary / 10;
            System.out.println(product);
            System.out.println(temporary);
        }

        Toast.makeText(this, getResources().getString(R.string.digit_product) + product, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE && data != null) {
                Toast.makeText(this, getResources().getString(R.string.guessed_number) + attempts + getResources().getString(R.string.score) + scoreLabel.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void openSecondActivity() {
        Intent intent = new Intent(this, ResultActivity.class);
        int guessedNumber = Integer.parseInt(numberGuessedText.getText().toString());
        int min = Integer.parseInt(lowerBoundText.getText().toString());
        int max = Integer.parseInt((upperBoundText.getText().toString()));


        intent.putExtra(EXTRA_NUMBER, guessedNumber);
        intent.putExtra(EXTRA_RANDOM, getRandomNum());
        intent.putExtra(EXTRA_MIN, min);
        intent.putExtra(EXTRA_MAX, max);
        startActivityForResult(intent, REQUEST_CODE);
    }
}