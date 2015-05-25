/*
 * Copyright 2013, Edmodo, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License.
 * You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.edmodo.rangebar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;

/**
 * Represents a thumb in the RangeBar slider. This is the handle for the slider
 * that is pressed and slid.
 */
class Thumb {

    // Private Constants ///////////////////////////////////////////////////////

    // The radius (in dp) of the touchable area around the thumb. We are basing
    // this value off of the recommended 48dp Rhythm. See:
    // http://developer.android.com/design/style/metrics-grids.html#48dp-rhythm
    private static final float MINIMUM_TARGET_RADIUS_DP = 24;

    // Sets the default values for radius, normal, pressed if circle is to be
    // drawn but no value is given.
    private static final float DEFAULT_THUMB_RADIUS_DP = 14;

    // Corresponds to android.R.color.holo_blue_light.
    private static final int DEFAULT_THUMB_COLOR_NORMAL = 0xff33b5e5;
    private static final int DEFAULT_THUMB_COLOR_PRESSED = 0xff33b5e5;

    // Member Variables ////////////////////////////////////////////////////////

    // Radius (in pixels) of the touch area of the thumb.
    private final float mTargetRadiusPx;

    // The normal and pressed images to display for the thumbs.
    private final Bitmap mImageNormal;
    private final Bitmap mImagePressed;

    // Variables to store half the width/height for easier calculation.
    private final float mHalfWidthNormal;
    private final float mHalfHeightNormal;

    private final float mHalfWidthPressed;
    private final float mHalfHeightPressed;

    // Indicates whether this thumb is currently pressed and active.
    private boolean mIsPressed = false;

    // The y-position of the thumb in the parent view. This should not change.
    private final float mY;

    // The current x-position of the thumb in the parent view.
    private float mX;

    // mPaint to draw the thumbs if attributes are selected
    private Paint mPaintNormal;
    private Paint mPaintPressed;

    // Radius of the new thumb if selected
    private float mThumbRadiusPx;

    // Toggle to select bitmap thumbImage or not
    private boolean mUseBitmap;

    // Colors of the thumbs if they are to be drawn
    private int mThumbColorNormal;
    private int mThumbColorPressed;

    // Constructors ////////////////////////////////////////////////////////////

    Thumb(Context ctx,
          float y,
          int thumbColorNormal,
          int thumbColorPressed,
          float thumbRadiusPX,
          int thumbImageNormal,
          int thumbImagePressed) {

        final Resources res = ctx.getResources();

        mImageNormal = BitmapFactory.decodeResource(res, thumbImageNormal);
        mImagePressed = BitmapFactory.decodeResource(res, thumbImagePressed);

        // If any of the attributes are set, toggle bitmap off
        if (thumbRadiusPX == -1 && thumbColorNormal == -1 && thumbColorPressed == -1) {

            mUseBitmap = true;

            mHalfWidthNormal = mImageNormal.getWidth() / 2f;
            mHalfHeightNormal = mImageNormal.getHeight() / 2f;

            mHalfWidthPressed = mImagePressed.getWidth() / 2f;
            mHalfHeightPressed = mImagePressed.getHeight() / 2f;

        } else {

            mUseBitmap = false;

            // If one of the attributes are set, but the others aren't, set the
            // attributes to default
            if (thumbRadiusPX == -1)
                mThumbRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                                           DEFAULT_THUMB_RADIUS_DP,
                                                           res.getDisplayMetrics());
            else
                mThumbRadiusPx = thumbRadiusPX;

            if (thumbColorNormal == -1)
                mThumbColorNormal = DEFAULT_THUMB_COLOR_NORMAL;
            else
                mThumbColorNormal = thumbColorNormal;

            if (thumbColorPressed == -1)
                mThumbColorPressed = DEFAULT_THUMB_COLOR_PRESSED;
            else
                mThumbColorPressed = thumbColorPressed;

            // Creates the paint and sets the Paint values
            mPaintNormal = new Paint();
            mPaintNormal.setColor(mThumbColorNormal);
            mPaintNormal.setAntiAlias(true);

            mPaintPressed = new Paint();
            mPaintPressed.setColor(mThumbColorPressed);
            mPaintPressed.setAntiAlias(true);

            mHalfHeightPressed = mHalfWidthPressed = mHalfHeightNormal = mHalfWidthNormal = mThumbRadiusPx;
        }

        // Sets the minimum touchable area, but allows it to expand based on
        // image size
        int targetRadius = (int) Math.max(MINIMUM_TARGET_RADIUS_DP, thumbRadiusPX);

        mTargetRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                                    targetRadius,
                                                    res.getDisplayMetrics());

        mX = mHalfWidthNormal;
        mY = y;
    }

    // Package-Private Methods /////////////////////////////////////////////////

    float getWidth() {
        return mHalfWidthNormal * 2;
    }

    float getHalfWidth() {
        return mHalfWidthNormal;
    }

    float getHalfHeight() {
        return mHalfHeightNormal;
    }

    float getHeight() {
        return mHalfHeightNormal * 2;
    }

    void setX(float x) {
        mX = x;
    }

    float getX() {
        return mX;
    }

    boolean isPressed() {
        return mIsPressed;
    }

    void press() {
        mIsPressed = true;
    }

    void release() {
        mIsPressed = false;
    }

    /**
     * Determines if the input coordinate is close enough to this thumb to
     * consider it a press.
     *
     * @param x the x-coordinate of the user touch
     * @param y the y-coordinate of the user touch
     * @return true if the coordinates are within this thumb's target area;
     *         false otherwise
     */
    boolean isInTargetZone(float x, float y) {

        if (Math.abs(x - mX) <= mTargetRadiusPx && Math.abs(y - mY) <= mTargetRadiusPx) {
            return true;
        }
        return false;
    }

    /**
     * Draws this thumb on the provided canvas.
     *
     * @param canvas Canvas to draw on; should be the Canvas passed into {#link
     *            View#onDraw()}
     */
    void draw(Canvas canvas) {

        // If a bitmap is to be printed. Determined by thumbRadius attribute.
        if (mUseBitmap) {

            final Bitmap bitmap = (mIsPressed) ? mImagePressed : mImageNormal;

            if (mIsPressed) {
                final float topPressed = mY - mHalfHeightPressed;
                final float leftPressed = mX - mHalfWidthPressed;
                canvas.drawBitmap(bitmap, leftPressed, topPressed, null);
            } else {
                final float topNormal = mY - mHalfHeightNormal;
                final float leftNormal = mX - mHalfWidthNormal;
                canvas.drawBitmap(bitmap, leftNormal, topNormal, null);
            }

        } else {

            // Otherwise use a circle to display.
            if (mIsPressed)
                canvas.drawCircle(mX, mY, mThumbRadiusPx, mPaintPressed);
            else
                canvas.drawCircle(mX, mY, mThumbRadiusPx, mPaintNormal);
        }
    }
}
