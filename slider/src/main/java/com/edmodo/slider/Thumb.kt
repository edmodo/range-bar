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

package com.edmodo.slider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.TypedValue

/**
 * Represents a thumb in the RangeBar slider. This is the handle for the slider
 * that is pressed and slid.
 */
internal class Thumb(ctx: Context, private val mY: Float, private var indicatorColor: Int, thumbRadiusDP: Float) {

    // Radius (in pixels) of the touch area of the thumb.
    private val mTargetRadiusPx: Float

    // Variables to store half the width/height for easier calculation.

    val halfWidth: Float
    val halfHeight: Float

    private val mHalfWidthPressed: Float
    private val mHalfHeightPressed: Float

    // Indicates whether this thumb is currently pressed and active.
    var isPressed = false
        private set

    // The current x-position of the thumb in the parent view.
    var x: Float = 0.toFloat()

    // mPaint to draw the thumbs if attributes are selected
    private var mPaintNormal: Paint? = null
    private var mPaintPressed: Paint? = null

    // Radius of the new thumb if selected
    private var mThumbRadiusPx: Float = 0.toFloat()

    init {

        val res = ctx.resources

        // If one of the attributes are set, but the others aren't, set the
        // attributes to default
        if (thumbRadiusDP == -1f)
            mThumbRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    DEFAULT_THUMB_RADIUS_DP,
                    res.displayMetrics)
        else
            mThumbRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    thumbRadiusDP,
                    res.displayMetrics)

        if (indicatorColor == -1)
            indicatorColor = DEFAULT_THUMB_COLOR_NORMAL

        // Creates the paint and sets the Paint values
        mPaintNormal = Paint()
        mPaintNormal!!.color = indicatorColor
        mPaintNormal!!.isAntiAlias = true

        mPaintPressed = Paint()
        mPaintPressed!!.color = indicatorColor
        mPaintPressed!!.alpha = 50
        mPaintPressed!!.isAntiAlias = true

        halfWidth = mThumbRadiusPx
        halfHeight = mThumbRadiusPx

        mHalfWidthPressed = mThumbRadiusPx
        mHalfHeightPressed = mThumbRadiusPx

        // Sets the minimum touchable area, but allows it to expand based on
        // image size
        val targetRadius = Math.max(MINIMUM_TARGET_RADIUS_DP, thumbRadiusDP).toInt()

        mTargetRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, targetRadius.toFloat(), res.displayMetrics)

        x = halfWidth
    }

    fun press() {
        isPressed = true
    }

    fun release() {
        isPressed = false
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
    fun isInTargetZone(x: Float, y: Float) = Math.abs(x - this.x) <= mTargetRadiusPx && Math.abs(y - mY) <= mTargetRadiusPx

    /**
     * Draws this thumb on the provided canvas.
     *
     * @param canvas Canvas to draw on; should be the Canvas passed into {#link
     * View#onDraw()}
     */
    fun draw(canvas: Canvas) {

        // Otherwise use a circle to display.
        if (isPressed) {
            canvas.drawCircle(x, mY, mThumbRadiusPx * INDICATOR_PRESSED_SCALE_OUTSIDE, mPaintPressed!!)
            canvas.drawCircle(x, mY, mThumbRadiusPx * INDICATOR_PRESSED_SCALE, mPaintNormal!!)
        } else {
            canvas.drawCircle(x, mY, mThumbRadiusPx, mPaintNormal!!)
        }
    }

    companion object {
        // The radius (in dp) of the touchable area around the thumb. We are basing
        // this value off of the recommended 48dp Rhythm. See:
        // http://developer.android.com/design/style/metrics-grids.html#48dp-rhythm
        private const val MINIMUM_TARGET_RADIUS_DP = 24f

        // Sets the default values for radius, normal, pressed if circle is to be
        // drawn but no value is given.
        private const val DEFAULT_THUMB_RADIUS_DP = 14f

        // Corresponds to android.R.color.holo_blue_light.
        private const val DEFAULT_THUMB_COLOR_NORMAL = -0xcc4a1b

        private const val INDICATOR_PRESSED_SCALE_OUTSIDE = 2.5f
        private const val INDICATOR_PRESSED_SCALE = 1.2f
    }
}
