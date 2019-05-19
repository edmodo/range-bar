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
        get() = this.mLeftThumb?.halfWidth ?: 0f

    /**
     * Get yPos in each of the public attribute methods.
     *
     * @param none
     * @return float yPos
     */
    private val yPos: Float
        get() = this.height / 2f

    /**
     * Get barLength in each of the public attribute methods.
     *
     * @param none
     * @return float barLength
     */
    private val barLength: Float
        get() = this.width - 2 * this.marginLeft

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

        bundle.putFloat("BAR_WEIGHT", this.mBarWeight)
        bundle.putInt("BAR_COLOR", this.mBarColor)
        bundle.putFloat("CONNECTING_LINE_WEIGHT", this.mConnectingLineWeight)
        bundle.putInt("CONNECTING_LINE_COLOR", this.mConnectingLineColor)

        bundle.putFloat("THUMB_RADIUS_DP", this.mThumbRadiusDP)
        bundle.putInt("THUMB_COLOR_NORMAL", this.mIndicatorColor)

        bundle.putInt("LEFT_INDEX", this.leftIndex)
        bundle.putInt("RIGHT_INDEX", this.rightIndex)

        bundle.putFloat("MIN_VALUE", this.minSliderValue)
        bundle.putFloat("MAX_VALUE", this.maxSliderValue)

        bundle.putBoolean("FIRST_SET_TICK_COUNT", this.mFirstSetTickCount)

        return bundle
    }

    private fun invalidateSliderView() {
        this.stepsSize = (this.maxSliderValue - this.minSliderValue).toInt()
        createBar()
        setThumbIndices(this.leftIndex, this.rightIndex)
    }

    override fun onRestoreInstanceState(state: Parcelable) {

        if (state is Bundle) {

            this.mBarWeight = state.getFloat("BAR_WEIGHT")
            this.mBarColor = state.getInt("BAR_COLOR")
            this.mConnectingLineWeight = state.getFloat("CONNECTING_LINE_WEIGHT")
            this.mConnectingLineColor = state.getInt("CONNECTING_LINE_COLOR")

            this.mThumbRadiusDP = state.getFloat("THUMB_RADIUS_DP")
            this.mIndicatorColor = state.getInt("THUMB_COLOR_NORMAL")

            this.leftIndex = state.getInt("LEFT_INDEX")
            this.rightIndex = state.getInt("RIGHT_INDEX")
            this.mFirstSetTickCount = state.getBoolean("FIRST_SET_TICK_COUNT")

            this.minSliderValue = state.getFloat("MIN_VALUE")
            this.maxSliderValue = state.getFloat("MAX_VALUE")

            setThumbIndices(this.leftIndex, this.rightIndex)

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
            else -> this.mDefaultWidth
        }

        // The RangeBar height should be as small as possible.
        height = when (measureHeightMode) {
            MeasureSpec.AT_MOST -> Math.min(mDefaultHeight, measureHeight)
            MeasureSpec.EXACTLY -> measureHeight
            else -> this.mDefaultHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        super.onSizeChanged(w, h, oldw, oldh)

        val ctx = this.context

        // This is the initial point at which we know the size of the View.

        // Create the two thumb objects.
        val yPos = h / 2f
        this.mLeftThumb = Thumb(ctx, yPos, this.mIndicatorColor, this.mThumbRadiusDP)
        this.mRightThumb = Thumb(ctx, yPos, this.mIndicatorColor, this.mThumbRadiusDP)

        // Create the underlying bar.
        val marginLeft = this.mLeftThumb?.halfWidth ?: 0f
        val barLength = w - 2 * marginLeft
        this.mBar = Bar(marginLeft, yPos, barLength, this.stepsSize, this.mBarWeight, this.mBarColor)

        // Initialize thumbs to the desired indices
        this.mLeftThumb?.x = getThumbLeftPosition()
        this.mRightThumb?.x = getThumbRightPosition()

        // Create the line connecting the two thumbs.
        this.mConnectingLine = ConnectingLine(ctx, yPos, this.mConnectingLineWeight, this.mConnectingLineColor)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        this.mBar?.draw(canvas)

        if (this.mLeftThumb != null && this.mRightThumb != null) {
            this.mConnectingLine?.draw(canvas, this.mLeftThumb!!, this.mRightThumb!!)
        }

        this.mLeftThumb?.draw(canvas)
        this.mRightThumb?.draw(canvas)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        // If this View is not enabled, don't allow for touch interactions.
        if (!this.isEnabled) {
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
        this.mBarWeight = barWeight
        createBar()
    }

    /**
     * Set the color of the bar line and the tick lines in the range bar.
     *
     * @param barColor Integer specifying the color of the bar line.
     */
    fun setBarColor(barColor: Int) {
        this.mBarColor = barColor
        createBar()
    }

    /**
     * Set the weight of the connecting line between the thumbs.
     *
     * @param connectingLineWeight Float specifying the weight of the connecting
     * line.
     */
    fun setConnectingLineWeight(connectingLineWeight: Float) {
        this.mConnectingLineWeight = connectingLineWeight
        createConnectingLine()
    }

    /**
     * Set the color of the connecting line between the thumbs.
     *
     * @param connectingLineColor Integer specifying the color of the connecting
     * line.
     */
    fun setConnectingLineColor(connectingLineColor: Int) {
        this.mConnectingLineColor = connectingLineColor
        createConnectingLine()
    }

    /**
     * If this is set, the thumb images will be replaced with a circle of the
     * specified radius. Default width = 20dp.
     *
     * @param thumbRadius Float specifying the radius of the thumbs to be drawn.
     */
    fun setThumbRadius(thumbRadius: Float) {
        this.mThumbRadiusDP = thumbRadius
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

            if (leftThumbIndex < this.minSliderValue) {
                setThumbIndices(this.minSliderValue.toInt(), rightThumbIndex)
            }

            if (rightThumbIndex > this.maxSliderValue) {
                setThumbIndices(leftThumbIndex, this.maxSliderValue.toInt())
            }

        } else {

            if (this.mFirstSetTickCount)
                this.mFirstSetTickCount = false

            this.leftIndex = leftThumbIndex
            this.rightIndex = rightThumbIndex
            createThumbs()

            this.onSliderChangeListener?.onIndexChange(this, this.leftIndex, this.rightIndex)
        }

        invalidate()
        requestLayout()
    }

    private fun fetchAccentColor(): Int {
        val typedValue = TypedValue()

        val a = this.context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorAccent))
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

            this.mBarWeight = ta.getDimension(R.styleable.Slider_barWeight, DEFAULT_BAR_WEIGHT_PX)
            this.mBarColor = ta.getColor(R.styleable.Slider_barColor, colorAccent)
            this.mConnectingLineWeight = ta.getDimension(R.styleable.Slider_connectingLineWeight, DEFAULT_CONNECTING_LINE_WEIGHT_PX)
            this.mConnectingLineColor = ta.getColor(R.styleable.Slider_connectingLineColor, colorAccent)
            this.mThumbRadiusDP = ta.getDimension(R.styleable.Slider_indicatorRadius, DEFAULT_THUMB_RADIUS_DP)
            this.mIndicatorColor = ta.getColor(R.styleable.Slider_indicatorColor, colorAccent)

            this.minSliderValue = ta.getFloat(R.styleable.Slider_minValue, 0f)
            this.maxSliderValue = ta.getFloat(R.styleable.Slider_maxValue, 0f)
            if (this.minSliderValue > this.maxSliderValue) {
                throw IllegalStateException("The minValue ${this.minSliderValue} can't be major of maxValue ${this.maxSliderValue}")
            }

            // Sets the values of the user-defined attributes based on the XML
            // attributes.
            val delta = this.maxSliderValue - this.minSliderValue

            if (isValidTickCount(delta.toInt())) {

                // Similar functions performed above in setTickCount; make sure
                // you know how they interact
                this.stepsSize = delta.toInt()

                this.onSliderChangeListener?.onIndexChange(this, this.leftIndex, this.rightIndex)

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

        if (this.mLeftThumb != null)
            this.mBar = Bar(this.marginLeft, this.yPos, this.barLength, this.stepsSize, this.mBarWeight, this.mBarColor)
        invalidate()
    }

    /**
     * Creates a new ConnectingLine.
     *
     * @param none
     */
    private fun createConnectingLine() {

        this.mConnectingLine = ConnectingLine(this.context, this.yPos, this.mConnectingLineWeight, this.mConnectingLineColor)
        invalidate()
    }

    /**
     * Creates two new Thumbs.
     *
     * @param none
     */
    private fun createThumbs() {

        val ctx = this.context
        val yPos = this.yPos

        this.mLeftThumb = Thumb(ctx, yPos, this.mIndicatorColor, this.mThumbRadiusDP)
        this.mRightThumb = Thumb(ctx, yPos, this.mIndicatorColor, this.mThumbRadiusDP)

        // Initialize thumbs to the desired indices
        this.mLeftThumb?.x = getThumbLeftPosition()
        this.mRightThumb?.x = getThumbRightPosition()

        invalidate()
    }

    private fun getThumbLeftPosition() = this.marginLeft + ((this.leftIndex.toFloat() - this.minSliderValue) / (this.maxSliderValue - this.minSliderValue)) * this.barLength

    private fun getThumbRightPosition() = this.marginLeft + ((this.rightIndex.toFloat() - this.minSliderValue) / (this.maxSliderValue - this.minSliderValue)) * this.barLength

    /**
     * Returns if either index is outside the range of the tickCount.
     *
     * @param leftThumbIndex Integer specifying the left thumb index.
     * @param rightThumbIndex Integer specifying the right thumb index.
     * @return boolean If the index is out of range.
     */
    private fun indexOutOfRange(leftThumbIndex: Int, rightThumbIndex: Int): Boolean {
        return leftThumbIndex < this.minSliderValue || rightThumbIndex > this.maxSliderValue
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

        if (this.mLeftThumb?.isPressed == false && this.mLeftThumb?.isInTargetZone(x, y) == true) {

            pressThumb(this.mLeftThumb)

        } else if (this.mLeftThumb?.isPressed == false && this.mRightThumb?.isInTargetZone(x, y) == true) {

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

        if (this.mLeftThumb?.isPressed == true) {

            releaseThumb(this.mLeftThumb)

        } else if (this.mRightThumb?.isPressed == true) {

            releaseThumb(this.mRightThumb)

        } else {

            val leftThumbXDistance = Math.abs(mLeftThumb?.x ?: 0f - x)
            val rightThumbXDistance = Math.abs(mRightThumb?.x ?: 0f - x)

            if (leftThumbXDistance < rightThumbXDistance) {
                this.mLeftThumb?.x = x
                releaseThumb(this.mLeftThumb)
            } else {
                this.mRightThumb?.x = x
                releaseThumb(this.mRightThumb)
            }

            // Get the updated nearest tick marks for each thumb.
            val newLeftIndex = mBar!!.getNearestTickIndex(this.mLeftThumb)
            val newRightIndex = mBar!!.getNearestTickIndex(this.mRightThumb)

            // If either of the indices have changed, update and call the listener.
            if (newLeftIndex != this.leftIndex || newRightIndex != this.rightIndex) {

                this.leftIndex = newLeftIndex
                this.rightIndex = newRightIndex

                this.onSliderChangeListener?.onIndexChange(this, this.leftIndex, this.rightIndex)
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
        if (this.mLeftThumb?.isPressed == true) {
            moveThumb(this.mLeftThumb, x)
        } else if (this.mRightThumb?.isPressed == true) {
            moveThumb(this.mRightThumb, x)
        }

        // If the thumbs have switched order, fix the references.
        if (this.mLeftThumb?.x ?: 0f > this.mRightThumb?.x ?: 0f) {
            val temp = this.mLeftThumb
            this.mLeftThumb = this.mRightThumb
            this.mRightThumb = temp
        }

        // Get the updated nearest tick marks for each thumb.
        val newLeftIndex = this.mBar!!.getNearestTickIndex(this.mLeftThumb) + this.minSliderValue.toInt()
        val newRightIndex = this.mBar!!.getNearestTickIndex(this.mRightThumb) + this.minSliderValue.toInt()

        // If either of the indices have changed, update and call the listener.
        if (newLeftIndex != this.leftIndex || newRightIndex != this.rightIndex) {

            this.leftIndex = newLeftIndex
            this.rightIndex = newRightIndex

            this.onSliderChangeListener?.onIndexChange(this, this.leftIndex, this.rightIndex)
        }
    }

    /**
     * Set the thumb to be in the pressed state and calls invalidate() to redraw
     * the canvas to reflect the updated state.
     *
     * @param thumb the thumb to press
     */
    private fun pressThumb(thumb: Thumb?) {
        if (this.mFirstSetTickCount)
            this.mFirstSetTickCount = false
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

        val nearestTicchakX = this.mBar?.getNearestTickCoordinate(thumb)
        thumb?.x = nearestTicchakX ?: 0f
        thumb?.release()
        invalidate()
        this.onSliderChangeListener?.onRelease(this, this.leftIndex, this.rightIndex)
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
        if (x < this.mBar!!.leftX || x > this.mBar!!.rightX) {
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

        private const val TAG = "Slider"

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
