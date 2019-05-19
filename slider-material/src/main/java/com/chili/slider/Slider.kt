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

package com.chili.slider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

/**
 * The RangeBar is a double-sided version of a [android.widget.SeekBar]
 * with discrete values. Whereas the thumb for the SeekBar can be dragged to any
 * position in the bar, the RangeBar only allows its thumbs to be dragged to
 * discrete positions (denoted by tick marks) in the bar. When released, a
 * RangeBar thumb will snap to the nearest tick mark.
 *
 *
 * Clients of the RangeBar can attach a
 * [Slider.OnSliderChangeListener] to be notified when the thumbs have
 * been moved.
 */
class Slider : View {

    // Instance variables for all of the customizable attributes
    private var mBarWeight = DEFAULT_BAR_WEIGHT_PX
    private var mBarColor = DEFAULT_BAR_COLOR
    private var mConnectingLineWeight = DEFAULT_CONNECTING_LINE_WEIGHT_PX
    private var mConnectingLineColor = DEFAULT_CONNECTING_LINE_COLOR

    private var mThumbRadiusDP = DEFAULT_THUMB_RADIUS_DP
    private var mIndicatorColor = DEFAULT_THUMB_COLOR_NORMAL

    // setTickCount only resets indices before a thumb has been pressed or a
    // setThumbIndices() is called, to correspond with intended usage
    private var mFirstSetTickCount = true

    private val mDefaultWidth = 500
    private val mDefaultHeight = 100

    private var mLeftThumb: Thumb? = null
    private var mRightThumb: Thumb? = null
    private var mBar: Bar? = null
    private var mConnectingLine: ConnectingLine? = null

    var minSliderValue = 0f
        set(value) {
            field = value
            invalidateSliderView()
        }

    var maxSliderValue = 0f
        set(value) {
            field = value
            invalidateSliderView()
        }

    var onSliderChangeListener: OnSliderChangeListener? = null
    /**
     * Gets the index of the left-most thumb.
     *
     * @return the 0-based index of the left thumb
     */
    var leftIndex = 0
        private set
    /**
     * Gets the index of the right-most thumb.
     *
     * @return the 0-based index of the right thumb
     */
    var rightIndex = 0
        private set

    /**
     * Get marginLeft in each of the public attribute methods.
     *
     * @param none
     * @return float marginLeft
     */
    private val marginLeft: Float
        get() = mLeftThumb?.halfWidth ?: 0f

    /**
     * Get yPos in each of the public attribute methods.
     *
     * @param none
     * @return float yPos
     */
    private val yPos: Float
        get() = height / 2f

    /**
     * Get barLength in each of the public attribute methods.
     *
     * @param none
     * @return float barLength
     */
    private val barLength: Float
        get() = width - 2 * marginLeft

    private var stepsSize: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        sliderInit(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        sliderInit(context, attrs)
    }

    override fun onSaveInstanceState(): Parcelable? {

        val bundle = Bundle()

        bundle.putParcelable("instanceState", super.onSaveInstanceState())

        bundle.putFloat("BAR_WEIGHT", mBarWeight)
        bundle.putInt("BAR_COLOR", mBarColor)
        bundle.putFloat("CONNECTING_LINE_WEIGHT", mConnectingLineWeight)
        bundle.putInt("CONNECTING_LINE_COLOR", mConnectingLineColor)

        bundle.putFloat("THUMB_RADIUS_DP", mThumbRadiusDP)
        bundle.putInt("THUMB_COLOR_NORMAL", mIndicatorColor)

        bundle.putInt("LEFT_INDEX", leftIndex)
        bundle.putInt("RIGHT_INDEX", rightIndex)

        bundle.putFloat("MIN_VALUE", minSliderValue)
        bundle.putFloat("MAX_VALUE", maxSliderValue)

        bundle.putBoolean("FIRST_SET_TICK_COUNT", mFirstSetTickCount)

        return bundle
    }

    private fun invalidateSliderView() {
        stepsSize = (maxSliderValue - minSliderValue).toInt()
        createBar()
        setThumbIndices(this.leftIndex, this.rightIndex)
    }

