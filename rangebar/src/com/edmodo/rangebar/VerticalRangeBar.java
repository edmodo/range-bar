package com.edmodo.rangebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jayeshthadani on 7/28/15.
 */
public class VerticalRangeBar  extends View  {


    private static final String TAG = VerticalRangeBar.class.getName();

    // Default values for variables
    private static final int DEFAULT_TICK_COUNT = 3;
    private static final float DEFAULT_TICK_HEIGHT_DP = 24;
    private static final float DEFAULT_BAR_WEIGHT_PX = 2;
    private static final int DEFAULT_BAR_COLOR = Color.LTGRAY;
    private static final float DEFAULT_CONNECTING_LINE_WEIGHT_PX = 4;
    private static final int DEFAULT_THUMB_IMAGE_NORMAL = R.drawable.seek_thumb_normal;
    private static final int DEFAULT_THUMB_IMAGE_PRESSED = R.drawable.seek_thumb_pressed;
    private static final int DEFAULT_BAR_ORIENTATION = 0;
    // O FOR HORIZONTAL, 1 FOR VERTICAL
    // Corresponds to android.R.color.holo_blue_light.
    private static final int DEFAULT_CONNECTING_LINE_COLOR = 0xff33b5e5;

    // Indicator value tells Thumb.java whether it should draw the circle or not
    private static final float DEFAULT_THUMB_RADIUS_DP = -1;
    private static final int DEFAULT_THUMB_COLOR_NORMAL = -1;
    private static final int DEFAULT_THUMB_COLOR_PRESSED = -1;
    private static final int DEFAULT_TICK_MARK_STEP = 1;



    // Instance variables for all of the customizable attributes
    private int mTickCount = DEFAULT_TICK_COUNT;
    private float mTickHeightDP = DEFAULT_TICK_HEIGHT_DP;
    private float mBarWeight = DEFAULT_BAR_WEIGHT_PX;
    private int mBarColor = DEFAULT_BAR_COLOR;
    private float mConnectingLineWeight = DEFAULT_CONNECTING_LINE_WEIGHT_PX;
    private int mConnectingLineColor = DEFAULT_CONNECTING_LINE_COLOR;
    private int mThumbImageNormal = DEFAULT_THUMB_IMAGE_NORMAL;
    private int mThumbImagePressed = DEFAULT_THUMB_IMAGE_PRESSED;

    private float mThumbRadiusDP = DEFAULT_THUMB_RADIUS_DP;
    private int mThumbColorNormal = DEFAULT_THUMB_COLOR_NORMAL;
    private int mThumbColorPressed = DEFAULT_THUMB_COLOR_PRESSED;
    private int mTickMarkStep = 1;

    private VerticalRangeBar.OnRangeBarChangeListener mListener;
    private int mLeftIndex = 0;
    private int mRightIndex = mTickCount - 1;


    private int mDefaultWidth = 250;
    private int mDefaultHeight = 400;


    private Thumb mLeftThumb;
    private Thumb mRightThumb;
    private VerticalBar mVerticalBar;
    private VerticalConnectingLine mConnectingLine;

    // setTickCount only resets indices before a thumb has been pressed or a
    // setThumbIndices() is called, to correspond with intended usage
    private boolean mFirstSetTickCount = true;

    //// constructors //////
    public VerticalRangeBar(Context context) {
        super(context);
    }

