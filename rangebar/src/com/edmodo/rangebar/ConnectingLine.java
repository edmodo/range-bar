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
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;

/**
 * Class representing the blue connecting line between the two thumbs.
 */
class ConnectingLine {
    private static final float DEFAULT_CONNECTING_LINE_WEIGHT_DP = 4;

    // Member Variables ////////////////////////////////////////////////////////

    private final Paint mPaint;

    private final float mY;
    private ColorStateList mConnectingLineColor;

    private int[] mState = {};

    // Constructor /////////////////////////////////////////////////////////////

    ConnectingLine(Context ctx, float y, float connectingLineWeight, ColorStateList connectingLineColor) {

        final Resources res = ctx.getResources();

        if (connectingLineWeight == -1) {
            connectingLineWeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    DEFAULT_CONNECTING_LINE_WEIGHT_DP,
                    res.getDisplayMetrics());
        }

        mConnectingLineColor = connectingLineColor;

        // Initialize the paint, set values
        mPaint = new Paint();
        mPaint.setColor(connectingLineColor.getDefaultColor());
        mPaint.setStrokeWidth(connectingLineWeight);

        mY = y;
    }

    void setWeight(float connectingLineWeight) {
        mPaint.setStrokeWidth(connectingLineWeight);
    }

    void setColor(ColorStateList colors) {
        mConnectingLineColor = colors;
        updateState();
    }

    void setState(int[] state) {
        mState = state;
        updateState();
    }

    private void updateState() {
        if (mConnectingLineColor.isStateful()) {
            mPaint.setColor(mConnectingLineColor.getColorForState(mState, mConnectingLineColor.getDefaultColor()));
        }
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
        canvas.drawLine(leftThumb.getX(), mY, rightThumb.getX(), mY, mPaint);
    }
}