    override fun onRestoreInstanceState(state: Parcelable) {

        if (state is Bundle) {

            mBarWeight = state.getFloat("BAR_WEIGHT")
            mBarColor = state.getInt("BAR_COLOR")
            mConnectingLineWeight = state.getFloat("CONNECTING_LINE_WEIGHT")
            mConnectingLineColor = state.getInt("CONNECTING_LINE_COLOR")

            mThumbRadiusDP = state.getFloat("THUMB_RADIUS_DP")
            mIndicatorColor = state.getInt("THUMB_COLOR_NORMAL")

            leftIndex = state.getInt("LEFT_INDEX")
            rightIndex = state.getInt("RIGHT_INDEX")
            mFirstSetTickCount = state.getBoolean("FIRST_SET_TICK_COUNT")

            minSliderValue = state.getFloat("MIN_VALUE")
            maxSliderValue = state.getFloat("MAX_VALUE")

            setThumbIndices(leftIndex, rightIndex)

            super.onRestoreInstanceState(state.getParcelable("instanceState"))

        } else {

            super.onRestoreInstanceState(state)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val width: Int
        val height: Int

        // Get measureSpec mode and size values.
        val measureWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val measureHeightMode = MeasureSpec.getMode(heightMeasureSpec)
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measureHeight = MeasureSpec.getSize(heightMeasureSpec)

        // The RangeBar width should be as large as possible.
        width = when (measureWidthMode) {
            MeasureSpec.AT_MOST -> measureWidth
            MeasureSpec.EXACTLY -> measureWidth
            else -> mDefaultWidth
        }

        // The RangeBar height should be as small as possible.
        height = when (measureHeightMode) {
            MeasureSpec.AT_MOST -> Math.min(mDefaultHeight, measureHeight)
            MeasureSpec.EXACTLY -> measureHeight
            else -> mDefaultHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        super.onSizeChanged(w, h, oldw, oldh)

        val ctx = context

        // This is the initial point at which we know the size of the View.

        // Create the two thumb objects.
        val yPos = h / 2f
        mLeftThumb = Thumb(ctx,
                yPos,
                mIndicatorColor,
                mThumbRadiusDP)
        mRightThumb = Thumb(ctx,
                yPos,
                mIndicatorColor,
                mThumbRadiusDP)

        // Create the underlying bar.
        val marginLeft = mLeftThumb?.halfWidth ?: 0f
        val barLength = w - 2 * marginLeft
        mBar = Bar(marginLeft, yPos, barLength, stepsSize, mBarWeight, mBarColor)

        // Initialize thumbs to the desired indices
        mLeftThumb?.x = getThumbLeftPosition()
        mRightThumb?.x = getThumbRightPosition()

        // Create the line connecting the two thumbs.
        mConnectingLine = ConnectingLine(ctx, yPos, mConnectingLineWeight, mConnectingLineColor)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mBar?.draw(canvas)

        if (mLeftThumb != null && mRightThumb != null) {
            mConnectingLine?.draw(canvas, mLeftThumb!!, mRightThumb!!)
        }

        mLeftThumb?.draw(canvas)
        mRightThumb?.draw(canvas)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        // If this View is not enabled, don't allow for touch interactions.
        if (!isEnabled) {
            return false
        }

        return when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                onActionDown(event.x, event.y)
                true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                this.parent.requestDisallowInterceptTouchEvent(false)
                onActionUp(event.x)
                true
            }

            MotionEvent.ACTION_MOVE -> {
                onActionMove(event.x)
                this.parent.requestDisallowInterceptTouchEvent(true)
                true
            }

            else -> false
        }
    }

    /**
     * Set the weight of the bar line and the tick lines in the range bar.
     *
     * @param barWeight Float specifying the weight of the bar and tick lines in
     * px.
     */
    fun setBarWeight(barWeight: Float) {
        mBarWeight = barWeight
        createBar()
    }

    /**
     * Set the color of the bar line and the tick lines in the range bar.
     *
     * @param barColor Integer specifying the color of the bar line.
     */
    fun setBarColor(barColor: Int) {
        mBarColor = barColor
        createBar()
    }

