package com.edmodo.rangebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;

/**
 * Created by jayeshthadani on 7/28/15.
 */
class VerticalBar {

    // Member Variables ////////////////////////////////////////////////////////

    private final Paint mPaint;

    // Top-coordinate of the vertical bar.
    private final float mTopY;
    private final float mBottomY;
    private final float mX;

    private int mNumSegments;
    private float mTickDistance;
    private final float mTickHeight;
    private final float mTickStartX;
    private final float mTickEndX;
    private final int mTickMarkStep;

    VerticalBar(Context ctx, float x, float y, float length, int tickCount,
                float tickHeightDP,
                float BarWeight,
                int BarColor, int tickMarkStep){

        mTopY = y;
        mBottomY = mTopY + length;
        mX = x;
        mTickMarkStep = tickMarkStep;

        mNumSegments = tickCount - 1;
        mTickDistance = length / mNumSegments;
        mTickHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                tickHeightDP,
                ctx.getResources().getDisplayMetrics());

        mTickStartX = mX - mTickHeight / 2f;
        mTickEndX = mX + mTickHeight / 2f;

        // Initialize the paint.
        mPaint = new Paint();
        mPaint.setColor(BarColor);
        mPaint.setStrokeWidth(BarWeight);
        mPaint.setAntiAlias(true);

    }


    // Package-Private Methods /////////////////////////////////////////////////

    /**
     * Draws the bar on the given Canvas.
     *
     * @param canvas Canvas to draw on; should be the Canvas passed into {#link
     *            View#onDraw()}
     */
    void draw(Canvas canvas) {

        canvas.drawLine(mX, mBottomY, mX, mTopY, mPaint);

        drawTicks(canvas);
    }

    /**
     * Draws the tick marks on the bar.
     *
     * @param canvas Canvas to draw on; should be the Canvas passed into {#link
     *            View#onDraw()}
     */
    private void drawTicks(Canvas canvas) {

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);


        // Loop through and draw each tick (except final tick).
        for (int i = 0; i < mNumSegments; i++) {
            final float y = mBottomY - (i * mTickDistance );
            float tickStartX = 0; float tickEndX = 0;
            if (i == 0 ){
                tickStartX = mTickStartX - 40;
                tickEndX = mTickEndX + 40;
                canvas.drawText(String.valueOf(i * mTickMarkStep) , tickEndX + 10, y + 10, paint);
            } else{
                tickStartX = mTickStartX;
                tickEndX = mTickEndX;
                if(i % mTickMarkStep == 0){
                    canvas.drawText(String.valueOf((i / mTickMarkStep ) * mTickMarkStep), tickEndX + 50, y + 25, paint);
                }

            }

            canvas.drawLine(tickStartX, y, tickEndX, y, mPaint);
        }
        // Draw final tick. We draw the final tick outside the loop to avoid any
        // rounding discrepancies.

        // drawLine on TopY -- starting line
        canvas.drawLine(mTickStartX -40, mTopY , mTickEndX + 40, mTopY, mPaint);

        // drawLine on BottomY -- end line
        canvas.drawLine(mTickStartX -40, mBottomY , mTickEndX + 40, mBottomY, mPaint);

        canvas.drawText(String.valueOf(mNumSegments ), mTickEndX + 40, mTopY + 10, paint);
    }

    /**
     * Gets the y-coordinate of the nearest tick to the given x-coordinate.
     *
     * @return the y-coordinate of the nearest tick
     */
    float getNearestTickCoordinate(Thumb thumb) {

        final int nearestTickIndex = getNearestTickIndex(thumb);

        final float nearestTickCoordinate = mTopY + (nearestTickIndex * mTickDistance);

        return nearestTickCoordinate;
    }

    /**
     * Gets the zero-based index of the nearest tick to the given thumb.
     *
     * @param thumb the Thumb to find the nearest tick for
     * @return the zero-based index of the nearest tick
     */
    int getNearestTickIndex(Thumb thumb) {

        final int nearestTickIndex = (int) ((thumb.getY() - mTopY + mTickDistance / 2f) / mTickDistance);

        return nearestTickIndex;
    }

    /**
     * Get the y-coordinate of the top edge of the bar.
     *
     * @return y-coordinate of the top edge of the bar.
     */
    float getTopY() {
        return mTopY;
    }


    /**
     * Get the y-coordinate of the bottom edge of the bar.
     *
     * @return y-coordinate of the bottom edge of the bar.
     */
    float getBottomY() {
        return mBottomY;
    }
}
