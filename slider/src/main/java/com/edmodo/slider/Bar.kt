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

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.roundToInt

/**
 * This class represents the underlying gray bar in the RangeBar (without the
 * thumbs).
 */
internal class Bar(val leftX: Float, private val mY: Float, private val barLength: Float, steps: Int, BarWeight: Float, BarColor: Int) {

    private val mPaint: Paint
    /**
     * Get the x-coordinate of the right edge of the bar.
     *
     * @return x-coordinate of the right edge of the bar
     */
    val rightX: Float = leftX + barLength

    private var mDeltaMinMaxValue: Int = 0
    private var mTickDistance: Float = 0f

    init {

        mDeltaMinMaxValue = steps
        mTickDistance = barLength / mDeltaMinMaxValue

        // Initialize the paint.
        mPaint = Paint()
        mPaint.color = BarColor
        mPaint.strokeWidth = BarWeight
        mPaint.isAntiAlias = true
    }

    /**
     * Draws the bar on the given Canvas.
     *
     * @param canvas Canvas to draw on; should be the Canvas passed into {#link
     * View#onDraw()}
     */
    fun draw(canvas: Canvas) {
        canvas.drawLine(leftX, mY, rightX, mY, mPaint)
    }

    /**
     * Gets the x-coordinate of the nearest tick to the given x-coordinate.
     *
     * @param x the x-coordinate to find the nearest tick for
     * @return the x-coordinate of the nearest tick
     */
    fun getNearestTickCoordinate(thumb: Thumb): Float {

        val nearestTickIndex = getNearestTickIndex(thumb)

        return leftX + nearestTickIndex * mTickDistance
    }

    /**
     * Gets the zero-based index of the nearest tick to the given thumb.
     *
     * @param thumb the Thumb to find the nearest tick for
     * @return the zero-based index of the nearest tick
     */
    fun getNearestTickIndex(thumb: Thumb): Int {

        val leftPx = thumb.x - leftX

        return (mDeltaMinMaxValue.toFloat() * (leftPx / barLength)).roundToInt()
    }

}
