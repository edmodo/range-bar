
package com.example.slidersample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import eu.marcocattaneo.slider.Slider;

public class MainActivity extends Activity {

    // Corresponds to Color.LTGRAY
    private static final int DEFAULT_BAR_COLOR = 0xffcccccc;

    // Corresponds to android.R.color.holo_blue_light.
    private static final int DEFAULT_CONNECTING_LINE_COLOR = 0xff33b5e5;
    private static final int HOLO_BLUE = 0xff33b5e5;

    // Sets the initial values such that the image will be drawn
    private static final int DEFAULT_THUMB_COLOR_NORMAL = -1;
    private static final int DEFAULT_THUMB_COLOR_PRESSED = -1;

    // Sets variables to save the colors of each attribute
    private int mBarColor = DEFAULT_BAR_COLOR;
    private int mConnectingLineColor = DEFAULT_CONNECTING_LINE_COLOR;
    private int mThumbColorNormal = DEFAULT_THUMB_COLOR_NORMAL;
    private int mThumbColorPressed = DEFAULT_THUMB_COLOR_PRESSED;

    // Initializes the RangeBar in the application
    private Slider rangebar;

    // Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("BAR_COLOR", mBarColor);
        bundle.putInt("CONNECTING_LINE_COLOR", mConnectingLineColor);
        bundle.putInt("THUMB_COLOR_NORMAL", mThumbColorNormal);
        bundle.putInt("THUMB_COLOR_PRESSED", mThumbColorPressed);
    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mBarColor = bundle.getInt("BAR_COLOR");
        mConnectingLineColor = bundle.getInt("CONNECTING_LINE_COLOR");
        mThumbColorNormal = bundle.getInt("THUMB_COLOR_NORMAL");
        mThumbColorPressed = bundle.getInt("THUMB_COLOR_PRESSED");

        // Gets the RangeBar
        rangebar = (Slider) findViewById(R.id.rangebar1);

        // Gets the index value TextViews
        final TextView leftIndexValue = (TextView) findViewById(R.id.leftIndexValue);
        final TextView rightIndexValue = (TextView) findViewById(R.id.rightIndexValue);
        // Resets the index values every time the activity is changed
        leftIndexValue.setText("" + rangebar.getLeftIndex());
        rightIndexValue.setText("" + rangebar.getRightIndex());

        // Sets focus to the main layout, not the index text fields
        findViewById(R.id.mylayout).requestFocus();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Removes title bar and sets content view
        setContentView(R.layout.activity_main);

        // Sets fonts for all

        // Gets the buttons references for the buttons
        final Button barColor = (Button) findViewById(R.id.barColor);
        final Button connectingLineColor = (Button) findViewById(R.id.connectingLineColor);
        final Button thumbColorNormal = (Button) findViewById(R.id.thumbColorNormal);
        final Button thumbColorPressed = (Button) findViewById(R.id.thumbColorPressed);
        final Button resetThumbColors = (Button) findViewById(R.id.resetThumbColors);
        final Button refreshButton = (Button) findViewById(R.id.refresh);
        
        // Sets initial colors for the Color buttons
        barColor.setTextColor(DEFAULT_BAR_COLOR);
        connectingLineColor.setTextColor(DEFAULT_CONNECTING_LINE_COLOR);
        thumbColorNormal.setTextColor(HOLO_BLUE);
        thumbColorPressed.setTextColor(HOLO_BLUE);

        // Gets the RangeBar
        rangebar = (Slider) findViewById(R.id.rangebar1);
        // Setting Index Values -------------------------------

        // Gets the index value TextViews
        final EditText leftIndexValue = (EditText) findViewById(R.id.leftIndexValue);
        final EditText rightIndexValue = (EditText) findViewById(R.id.rightIndexValue);

        // Sets the display values of the indices
        rangebar.setOnRangeBarChangeListener(new Slider.OnSliderChangeListener() {
            @Override
            public void onIndexChangeListener(Slider slider, int leftThumbIndex, int rightThumbIndex) {

                leftIndexValue.setText("" + leftThumbIndex);
                rightIndexValue.setText("" + rightThumbIndex);
            }
        });