    public VerticalRangeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        rangeBarInit(context, attrs);
    }

    public VerticalRangeBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        rangeBarInit(context, attrs);
    }

    // View Methods ////////////////////////////////////////////////////////////

    @Override
    public Parcelable onSaveInstanceState() {

        final Bundle bundle = new Bundle();

        bundle.putParcelable("instanceState", super.onSaveInstanceState());

        bundle.putInt("TICK_COUNT", mTickCount);
        bundle.putFloat("TICK_HEIGHT_DP", mTickHeightDP);
        bundle.putInt("TICK_MARK_STEP", mTickMarkStep);
        bundle.putFloat("BAR_WEIGHT", mBarWeight);
        bundle.putInt("BAR_COLOR", mBarColor);
        bundle.putFloat("CONNECTING_LINE_WEIGHT", mConnectingLineWeight);
        bundle.putInt("CONNECTING_LINE_COLOR", mConnectingLineColor);

        bundle.putInt("THUMB_IMAGE_NORMAL", mThumbImageNormal);
        bundle.putInt("THUMB_IMAGE_PRESSED", mThumbImagePressed);

        bundle.putFloat("THUMB_RADIUS_DP", mThumbRadiusDP);
        bundle.putInt("THUMB_COLOR_NORMAL", mThumbColorNormal);
        bundle.putInt("THUMB_COLOR_PRESSED", mThumbColorPressed);

        bundle.putInt("LEFT_INDEX", mLeftIndex);
        bundle.putInt("RIGHT_INDEX", mRightIndex);

        bundle.putBoolean("FIRST_SET_TICK_COUNT", mFirstSetTickCount);

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {

            final Bundle bundle = (Bundle) state;

            mTickCount = bundle.getInt("TICK_COUNT");
            mTickHeightDP = bundle.getFloat("TICK_HEIGHT_DP");
            mBarWeight = bundle.getFloat("BAR_WEIGHT");
            mBarColor = bundle.getInt("BAR_COLOR");
            mConnectingLineWeight = bundle.getFloat("CONNECTING_LINE_WEIGHT");
            mConnectingLineColor = bundle.getInt("CONNECTING_LINE_COLOR");

            mThumbImageNormal = bundle.getInt("THUMB_IMAGE_NORMAL");
            mThumbImagePressed = bundle.getInt("THUMB_IMAGE_PRESSED");

            mThumbRadiusDP = bundle.getFloat("THUMB_RADIUS_DP");
            mThumbColorNormal = bundle.getInt("THUMB_COLOR_NORMAL");
            mThumbColorPressed = bundle.getInt("THUMB_COLOR_PRESSED");
            bundle.getInt("TICK_MARK_STEP");

            mLeftIndex = bundle.getInt("LEFT_INDEX");
            mRightIndex = bundle.getInt("RIGHT_INDEX");
            mFirstSetTickCount = bundle.getBoolean("FIRST_SET_TICK_COUNT");
            mTickMarkStep = bundle.getInt("TICK_MARK_STEP");

            setThumbIndices(mLeftIndex, mRightIndex);

            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));

        } else {

            super.onRestoreInstanceState(state);
        }
    }


    /**
     * Does all the functions of the constructor for RangeBar. Called by both
     * RangeBar constructors in lieu of copying the code for each constructor.
     *
     * @param context Context from the constructor.
     * @param attrs AttributeSet from the constructor.
     * @return none
     */
    private void rangeBarInit(Context context, AttributeSet attrs)
    {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RangeBar, 0, 0);

        try {

            // Sets the values of the user-defined attributes based on the XML
            // attributes.
            final Integer tickCount = ta.getInteger(R.styleable.RangeBar_tickCount, DEFAULT_TICK_COUNT);
            if (isValidTickCount(tickCount)) {

                // Similar functions performed above in setTickCount; make sure
                // you know how they interact
                mTickCount = tickCount;
                mLeftIndex = 0;
                mRightIndex = mTickCount - 1;

            } else {

                Log.e(TAG, "tickCount less than 2; invalid tickCount. XML input ignored.");
            }

            mTickHeightDP = ta.getDimension(R.styleable.RangeBar_tickHeight, DEFAULT_TICK_HEIGHT_DP);
            mBarWeight = ta.getDimension(R.styleable.RangeBar_barWeight, DEFAULT_BAR_WEIGHT_PX);
            mBarColor = ta.getColor(R.styleable.RangeBar_barColor, DEFAULT_BAR_COLOR);
            mConnectingLineWeight = ta.getDimension(R.styleable.RangeBar_connectingLineWeight,
                    DEFAULT_CONNECTING_LINE_WEIGHT_PX);
            mConnectingLineColor = ta.getColor(R.styleable.RangeBar_connectingLineColor,
                    DEFAULT_CONNECTING_LINE_COLOR);
            mThumbRadiusDP = ta.getDimension(R.styleable.RangeBar_thumbRadius, DEFAULT_THUMB_RADIUS_DP);
            mThumbImageNormal = ta.getResourceId(R.styleable.RangeBar_thumbImageNormal,
                    DEFAULT_THUMB_IMAGE_NORMAL);
            mThumbImagePressed = ta.getResourceId(R.styleable.RangeBar_thumbImagePressed,
                    DEFAULT_THUMB_IMAGE_PRESSED);
            mThumbColorNormal = ta.getColor(R.styleable.RangeBar_thumbColorNormal, DEFAULT_THUMB_COLOR_NORMAL);
            mThumbColorPressed = ta.getColor(R.styleable.RangeBar_thumbColorPressed,
                    DEFAULT_THUMB_COLOR_PRESSED);

            mTickMarkStep = ta.getInteger(R.styleable.RangeBar_tickMarkStep, DEFAULT_TICK_MARK_STEP);

        } finally {

            ta.recycle();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width;
        int height;


        // Get measureSpec mode and size values.
        final int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        //Log.i(RangeBar.class.getName(), "onMeasure: "+ measureWidth + " x " + measureHeight);
        // The RangeBar width should be as large as possible.

        // height should be max, width should be min
        if (measureWidthMode == MeasureSpec.AT_MOST || measureWidthMode == MeasureSpec.EXACTLY) {
            width = Math.min(mDefaultWidth, measureWidth);
        } else {
            width = mDefaultWidth;
        }

        // The RangeBar height should be as small as possible.
        if (measureHeightMode == MeasureSpec.AT_MOST) {
            height = Math.max(mDefaultHeight, measureHeight);
        } else if (measureHeightMode == MeasureSpec.EXACTLY) {
            height = measureHeight;
        } else {
            height = mDefaultHeight;
        }

        //Log.i(RangeBar.class.getName(), "Vertical onMeasure calculation: "+ width + " x " + height);
        setMeasuredDimension(width, height);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Log.i(RangeBar.class.getName(), "onSizeChanged");
        final Context ctx = getContext();

        // This is the initial point at which we know the size of the View.
        // Create the two thumb objects.

        final float xPos =  mDefaultWidth /4f;
        mLeftThumb = new Thumb(ctx,
                xPos,
                mThumbColorNormal,
                mThumbColorPressed,
                mThumbRadiusDP,
                mThumbImageNormal,
                mThumbImagePressed);
        mRightThumb = new Thumb(ctx,
                xPos,
                mThumbColorNormal,
                mThumbColorPressed,
                mThumbRadiusDP,
                mThumbImageNormal,
                mThumbImagePressed);

        // Create the underlying bar.
        final float marginBottom = getMarginBottom();
        final float barLength = getBarLength() - 10;
        mVerticalBar = new VerticalBar(ctx, marginBottom + getWidth()/4, xPos, barLength, mTickCount, mTickHeightDP, mBarWeight, mBarColor, mTickMarkStep);

        // Initialize thumbs to the desired indices
        mLeftThumb.setY(xPos + barLength );
        mLeftThumb.setX(marginBottom + getWidth()/4);


        //mRightThumb.setY(marginBottom + barLength);
        mRightThumb.setX(marginBottom + getWidth() / 4);
        mRightThumb.setY(marginBottom  + barLength - (mRightIndex / (float) (mTickCount - 1)) * barLength);

        // Create the line connecting the two thumbs.
        mConnectingLine = new VerticalConnectingLine(ctx, marginBottom + getWidth()/4, mConnectingLineWeight, mConnectingLineColor);
    }

    public boolean isFirstTouchEvent() {
        return firstTouchEvent;
    }

    public void setFirstTouchEvent(boolean firstTouchEvent) {
        this.firstTouchEvent = firstTouchEvent;
    }

    private boolean firstTouchEvent;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mVerticalBar.draw(canvas);

        //setFirstTouchEvent(true);
        if (isFirstTouchEvent()){
            mConnectingLine.draw(canvas, mLeftThumb, mRightThumb);

            //mLeftThumb.draw(canvas);
            mRightThumb.draw(canvas);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // If this View is not enabled, don't allow for touch interactions.
        if (!isEnabled()) {
            return false;
        }

        setFirstTouchEvent(true);
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                onActionDown(event.getX(), event.getY());
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                onActionUp(event.getX(), event.getY());
                return true;

            case MotionEvent.ACTION_MOVE:
                onActionMove(event.getY());
                this.getParent().requestDisallowInterceptTouchEvent(true);
                return true;

            default:
                return false;
        }
    }

    /**
     * Handles a {@link MotionEvent#ACTION_UP} or
     * {@link MotionEvent#ACTION_CANCEL} event.
     *
     * @param x the x-coordinate of the up action
     * @param y the y-coordinate of the up action
     */
    private void onActionUp(float x, float y) {

        if (mRightThumb.isPressed()) {
            releaseThumb(mRightThumb);
            if (mListener != null) {
                mListener.onIndexChangeListener(this, mLeftIndex, mRightIndex);
            }
        } else {
            if (y < mVerticalBar.getTopY()) {
                // Do nothing.
                mRightThumb.setY(mVerticalBar.getTopY());
                releaseThumb(mRightThumb);
            }else if ( y > mVerticalBar.getBottomY()){
                mRightThumb.setY(mVerticalBar.getBottomY());
                releaseThumb(mRightThumb);
            }else {
                mRightThumb.setY(y);
                releaseThumb(mRightThumb);
            }

            final int newRightIndex = mVerticalBar.getNearestTickIndex(mRightThumb);
            mRightIndex = mTickCount - 1 - newRightIndex;
            if (mRightIndex != newRightIndex){
                if (mListener != null) {
                    mListener.onIndexChangeListener(this, mLeftIndex, mRightIndex);
                }
            }
        }


    }


    /**
     * Handles a {@link MotionEvent#ACTION_DOWN} event.
     *
     * @param x the x-coordinate of the down action
     * @param y the y-coordinate of the down action
     */
    private void onActionDown(float x, float y) {

        if (mRightThumb.isInTargetZone(x, y)){
            pressThumb(mRightThumb);
        }
    }

    /**
     * Handles a {@link MotionEvent#ACTION_MOVE} event.
     *
     * @param y the y-coordinate of the move event
     */
    private void onActionMove(float y) {

        // Move the pressed thumb to the new x-position.
//        if (mLeftThumb.isPressed()) {
//            moveThumb(mLeftThumb, x);
//        } else if (mRightThumb.isPressed()) {
//            moveThumb(mRightThumb, x);
//        }

        if (mRightThumb.isPressed()) {
            moveThumb(mRightThumb, y);
        }


        // If the thumbs have switched order, fix the references.
//        if (mLeftThumb.getY() > mRightThumb.getY()) {
//            final Thumb temp = mLeftThumb;
//            mLeftThumb = mRightThumb;
//            mRightThumb = temp;
//        }

        // Get the updated nearest tick marks for each thumb.
        //final int newLeftIndex = mVerticalBar.getNearestTickIndex(mLeftThumb);
        final int newRightIndex = mVerticalBar.getNearestTickIndex(mRightThumb);

            mRightIndex = mTickCount - 1 - newRightIndex ;

            if (mListener != null) {
                mListener.onIndexChangeListener(this, mLeftIndex, mRightIndex);
            }
    }


    /**
     * Set the thumb to be in the pressed state and calls invalidate() to redraw
     * the canvas to reflect the updated state.
     *
     * @param thumb the thumb to press
     */
    private void pressThumb(Thumb thumb) {
        if (mFirstSetTickCount == true) {
            mFirstSetTickCount = false;
        }
        thumb.press();
        invalidate();
    }

    /**
     * Set the thumb to be in the normal/un-pressed state and calls invalidate()
     * to redraw the canvas to reflect the updated state.
     *
     * @param thumb the thumb to release
     */
    private void releaseThumb(Thumb thumb) {

        final float nearestTickX = mVerticalBar.getNearestTickCoordinate(thumb);
        thumb.setY(nearestTickX);
        thumb.release();
        invalidate();
    }

    /**
     * Moves the thumb to the given y-coordinate.
     *
     * @param thumb the thumb to move
     * @param y the y-coordinate to move the thumb to
     */
    private void moveThumb(Thumb thumb, float y) {

        // If the user has moved their finger outside the range of the bar,
        // do not move the thumbs past the edge.
        // Log.i(RangeBar.class.getName(), "moveThumb: " + x );
        if (y < mVerticalBar.getTopY() || y > mVerticalBar.getBottomY()) {
            // Do nothing.
        } else {
            thumb.setY(y);
            invalidate();
        }
    }

    /**
     * Sets the location of each thumb according to the developer's choice.
     * Numbered from 0 to mTickCount - 1 from the left.
     *
     * @param leftThumbIndex  Integer specifying the index of the left thumb
     * @param rightThumbIndex Integer specifying the index of the right thumb
     */
    public void setThumbIndices(int leftThumbIndex, int rightThumbIndex) {
        if (indexOutOfRange(leftThumbIndex, rightThumbIndex)) {

            Log.e(TAG, "A thumb index is out of bounds. Check that it is between 0 and mTickCount - 1");
            throw new IllegalArgumentException("A thumb index is out of bounds. Check that it is between 0 and mTickCount - 1");

        } else {

            if (mFirstSetTickCount == true)
                mFirstSetTickCount = false;

            mLeftIndex = leftThumbIndex;
            mRightIndex = rightThumbIndex;
            createThumbs();

            if (mListener != null) {
                mListener.onIndexChangeListener(this, mLeftIndex, mRightIndex);
            }
        }

        invalidate();
        requestLayout();
    }

    /**
     * Creates two new Thumbs.
     */
    private void createThumbs() {

        Context ctx = getContext();

//        mLeftThumb = new Thumb(ctx,
//                               yPos,
//                               mThumbColorNormal,
//                               mThumbColorPressed,
//                               mThumbRadiusDP,
//                               mThumbImageNormal,
//                               mThumbImagePressed);
        final float xPos =  mDefaultWidth /4f;
        mRightThumb = new Thumb(ctx,
                xPos,
                mThumbColorNormal,
                mThumbColorPressed,
                mThumbRadiusDP,
                mThumbImageNormal,
                mThumbImagePressed);

//        mRightThumb = new Thumb(ctx,
//                getWidth()/2f,
//                mThumbColorNormal,
//                mThumbColorPressed,
//                mThumbRadiusDP,
//                mThumbImageNormal,
//                mThumbImagePressed);

        final float marginBottom = getMarginBottom();
        final float barLength = getBarLength();

        // Initialize thumbs to the desired indices
        //mLeftThumb.setX(marginLeft + (mLeftIndex / (float) (mTickCount - 1)) * barLength);

        mRightThumb.setY(marginBottom + barLength - (mRightIndex / (float) (mTickCount - 1)) * barLength);
        mRightThumb.setX(marginBottom + getWidth() / 4);

        invalidate();
    }

    /**
     * Set the color of the bar line and the tick lines in the range bar.
     *
     * @param barColor Integer specifying the color of the bar line.
     */
    public void setBarColor(int barColor) {
        Log.i(VerticalRangeBar.class.getName(), "setBarColor");
        mBarColor = barColor;
        createBar();
    }

    /**
     * Creates a new mBar
     *
     */
    private void createBar() {

        // Create the underlying bar.
        mVerticalBar = new VerticalBar(getContext(), getMarginBottom(), getWidth() / 4f ,
                getBarLength(), mTickCount, mTickHeightDP, mBarWeight, mBarColor, mTickMarkStep);

        invalidate();
    }


    /**
     * Returns if either index is outside the range of the tickCount.
     *
     * @param leftThumbIndex Integer specifying the left thumb index.
     * @param rightThumbIndex Integer specifying the right thumb index.
     * @return boolean If the index is out of range.
     */
    private boolean indexOutOfRange(int leftThumbIndex, int rightThumbIndex) {
        return (leftThumbIndex < 0 || leftThumbIndex >= mTickCount
                || rightThumbIndex < 0
                || rightThumbIndex >= mTickCount);
    }


    /**
     * Sets a listener to receive notifications of changes to the RangeBar. This
     * will overwrite any existing set listeners.
     *
     * @param listener the RangeBar notification listener; null to remove any
     *            existing listener
     */
    public void setOnRangeBarChangeListener(VerticalRangeBar.OnRangeBarChangeListener listener) {
        mListener = listener;
    }

    /**
     * Sets the number of ticks in the RangeBar.
     *
     * @param tickCount Integer specifying the number of ticks.
     */
    public void setTickCount(int tickCount) {

        if (isValidTickCount(tickCount)) {
            mTickCount = tickCount;

            // Prevents resetting the indices when creating new activity, but
            // allows it on the first setting.
            if (mFirstSetTickCount) {
                mLeftIndex = 0;
                mRightIndex = mTickCount - 1;

                if (mListener != null) {
                    mListener.onIndexChangeListener(this, mLeftIndex, mRightIndex);
                }
            }
            if (indexOutOfRange(mLeftIndex, mRightIndex))
            {
                mLeftIndex = 0;
                mRightIndex = mTickCount - 1;

                if (mListener != null)
                    mListener.onIndexChangeListener(this, mLeftIndex, mRightIndex);
            }

            createBar();
            createThumbs();
        }
        else {
            Log.e(TAG, "tickCount less than 2; invalid tickCount.");
            throw new IllegalArgumentException("tickCount less than 2; invalid tickCount.");
        }
    }

    /**
     * Sets the height of the ticks in the range bar.
     *
     * @param tickHeight Float specifying the height of each tick mark in dp.
     */
    public void setTickHeight(float tickHeight) {

        mTickHeightDP = tickHeight;
        createBar();
    }

    /**
     * sets tick mark steps in Vertical Range bar.
     *
     * @param tickMarkStep int for specifying tickMark steps between two ticks.
     */
    public void setTickMarkStep(int tickMarkStep){
        mTickMarkStep = tickMarkStep;
        createBar();
    }

    /**
     * Get marginLeft in each of the public attribute methods.
     *
     * @return float marginLeft
     */
    private float getMarginBottom() {
        return ((mRightThumb != null) ? mRightThumb.getHalfWidth() : 0);
    }

    /**
     * Get barLength in each of the public attribute methods.
     *
     * @return float barLength
     */
    private float getBarLength() {
        return (getHeight() - 2 * getMarginBottom());
    }


    /**
     * If is invalid tickCount, rejects. TickCount must be greater than 1
     *
     * @param tickCount Integer
     * @return boolean: whether tickCount > 1
     */
    private boolean isValidTickCount(int tickCount) {
        return (tickCount > 1);
    }

// Inner Classes ///////////////////////////////////////////////////////////

    /**
     * A callback that notifies clients when the RangeBar has changed. The
     * listener will only be called when either thumb's index has changed - not
     * for every movement of the thumb.
     */
    public static interface OnRangeBarChangeListener {

        public void onIndexChangeListener(VerticalRangeBar rangeBar, int leftThumbIndex, int rightThumbIndex);
    }


}
