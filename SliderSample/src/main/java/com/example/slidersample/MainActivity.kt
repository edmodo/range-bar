package com.example.slidersample

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.chili.slider.Slider

class MainActivity : Activity() {

    // Sets variables to save the colors of each attribute
    private var mBarColor = DEFAULT_BAR_COLOR
    private var mConnectingLineColor = DEFAULT_CONNECTING_LINE_COLOR
    private var mThumbColorNormal = DEFAULT_THUMB_COLOR_NORMAL
    private var mThumbColorPressed = DEFAULT_THUMB_COLOR_PRESSED

    // Initializes the RangeBar in the application
    private var rangebar: Slider? = null

    // Saves the state upon rotating the screen/restarting the activity
    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putInt("BAR_COLOR", mBarColor)
        bundle.putInt("CONNECTING_LINE_COLOR", mConnectingLineColor)
        bundle.putInt("THUMB_COLOR_NORMAL", mThumbColorNormal)
        bundle.putInt("THUMB_COLOR_PRESSED", mThumbColorPressed)
    }

    // Restores the state upon rotating the screen/restarting the activity
    override fun onRestoreInstanceState(bundle: Bundle) {
        super.onRestoreInstanceState(bundle)
        mBarColor = bundle.getInt("BAR_COLOR")
        mConnectingLineColor = bundle.getInt("CONNECTING_LINE_COLOR")
        mThumbColorNormal = bundle.getInt("THUMB_COLOR_NORMAL")
        mThumbColorPressed = bundle.getInt("THUMB_COLOR_PRESSED")

        // Gets the RangeBar
        rangebar = findViewById<View>(R.id.rangebar1) as Slider

        // Gets the index value TextViews
        val leftIndexValue = findViewById<View>(R.id.leftIndexValue) as TextView
        val rightIndexValue = findViewById<View>(R.id.rightIndexValue) as TextView
        // Resets the index values every time the activity is changed
        leftIndexValue.text = "" + rangebar!!.leftIndex
        rightIndexValue.text = "" + rangebar!!.rightIndex

        // Sets focus to the main layout, not the index text fields
        findViewById<View>(R.id.mylayout).requestFocus()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Removes title bar and sets content view
        setContentView(R.layout.activity_main)

        // Sets fonts for all

        // Gets the buttons references for the buttons
        val barColor = findViewById<View>(R.id.barColor) as Button
        val connectingLineColor = findViewById<View>(R.id.connectingLineColor) as Button
        val thumbColorNormal = findViewById<View>(R.id.thumbColorNormal) as Button
        val thumbColorPressed = findViewById<View>(R.id.thumbColorPressed) as Button
        val resetThumbColors = findViewById<View>(R.id.resetThumbColors) as Button
        val refreshButton = findViewById<View>(R.id.refresh) as Button

        // Sets initial colors for the Color buttons
        barColor.setTextColor(DEFAULT_BAR_COLOR)
        connectingLineColor.setTextColor(DEFAULT_CONNECTING_LINE_COLOR)
        thumbColorNormal.setTextColor(HOLO_BLUE)
        thumbColorPressed.setTextColor(HOLO_BLUE)

        // Gets the RangeBar
        rangebar = findViewById<View>(R.id.rangebar1) as Slider
        // Setting Index Values -------------------------------

        // Gets the index value TextViews
        val leftIndexValue = findViewById<View>(R.id.leftIndexValue) as EditText
        val rightIndexValue = findViewById<View>(R.id.rightIndexValue) as EditText

        // Sets the display values of the indices
        rangebar!!.setOnRangeBarChangeListener(object : Slider.OnSliderChangeListener {

            override fun onRelease(slider: Slider, leftIndicatorValue: Int, rightIndicatorValue: Int) {
                Toast.makeText(this@MainActivity, "Changed: $leftIndicatorValue $rightIndicatorValue", Toast.LENGTH_LONG).show()
            }

            override fun onIndexChange(slider: Slider, leftThumbIndex: Int, rightThumbIndex: Int) {

                leftIndexValue.setText("" + leftThumbIndex)
                rightIndexValue.setText("" + rightThumbIndex)
            }

        })

        rangebar?.minSliderValue = 100f
        rangebar?.maxSliderValue = 300f

        rangebar?.setThumbIndices(150, 250)


        // Sets the indices themselves upon input from the user
        refreshButton.setOnClickListener {
            // Gets the String values of all the texts
            val leftIndex = leftIndexValue.text.toString()
            val rightIndex = rightIndexValue.text.toString()

            // Catches any IllegalArgumentExceptions; if fails, should throw
            // a dialog warning the user
            try {
                if (!leftIndex.isEmpty() && !rightIndex.isEmpty()) {
                    val leftIntIndex = Integer.parseInt(leftIndex)
                    val rightIntIndex = Integer.parseInt(rightIndex)

                    rangebar?.setThumbIndices(leftIntIndex, rightIntIndex)

                }
            } catch (e: IllegalArgumentException) {
            }
        }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    companion object {

        // Corresponds to Color.LTGRAY
        private val DEFAULT_BAR_COLOR = -0x333334

        // Corresponds to android.R.color.holo_blue_light.
        private val DEFAULT_CONNECTING_LINE_COLOR = -0xcc4a1b
        private val HOLO_BLUE = -0xcc4a1b

        // Sets the initial values such that the image will be drawn
        private val DEFAULT_THUMB_COLOR_NORMAL = -1
        private val DEFAULT_THUMB_COLOR_PRESSED = -1
    }


}
