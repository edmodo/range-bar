
package com.example.rangebarsample;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;

public class HorizontalRatingBarActivity extends Activity {

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
    private RangeBar rangebar;

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

        // Change the text colors to the appropriate colors, and the text as
        // well
//        colorChanged(Component.BAR_COLOR, mBarColor);
//        colorChanged(Component.CONNECTING_LINE_COLOR, mConnectingLineColor);
//        colorChanged(Component.THUMB_COLOR_NORMAL, mThumbColorNormal);
//        colorChanged(Component.THUMB_COLOR_PRESSED, mThumbColorPressed);

        // Gets the RangeBar
        rangebar = (RangeBar) findViewById(R.id.rangebar1);

        // Gets the index value TextViews
//        final TextView leftIndexValue = (TextView) findViewById(R.id.leftIndexValue);
//        final TextView rightIndexValue = (TextView) findViewById(R.id.rightIndexValue);
        // Resets the index values every time the activity is changed
//        leftIndexValue.setText("" + rangebar.getLeftIndex());
//        rightIndexValue.setText("" + rangebar.getRightIndex());

        // Sets focus to the main layout, not the index text fields

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Removes title bar and sets content view
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.horizontalbar_activity);

        // Gets the RangeBar
        rangebar = (RangeBar) findViewById(R.id.rangebar1);
        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {
                Log.i(HorizontalRatingBarActivity.class.getName(), "right: " + rightThumbIndex + " left:" + leftThumbIndex) ;
            }
        });



        // Setting Index Values -------------------------------

        // Gets the index value TextViews
//        final EditText leftIndexValue = (EditText) findViewById(R.id.leftIndexValue);
//        final EditText rightIndexValue = (EditText) findViewById(R.id.rightIndexValue);

        // Sets the display values of the indices
//        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
//            @Override
//            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {
//                Log.i(MainActivity.class.getName(), "right: " +rightThumbIndex + " left:" + leftThumbIndex) ;
//                leftIndexValue.setText("" + leftThumbIndex);
//                rightIndexValue.setText("" + rightThumbIndex);
//            }
//        });

        // Sets the indices themselves upon input from the user
//        refreshButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                // Gets the String values of all the texts
//                String leftIndex = leftIndexValue.getText().toString();
//                String rightIndex = rightIndexValue.getText().toString();
//
//                // Catches any IllegalArgumentExceptions; if fails, should throw
//                // a dialog warning the user
//                try {
//                    if (!leftIndex.isEmpty() && !rightIndex.isEmpty())
//                    {
//                        int leftIntIndex = Integer.parseInt(leftIndex);
//                        int rightIntIndex = Integer.parseInt(rightIndex);
//                        rangebar.setThumbIndices(leftIntIndex, rightIntIndex);
//                    }
//                } catch (IllegalArgumentException e) {
//                }
//            }
//        });

        // Setting Number Attributes -------------------------------

        // Sets tickCount
//        final TextView tickCount = (TextView) findViewById(R.id.tickCount);
//        SeekBar tickCountSeek = (SeekBar) findViewById(R.id.tickCountSeek);
//        tickCountSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar tickCountSeek, int progress, boolean fromUser) {
//                try {
//                    rangebar.setTickCount(progress);
//                } catch (IllegalArgumentException e) {
//                }
//                tickCount.setText("tickCount = " + progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });

        // Sets tickHeight
//        final TextView tickHeight = (TextView) findViewById(R.id.tickHeight);
//        SeekBar tickHeightSeek = (SeekBar) findViewById(R.id.tickHeightSeek);
//        tickHeightSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar tickHeightSeek, int progress, boolean fromUser) {
//                rangebar.setTickHeight(progress);
//                tickHeight.setText("tickHeight = " + progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });

        // Sets barWeight
//        final TextView barWeight = (TextView) findViewById(R.id.barWeight);
//        SeekBar barWeightSeek = (SeekBar) findViewById(R.id.barWeightSeek);
//        barWeightSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//
//            @Override
//            public void onProgressChanged(SeekBar barWeightSeek, int progress, boolean fromUser) {
//                rangebar.setBarWeight(progress);
//                barWeight.setText("barWeight = " + progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });

        // Sets connectingLineWeight
//        final TextView connectingLineWeight = (TextView) findViewById(R.id.connectingLineWeight);
//        SeekBar connectingLineWeightSeek = (SeekBar) findViewById(R.id.connectingLineWeightSeek);
//        connectingLineWeightSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar connectingLineWeightSeek, int progress, boolean fromUser) {
//                rangebar.setConnectingLineWeight(progress);
//                connectingLineWeight.setText("connectingLineWeight = " + progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });

        // Sets thumbRadius