    /**
     * Set the weight of the connecting line between the thumbs.
     *
     * @param connectingLineWeight Float specifying the weight of the connecting
     * line.
     */
    fun setConnectingLineWeight(connectingLineWeight: Float) {
        mConnectingLineWeight = connectingLineWeight
        createConnectingLine()
    }

    /**
     * Set the color of the connecting line between the thumbs.
     *
     * @param connectingLineColor Integer specifying the color of the connecting
     * line.
     */
    fun setConnectingLineColor(connectingLineColor: Int) {
        mConnectingLineColor = connectingLineColor
        createConnectingLine()
    }

    /**
     * If this is set, the thumb images will be replaced with a circle of the
     * specified radius. Default width = 20dp.
     *
     * @param thumbRadius Float specifying the radius of the thumbs to be drawn.
     */
    fun setThumbRadius(thumbRadius: Float) {
        mThumbRadiusDP = thumbRadius
        createThumbs()
    }

    /**
     * Sets the location of each thumb according to the developer's choice.
     * Numbered from 0 to mTickCount - 1 from the left.
     *
     * @param leftThumbIndex Integer specifying the index of the left thumb
     * @param rightThumbIndex Integer specifying the index of the right thumb
     */
    fun setThumbIndices(leftThumbIndex: Int, rightThumbIndex: Int) {
        if (indexOutOfRange(leftThumbIndex, rightThumbIndex)) {

            if (leftThumbIndex < minSliderValue) {
                setThumbIndices(minSliderValue.toInt(), rightThumbIndex)
            }

            if (rightThumbIndex > maxSliderValue) {
                setThumbIndices(leftThumbIndex, maxSliderValue.toInt())
            }

        } else {

            if (mFirstSetTickCount)
                mFirstSetTickCount = false

            leftIndex = leftThumbIndex
            rightIndex = rightThumbIndex
            createThumbs()

            onSliderChangeListener?.onIndexChange(this, leftIndex, rightIndex)
        }

        invalidate()
        requestLayout()
    }

