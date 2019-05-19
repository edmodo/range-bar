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

    // Initializes the RangeBar in the application
    private val slider: Slider by lazy { findViewById<Slider>(R.id.rangebar1) }

    // Restores the state upon rotating the screen/restarting the activity
    override fun onRestoreInstanceState(bundle: Bundle) {
        super.onRestoreInstanceState(bundle)

        // Gets the index value TextViews
        val leftIndexValue = findViewById<View>(R.id.leftIndexValue) as TextView
        val rightIndexValue = findViewById<View>(R.id.rightIndexValue) as TextView

        // Resets the index values every time the activity is changed
        leftIndexValue.text = "${slider.leftIndex}"
        rightIndexValue.text = "${slider.rightIndex}"

        // Sets focus to the main layout, not the index text fields
        findViewById<View>(R.id.mylayout).requestFocus()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Removes title bar and sets content view
        setContentView(R.layout.activity_main)

        // Sets fonts for all

        // Gets the buttons references for the buttons
        val refreshButton = findViewById<Button>(R.id.refresh)
        val refreshButtonMinMax = findViewById<Button>(R.id.refreshMinMax)

        val minEditText = findViewById<EditText>(R.id.minValue)
        val maxEditText = findViewById<EditText>(R.id.maxValue)

        // Gets the index value TextViews
        val leftIndexValue = findViewById<EditText>(R.id.leftIndexValue)
        val rightIndexValue = findViewById<EditText>(R.id.rightIndexValue)

        // Sets the display values of the indices
        slider.onSliderChangeListener = object : Slider.OnSliderChangeListener {

            override fun onRelease(slider: Slider, leftIndicatorValue: Int, rightIndicatorValue: Int) {
                Toast.makeText(this@MainActivity, "Changed: $leftIndicatorValue $rightIndicatorValue", Toast.LENGTH_LONG).show()
            }

            override fun onIndexChange(slider: Slider, leftThumbIndex: Int, rightThumbIndex: Int) {

                leftIndexValue.setText("$leftThumbIndex")
                rightIndexValue.setText("$rightThumbIndex")
            }

        }

        refreshButtonMinMax.setOnClickListener {
            val min = minEditText.text.toString().toFloat()
            val max = maxEditText.text.toString().toFloat()
            slider.minSliderValue = min
            slider.maxSliderValue = max
        }

        minEditText.setText("${slider.minSliderValue}")
        maxEditText.setText("${slider.maxSliderValue}")

        slider.setThumbIndices(150, 250)


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

                    slider.setThumbIndices(leftIntIndex, rightIntIndex)

                }
            } catch (e: IllegalArgumentException) {
            }
        }

    }

}
