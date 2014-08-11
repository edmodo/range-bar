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
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
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

    private static final int DEFAULT_THUMB_IMAGE_NORMAL = R.drawable.seek_thumb;

    // Corresponds to android.R.color.holo_blue_light.
    private static final int DEFAULT_THUMB_COLOR_NORMAL = 0xff33b5e5;
    private static final int DEFAULT_THUMB_COLOR_PRESSED = 0xff33b5e5;

    private static final int[] STATE_PRESSED = { android.R.attr.state_pressed };
    private static final int[] STATE_NOT_PRESSED = { -android.R.attr.state_pressed };

    // Member Variables ////////////////////////////////////////////////////////

    // Radius (in pixels) of the touch area of the thumb.
    private final float mTargetRadiusPx;

    // The normal and pressed images to display for the thumbs.
    private Drawable mImageNormal;

    // Variables to store half the width/height for easier calculation.
    private float mHalfWidthNormal;
    private float mHalfHeightNormal;

    // Indicates whether this thumb is currently pressed and active.
    private boolean mIsPressed = false;

    // The y-position of the thumb in the parent view. This should not change.
    private final float mY;

    // The current x-position of the thumb in the parent view.
    private float mX;

    // Constructors ////////////////////////////////////////////////////////////

    Thumb(Context ctx,
            float y,
            int thumbColorNormal,
            int thumbColorPressed,
            float thumbRadiusDP,
            int thumbImageNormal) {

        final Resources res = ctx.getResources();

        if (thumbImageNormal == -1 && thumbColorNormal == -1) {
            thumbImageNormal = DEFAULT_THUMB_IMAGE_NORMAL;
        }

        if (thumbImageNormal != -1) {
            mImageNormal = res.getDrawable(thumbImageNormal).mutate();
        } else {
            // No drawable specified, try to infer a thumb drawable from normal and pressed colors.
            if (thumbColorNormal == -1) {
                thumbColorNormal = DEFAULT_THUMB_COLOR_NORMAL;
            }

            if (thumbColorPressed == -1) {
                thumbColorPressed = DEFAULT_THUMB_COLOR_PRESSED;
            }

            // Create a stateful drawable that contains pressed and unpressed oval shapes
            StateListDrawable sld = new StateListDrawable();

            ShapeDrawable sd = new ShapeDrawable(new OvalShape());
            sd.getPaint().setColor(thumbColorNormal);
            sld.addState(STATE_NOT_PRESSED, sd);

            sd = new ShapeDrawable(new OvalShape());
            sd.getPaint().setColor(thumbColorPressed);
            sld.addState(STATE_PRESSED, sd);

            mImageNormal = sld;
        }

        if (thumbRadiusDP == -1 && mImageNormal.getIntrinsicWidth() < 0) {
            // No radius was specified, and the drawable has no intrinsic size;
            // use the default
            thumbRadiusDP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    DEFAULT_THUMB_RADIUS_DP,
                    res.getDisplayMetrics());
        }

        if (thumbRadiusDP != -1) {
            // A known radius was requested, set the thumb's bounds to that
            Rect bounds = new Rect(0, 0, (int) (thumbRadiusDP * 2), (int) (thumbRadiusDP * 2));
            mImageNormal.setBounds(bounds);
            mHalfHeightNormal = mHalfWidthNormal = thumbRadiusDP;
        } else {
            // No radius specified, so use the drawable's intrinsic size
            mHalfWidthNormal = mImageNormal.getIntrinsicWidth() / 2f;
            mHalfHeightNormal = mImageNormal.getIntrinsicHeight() / 2f;
        }

        // Sets the minimum touchable area, but allows it to expand based on
        // image size
        int targetRadius = (int) Math.max(MINIMUM_TARGET_RADIUS_DP, thumbRadiusDP);

        mTargetRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                targetRadius,
                res.getDisplayMetrics());

        mX = mHalfWidthNormal;
        mY = y;
    }

    // Package-Private Methods /////////////////////////////////////////////////

    float getHalfWidth() {
        return mHalfWidthNormal;
    }

    float getHalfHeight() {
        return mHalfHeightNormal;
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

        mImageNormal.setState(STATE_PRESSED);
    }

    void release() {
        mIsPressed = false;

        mImageNormal.setState(STATE_NOT_PRESSED);
    }

    /**
     * Determines if the input coordinate is close enough to this thumb to
     * consider it a press.
     *
     * @param x the x-coordinate of the user touch
     * @param y the y-coordinate of the user touch
     * @return true if the coordinates are within this thumb's target area;
     * false otherwise
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
     * View#onDraw()}
     */
    void draw(Canvas canvas) {
        // If a bitmap is to be printed. Determined by thumbRadius attribute.
        int saveCount = canvas.save();

        canvas.translate(mX - mHalfWidthNormal, mY - mHalfHeightNormal);
        mImageNormal.draw(canvas);

        canvas.restoreToCount(saveCount);
    }
}