        rangebar.setThumbIndices(150, 250);


        // Sets the indices themselves upon input from the user
        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Gets the String values of all the texts
                String leftIndex = leftIndexValue.getText().toString();
                String rightIndex = rightIndexValue.getText().toString();

                // Catches any IllegalArgumentExceptions; if fails, should throw
                // a dialog warning the user
                try {
                    if (!leftIndex.isEmpty() && !rightIndex.isEmpty()) {
                        int leftIntIndex = Integer.parseInt(leftIndex);
                        int rightIntIndex = Integer.parseInt(rightIndex);


                        rangebar.setThumbIndices(leftIntIndex, rightIntIndex);
                    }
                } catch (IllegalArgumentException e) {
                }
            }
        });

        // Setting Number Attributes -------------------------------
/*
        // Sets tickHeight
        final TextView tickHeight = (TextView) findViewById(R.id.tickHeight);
        SeekBar tickHeightSeek = (SeekBar) findViewById(R.id.tickHeightSeek);
        tickHeightSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar tickHeightSeek, int progress, boolean fromUser) {
                tickHeight.setText("tickHeight = " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Sets barWeight
        final TextView barWeight = (TextView) findViewById(R.id.barWeight);
        SeekBar barWeightSeek = (SeekBar) findViewById(R.id.barWeightSeek);
        barWeightSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar barWeightSeek, int progress, boolean fromUser) {
                rangebar.setBarWeight(progress);
                barWeight.setText("barWeight = " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Sets connectingLineWeight
        final TextView connectingLineWeight = (TextView) findViewById(R.id.connectingLineWeight);
        SeekBar connectingLineWeightSeek = (SeekBar) findViewById(R.id.connectingLineWeightSeek);
        connectingLineWeightSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar connectingLineWeightSeek, int progress, boolean fromUser) {
                rangebar.setConnectingLineWeight(progress);
                connectingLineWeight.setText("connectingLineWeight = " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Sets thumbRadius
        final TextView thumbRadius = (TextView) findViewById(R.id.thumbRadius);
        SeekBar thumbRadiusSeek = (SeekBar) findViewById(R.id.thumbRadiusSeek);
        thumbRadiusSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar thumbRadiusSeek, int progress, boolean fromUser) {
                if (progress == 0) {
                    rangebar.setThumbRadius(-1);
                    thumbRadius.setText("thumbRadius = N/A");
                }
                else {
                    rangebar.setThumbRadius(progress);
                    thumbRadius.setText("thumbRadius = " + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Setting Color Attributes---------------------------------

        // Sets barColor
        barColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initColorPicker(Component.BAR_COLOR, mBarColor, mBarColor);
            }
        });

        // Sets connectingLineColor
        connectingLineColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initColorPicker(Component.CONNECTING_LINE_COLOR, mConnectingLineColor, mConnectingLineColor);
            }
        });

        // Sets thumbColorNormal
        thumbColorNormal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initColorPicker(Component.THUMB_COLOR_NORMAL, mThumbColorNormal, mThumbColorNormal);
            }
        });

        // Sets thumbColorPressed
        thumbColorPressed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initColorPicker(Component.THUMB_COLOR_PRESSED, mThumbColorPressed, mThumbColorPressed);
            }
        });

        // Resets the thumbColors if selected
        resetThumbColors.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                rangebar.setThumbColorNormal(-1);
                rangebar.setThumbColorPressed(-1);

                mThumbColorNormal = -1;
                mThumbColorPressed = -1;

                thumbColorNormal.setText("thumbColorNormal = N/A");
                thumbColorPressed.setText("thumbColorPressed = N/A");
                thumbColorNormal.setTextColor(HOLO_BLUE);
                thumbColorPressed.setTextColor(HOLO_BLUE);
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
