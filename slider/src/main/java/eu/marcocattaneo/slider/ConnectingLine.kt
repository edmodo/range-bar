/*
 * Copyright 2019, Marco Cattaneo
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

package eu.marcocattaneo.slider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.TypedValue

/**
 * Class representing the blue connecting line between the two thumbs.
 */
internal class ConnectingLine (ctx: Context, private val mY: Float, connectingLineWeight: Float, connectingLineColor: Int) {

    private val mPaint: Paint

    private val mConnectingLineWeight: Float

    init {

        val res = ctx.resources

        mConnectingLineWeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, connectingLineWeight, res.displayMetrics)

        // Initialize the paint, set values
        mPaint = Paint()
        mPaint.color = connectingLineColor
        mPaint.strokeWidth = mConnectingLineWeight
        mPaint.isAntiAlias = true
    }

    /**
     * Draw the connecting line between the two thumbs.
     *
     * @param canvas the Canvas to draw to
     * @param leftThumb the left thumb
     * @param rightThumb the right thumb
     */
    fun draw(canvas: Canvas, leftThumb: Thumb, rightThumb: Thumb) {
        canvas.drawLine(leftThumb.x, mY, rightThumb.x, mY, mPaint)
    }
}
