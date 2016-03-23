
package com.example.rangebarsample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.edmodo.rangebar.RangeBar;
import com.edmodo.rangebar.VerticalRangeBar;

public class VerticalRatingBarActivity extends Activity {

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
    private VerticalRangeBar verticalRangeBar;

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

        verticalRangeBar = (VerticalRangeBar) findViewById(R.id.verticalRangeBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Removes title bar and sets content view
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.verticalbar_activity);
        verticalRangeBar = (VerticalRangeBar) findViewById(R.id.verticalRangeBar);
        verticalRangeBar.setTickMarkStep(10);

        verticalRangeBar.setBarColor(this.getResources().getColor(R.color.seekbar_progress_color));

        // Sets the display values of the indices
        verticalRangeBar.setOnRangeBarChangeListener(new VerticalRangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(VerticalRangeBar v, int leftThumbIndex, int rightThumbIndex) {
                Log.i(VerticalRatingBarActivity.class.getName(), "right: " + rightThumbIndex + " left:" + leftThumbIndex) ;
            }
        });

        final EditText rightIndexValue = (EditText) findViewById(R.id.rightValueIndex);
        Button refreshButton = (Button) findViewById(R.id.refreshButton);
        rightIndexValue.setVisibility(View.INVISIBLE);
        refreshButton.setVisibility(View.INVISIBLE);

        // Sets the indices themselves upon input from the user
        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Gets the String values of all the texts
                String rightIndex = rightIndexValue.getText().toString();

                // Catches any IllegalArgumentExceptions; if fails, should throw
                // a dialog warning the user
                try {
                    if (!rightIndex.isEmpty()) {
                        verticalRangeBar.setFirstTouchEvent(true);
                        int rightIntIndex = Integer.parseInt(rightIndex);
                        verticalRangeBar.setThumbIndices(0, rightIntIndex);
                    }
                } catch (IllegalArgumentException e) {

                }
            }
        });

        Button clearButton = (Button) findViewById(R.id.clearButton);


        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verticalRangeBar.setThumbIndices(0, 0);
                verticalRangeBar.setFirstTouchEvent(false);
                verticalRangeBar.invalidate();
            }
        });


        verticalRangeBar.setFirstTouchEvent(true);
        int rightIntIndex = Integer.parseInt("4");
        //verticalRangeBar.setThumbIndices(0, rightIntIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
