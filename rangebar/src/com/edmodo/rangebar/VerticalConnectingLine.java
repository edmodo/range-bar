package com.edmodo.rangebar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;

/**
 * Created by jayeshthadani on 7/28/15.
 */
public class VerticalConnectingLine {

    // Member Variables ////////////////////////////////////////////////////////

    private final Paint mPaint;

    private final float mConnectingLineWeight;
    private final float mX;

    // Constructor /////////////////////////////////////////////////////////////

    VerticalConnectingLine(Context ctx, float x, float connectingLineWeight, int connectingLineColor) {
        final Resources res = ctx.getResources();

        mConnectingLineWeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                connectingLineWeight,
                res.getDisplayMetrics());

        // Initialize the paint, set values
        mPaint = new Paint();
        mPaint.setColor(connectingLineColor);
        mPaint.setStrokeWidth(mConnectingLineWeight);
        mPaint.setAntiAlias(true);

        this.mX = x;
    }


    // Package-Private Methods /////////////////////////////////////////////////

    /**
     * Draw the connecting line between the two thumbs.
     *
     * @param canvas the Canvas to draw to
     * @param leftThumb the left thumb
     * @param rightThumb the right thumb
     */
    void draw(Canvas canvas, Thumb leftThumb, Thumb rightThumb) {
        canvas.drawLine(mX, leftThumb.getY(), mX, rightThumb.getY(), mPaint);
    }

}
