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

package com.edmodo.rangebar

import android.graphics.Canvas
import android.graphics.Paint

/**
 * This class represents the underlying gray bar in the RangeBar (without the
 * thumbs).
 */
internal class Bar(val leftX: Float, private val mY: Float, length: Float, tickCount: Int, BarWeight: Float, BarColor: Int) {

    private val mPaint: Paint
    /**
     * Get the x-coordinate of the right edge of the bar.
     *
     * @return x-coordinate of the right edge of the bar
     */
    val rightX: Float = leftX + length

    private var mNumSegments: Int = 0
    private var mTickDistance: Float = 0.toFloat()

    init {

        mNumSegments = tickCount - 1
        mTickDistance = length / mNumSegments

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

        return ((thumb.x - leftX + mTickDistance / 2f) / mTickDistance).toInt()
    }

    /**
     * Set the number of ticks that will appear in the RangeBar.
     *
     * @param tickCount the number of ticks
     */
    fun setTickCount(tickCount: Int) {

        val barLength = rightX - leftX

        mNumSegments = tickCount - 1
        mTickDistance = barLength / mNumSegments
    }
}