    private fun fetchAccentColor(): Int {
        val typedValue = TypedValue()

        val a = context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorAccent))
        val color = a.getColor(0, 0)

        a.recycle()

        return color
    }

    /**
     * Does all the functions of the constructor for RangeBar. Called by both
     * RangeBar constructors in lieu of copying the code for each constructor.
     *
     * @param context Context from the constructor.
     * @param attrs AttributeSet from the constructor.
     * @return none
     */
    private fun sliderInit(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.Slider, 0, 0)

        try {
            val colorAccent = fetchAccentColor()

            mBarWeight = ta.getDimension(R.styleable.Slider_barWeight, DEFAULT_BAR_WEIGHT_PX)
            mBarColor = ta.getColor(R.styleable.Slider_barColor, colorAccent)
            mConnectingLineWeight = ta.getDimension(R.styleable.Slider_connectingLineWeight, DEFAULT_CONNECTING_LINE_WEIGHT_PX)
            mConnectingLineColor = ta.getColor(R.styleable.Slider_connectingLineColor, colorAccent)
            mThumbRadiusDP = ta.getDimension(R.styleable.Slider_indicatorRadius, DEFAULT_THUMB_RADIUS_DP)
            mIndicatorColor = ta.getColor(R.styleable.Slider_indicatorColor, colorAccent)

            minSliderValue = ta.getFloat(R.styleable.Slider_minValue, 0f)
            maxSliderValue = ta.getFloat(R.styleable.Slider_maxValue, 0f)
            if (minSliderValue > maxSliderValue) {
                throw IllegalStateException("The minValue $minSliderValue can't be major of maxValue $maxSliderValue")
            }

            // Sets the values of the user-defined attributes based on the XML
            // attributes.
            val delta = maxSliderValue - minSliderValue

            if (isValidTickCount(delta.toInt())) {

                // Similar functions performed above in setTickCount; make sure
                // you know how they interact
                stepsSize = delta.toInt()

                onSliderChangeListener?.onIndexChange(this, leftIndex, rightIndex)

            } else {
                Log.e(TAG, "tickCount less than 2; invalid tickCount. XML input ignored.")
            }

        } finally {

            ta.recycle()
        }

    }

    /**
     * Creates a new mBar
     *
     * @param none
     */
    private fun createBar() {

        if (mLeftThumb != null)
            mBar = Bar(marginLeft,
                    yPos,
                    barLength,
                    stepsSize,
                    mBarWeight,
                    mBarColor)
        invalidate()
    }

    /**
     * Creates a new ConnectingLine.
     *
     * @param none
     */
    private fun createConnectingLine() {

        mConnectingLine = ConnectingLine(context,
                yPos,
                mConnectingLineWeight,
                mConnectingLineColor)
        invalidate()
    }

    /**
     * Creates two new Thumbs.
     *
     * @param none
     */
    private fun createThumbs() {

        val ctx = context
        val yPos = yPos

        mLeftThumb = Thumb(ctx,
                yPos,
                mIndicatorColor,
                mThumbRadiusDP)
        mRightThumb = Thumb(ctx,
                yPos,
                mIndicatorColor,
                mThumbRadiusDP)

        // Initialize thumbs to the desired indices
        mLeftThumb?.x = getThumbLeftPosition()
        mRightThumb?.x = getThumbRightPosition()

        invalidate()
    }

    private fun getThumbLeftPosition() = marginLeft + ((leftIndex.toFloat() - minSliderValue) / (maxSliderValue - minSliderValue)) * barLength

    private fun getThumbRightPosition() = marginLeft + ((rightIndex.toFloat() - minSliderValue) / (maxSliderValue - minSliderValue)) * barLength

    /**
     * Returns if either index is outside the range of the tickCount.
     *
     * @param leftThumbIndex Integer specifying the left thumb index.
     * @param rightThumbIndex Integer specifying the right thumb index.
     * @return boolean If the index is out of range.
     */
    private fun indexOutOfRange(leftThumbIndex: Int, rightThumbIndex: Int): Boolean {
        return leftThumbIndex < minSliderValue || rightThumbIndex > maxSliderValue
    }

    /**
     * If is invalid tickCount, rejects. TickCount must be greater than 1
     *
     * @param tickCount Integer
     * @return boolean: whether tickCount > 1
     */
    private fun isValidTickCount(tickCount: Int): Boolean {
        return tickCount > 1
    }

    /**
     * Handles a [MotionEvent.ACTION_DOWN] event.
     *
     * @param x the x-coordinate of the down action
     * @param y the y-coordinate of the down action
     */
    private fun onActionDown(x: Float, y: Float) {

        if (mLeftThumb?.isPressed == false && mLeftThumb?.isInTargetZone(x, y) == true) {

            pressThumb(mLeftThumb)

        } else if (mLeftThumb?.isPressed == false && mRightThumb?.isInTargetZone(x, y) == true) {

            pressThumb(mRightThumb)
        }
    }

    /**
     * Handles a [MotionEvent.ACTION_UP] or
     * [MotionEvent.ACTION_CANCEL] event.
     *
     * @param x the x-coordinate of the up action
     */
    private fun onActionUp(x: Float) {

        if (mLeftThumb?.isPressed == true) {

            releaseThumb(mLeftThumb)

        } else if (mRightThumb?.isPressed == true) {

            releaseThumb(mRightThumb)

        } else {

            val leftThumbXDistance = Math.abs(mLeftThumb?.x ?: 0f - x)
            val rightThumbXDistance = Math.abs(mRightThumb?.x ?: 0f - x)

            if (leftThumbXDistance < rightThumbXDistance) {
                mLeftThumb?.x = x
                releaseThumb(mLeftThumb)
            } else {
                mRightThumb?.x = x
                releaseThumb(mRightThumb)
            }

            // Get the updated nearest tick marks for each thumb.
            val newLeftIndex = mBar!!.getNearestTickIndex(mLeftThumb)
            val newRightIndex = mBar!!.getNearestTickIndex(mRightThumb)

            // If either of the indices have changed, update and call the listener.
            if (newLeftIndex != leftIndex || newRightIndex != rightIndex) {

                leftIndex = newLeftIndex
                rightIndex = newRightIndex

                onSliderChangeListener?.onIndexChange(this, leftIndex, rightIndex)
            }
        }
    }

    /**
     * Handles a [MotionEvent.ACTION_MOVE] event.
     *
     * @param x the x-coordinate of the move event
     */
    private fun onActionMove(x: Float) {

        // Move the pressed thumb to the new x-position.
        if (mLeftThumb?.isPressed == true) {
            moveThumb(mLeftThumb, x)
        } else if (mRightThumb?.isPressed == true) {
            moveThumb(mRightThumb, x)
        }

        // If the thumbs have switched order, fix the references.
        if (mLeftThumb?.x ?: 0f > mRightThumb?.x ?: 0f) {
            val temp = mLeftThumb
            mLeftThumb = mRightThumb
            mRightThumb = temp
        }

        // Get the updated nearest tick marks for each thumb.
        val newLeftIndex = mBar!!.getNearestTickIndex(mLeftThumb) + minSliderValue.toInt()
        val newRightIndex = mBar!!.getNearestTickIndex(mRightThumb) + minSliderValue.toInt()

        // If either of the indices have changed, update and call the listener.
        if (newLeftIndex != leftIndex || newRightIndex != rightIndex) {

            leftIndex = newLeftIndex
            rightIndex = newRightIndex

            onSliderChangeListener?.onIndexChange(this, leftIndex, rightIndex)
        }
    }

    /**
     * Set the thumb to be in the pressed state and calls invalidate() to redraw
     * the canvas to reflect the updated state.
     *
     * @param thumb the thumb to press
     */
    private fun pressThumb(thumb: Thumb?) {
        if (mFirstSetTickCount)
            mFirstSetTickCount = false
        thumb?.press()
        invalidate()
    }

    /**
     * Set the thumb to be in the normal/un-pressed state and calls invalidate()
     * to redraw the canvas to reflect the updated state.
     *
     * @param thumb the thumb to release
     */
    private fun releaseThumb(thumb: Thumb?) {

        val nearestTicchakX = mBar?.getNearestTickCoordinate(thumb)
        thumb?.x = nearestTicchakX ?: 0f
        thumb?.release()
        invalidate()
        onSliderChangeListener?.onRelease(this, leftIndex, rightIndex)
    }

    /**
     * Moves the thumb to the given x-coordinate.
     *
     * @param thumb the thumb to move
     * @param x the x-coordinate to move the thumb to
     */
    private fun moveThumb(thumb: Thumb?, x: Float) {

        // If the user has moved their finger outside the range of the bar,
        // do not move the thumbs past the edge.
        if (x < mBar!!.leftX || x > mBar!!.rightX) {
            // Do nothing.
        } else {
            thumb?.x = x
            invalidate()
        }
    }

    /**
     * A callback that notifies clients when the RangeBar has changed. The
     * listener will only be called when either thumb's index has changed - not
     * for every movement of the thumb.
     */
    interface OnSliderChangeListener {

        fun onIndexChange(slider: Slider, leftIndicatorValue: Int, rightIndicatorValue: Int)

        fun onRelease(slider: Slider, leftIndicatorValue: Int, rightIndicatorValue: Int)
    }

    companion object {

        private val TAG = "Slider"

        // Default values for variables
        private const val DEFAULT_BAR_WEIGHT_PX = 2f
        private const val DEFAULT_BAR_COLOR = Color.LTGRAY
        private const val DEFAULT_CONNECTING_LINE_WEIGHT_PX = 4f

        // Corresponds to android.R.color.holo_blue_light.
        private const val DEFAULT_CONNECTING_LINE_COLOR = -0xcc4a1b

        // Indicator value tells Thumb.java whether it should draw the circle or not
        private const val DEFAULT_THUMB_RADIUS_DP = -1f
        private const val DEFAULT_THUMB_COLOR_NORMAL = -1
    }
}