//        final TextView thumbRadius = (TextView) findViewById(R.id.thumbRadius);
//        SeekBar thumbRadiusSeek = (SeekBar) findViewById(R.id.thumbRadiusSeek);
//        thumbRadiusSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar thumbRadiusSeek, int progress, boolean fromUser) {
//                if (progress == 0) {
//                    rangebar.setThumbRadius(-1);
//                    thumbRadius.setText("thumbRadius = N/A");
//                }
//                else {
//                    rangebar.setThumbRadius(progress);
//                    thumbRadius.setText("thumbRadius = " + progress);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });

        // Setting Color Attributes---------------------------------

        // Sets barColor
//        barColor.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                initColorPicker(Component.BAR_COLOR, mBarColor, mBarColor);
//            }
//        });
//
//        // Sets connectingLineColor
//        connectingLineColor.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                initColorPicker(Component.CONNECTING_LINE_COLOR, mConnectingLineColor, mConnectingLineColor);
//            }
//        });
//
//        // Sets thumbColorNormal
//        thumbColorNormal.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                initColorPicker(Component.THUMB_COLOR_NORMAL, mThumbColorNormal, mThumbColorNormal);
//            }
//        });
//
//        // Sets thumbColorPressed
//        thumbColorPressed.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                initColorPicker(Component.THUMB_COLOR_PRESSED, mThumbColorPressed, mThumbColorPressed);
//            }
//        });
//
//        // Resets the thumbColors if selected
//        resetThumbColors.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                rangebar.setThumbColorNormal(-1);
//                rangebar.setThumbColorPressed(-1);
//
//                mThumbColorNormal = -1;
//                mThumbColorPressed = -1;
//
//                thumbColorNormal.setText("thumbColorNormal = N/A");
//                thumbColorPressed.setText("thumbColorPressed = N/A");
//                thumbColorNormal.setTextColor(HOLO_BLUE);
//                thumbColorPressed.setTextColor(HOLO_BLUE);
//            }
//        });

    }

    /**
     * Sets the changed color using the ColorPickerDialog.
     * 
     * @param component Component specifying which input is being used
     * @param newColor Integer specifying the new color to be selected.
     */
//    @Override
//    public void colorChanged(Component component, int newColor) {
//
//        String hexColor = String.format("#%06X", (0xFFFFFF & newColor));
//
//        switch (component)
//        {
//            case BAR_COLOR:
//                mBarColor = newColor;
//                rangebar.setBarColor(newColor);
//                final TextView barColorText = (TextView) findViewById(R.id.barColor);
//                barColorText.setText("barColor = " + hexColor);
//                barColorText.setTextColor(newColor);
//                break;
//
//            case CONNECTING_LINE_COLOR:
//                mConnectingLineColor = newColor;
//                rangebar.setConnectingLineColor(newColor);
//                final TextView connectingLineColorText = (TextView) findViewById(R.id.connectingLineColor);
//                connectingLineColorText.setText("connectingLineColor = " + hexColor);
//                connectingLineColorText.setTextColor(newColor);
//                break;
//
//            case THUMB_COLOR_NORMAL:
//                mThumbColorNormal = newColor;
//                rangebar.setThumbColorNormal(newColor);
//                final TextView thumbColorNormalText = (TextView) findViewById(R.id.thumbColorNormal);
//
//                if (newColor == -1) {
//                    thumbColorNormalText.setText("thumbColorNormal = N/A");
//                    thumbColorNormalText.setTextColor(HOLO_BLUE);
//                }
//                else {
//                    thumbColorNormalText.setText("thumbColorNormal = " + hexColor);
//                    thumbColorNormalText.setTextColor(newColor);
//                }
//                break;
//
//            case THUMB_COLOR_PRESSED:
//                mThumbColorPressed = newColor;
//                rangebar.setThumbColorPressed(newColor);
//                final TextView thumbColorPressedText = (TextView) findViewById(R.id.thumbColorPressed);
//
//                if (newColor == -1) {
//                    thumbColorPressedText.setText("thumbColorPressed = N/A");
//                    thumbColorPressedText.setTextColor(HOLO_BLUE);
//                }
//                else {
//                    thumbColorPressedText.setText("thumbColorPressed = " + hexColor);
//                    thumbColorPressedText.setTextColor(newColor);
//                }
//                break;
//            default:
//                break;
//        }
//    }

    /**
     * Sets the font on all TextViews in the ViewGroup. Searches recursively for
     * all inner ViewGroups as well. Just add a check for any other views you
     * want to set as well (EditText, etc.)
     */
    private void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Initiates the colorPicker from within a button function.
     * 
     * @param component Component specifying which input is being used
     * @param initialColor Integer specifying the initial color choice. *
     * @param defaultColor Integer specifying the default color choice.
     */
//    private void initColorPicker(Component component, int initialColor, int defaultColor)
//    {
//        ColorPickerDialog colorPicker = new ColorPickerDialog(this,
//                                                              this,
//                                                              component,
//                                                              initialColor,
//                                                              defaultColor);
//        colorPicker.show();
//
//    }

}
