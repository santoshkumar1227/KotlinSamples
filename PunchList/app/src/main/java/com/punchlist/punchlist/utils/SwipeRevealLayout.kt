package com.punchlist.punchlist.utils

import android.util.DisplayMetrics
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewCompat.offsetTopAndBottom
import android.support.v4.view.ViewCompat.offsetLeftAndRight
import android.support.v4.widget.ViewDragHelper
import android.opengl.ETC1.getWidth
import android.view.MotionEvent
import android.view.GestureDetector
import android.support.v4.view.GestureDetectorCompat
import com.punchlist.punchlist.utils.SwipeRevealLayout
import android.content.res.TypedArray
import android.opengl.ETC1.getHeight
import android.view.View.MeasureSpec
import android.view.ViewGroup.LayoutParams.FILL_PARENT
import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.support.annotation.Nullable
import android.util.AttributeSet
import android.view.View
import com.punchlist.punchlist.R


@SuppressLint("RtlHardcoded")
class SwipeRevealLayout : ViewGroup {

    /**
     * Main view is the view which is shown when the layout is closed.
     */
    private var mMainView: View? = null

    /**
     * Secondary view is the view which is shown when the layout is opened.
     */
    private var mSecondaryView: View? = null

    /**
     * The rectangle position of the main view when the layout is closed.
     */
    private val mRectMainClose = Rect()

    /**
     * The rectangle position of the main view when the layout is opened.
     */
    private val mRectMainOpen = Rect()

    /**
     * The rectangle position of the secondary view when the layout is closed.
     */
    private val mRectSecClose = Rect()

    /**
     * The rectangle position of the secondary view when the layout is opened.
     */
    private val mRectSecOpen = Rect()

    /**
     * The minimum distance (px) to the closest drag edge that the SwipeRevealLayout
     * will disallow the parent to intercept touch event.
     */
    private var mMinDistRequestDisallowParent = 0

    private var mIsOpenBeforeInit = false
    @Volatile
    private var mIsScrolling = false
    /**
     * @return true if the drag/swipe motion is currently locked.
     */
    val isDragLocked = false

    private var mMinFlingVelocity = DEFAULT_MIN_FLING_VELOCITY
    private var mMode = MODE_NORMAL

    private var mDragEdge = DRAG_EDGE_LEFT

    private var mDragDist = 0f
    private var mPrevX = -1f

    private var mDragHelper: ViewDragHelper? = null
    private var mGestureDetector: GestureDetectorCompat? = null

    private val mainOpenLeft: Int
        get() {
            when (mDragEdge) {
                DRAG_EDGE_LEFT -> return mRectMainClose.left + mSecondaryView!!.getWidth()

                DRAG_EDGE_RIGHT -> return mRectMainClose.left - mSecondaryView!!.getWidth()


                else -> return 0
            }
        }

    private val mainOpenTop: Int
        get() {
            when (mDragEdge) {
                DRAG_EDGE_LEFT -> return mRectMainClose.top

                DRAG_EDGE_RIGHT -> return mRectMainClose.top


                else -> return 0
            }
        }

    private val secOpenLeft: Int
        get() = mRectSecClose.left

    private val secOpenTop: Int
        get() = mRectSecClose.top

    private val mGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        internal var hasDisallowed = false

        override fun onDown(e: MotionEvent): Boolean {
            mIsScrolling = false
            hasDisallowed = false
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            mIsScrolling = true
            return false
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            mIsScrolling = true

            if (parent != null) {
                val shouldDisallow: Boolean

                if (!hasDisallowed) {
                    shouldDisallow = distToClosestEdge >= mMinDistRequestDisallowParent
                    if (shouldDisallow) {
                        hasDisallowed = true
                    }
                } else {
                    shouldDisallow = true
                }

                // disallow parent to intercept touch event so that the layout will work
                // properly on RecyclerView or view that handles scroll gesture.
                parent.requestDisallowInterceptTouchEvent(shouldDisallow)
            }

            return false
        }
    }

    private val distToClosestEdge: Int
        get() {
            when (mDragEdge) {
                DRAG_EDGE_LEFT -> {
                    val pivotRight = mRectMainClose.left + mSecondaryView!!.getWidth()

                    return Math.min(
                            mMainView!!.getLeft() - mRectMainClose.left,
                            pivotRight - mMainView!!.getLeft()
                    )
                }

                DRAG_EDGE_RIGHT -> {
                    val pivotLeft = mRectMainClose.right - mSecondaryView!!.getWidth()

                    return Math.min(
                            mMainView!!.getRight() - pivotLeft,
                            mRectMainClose.right - mMainView!!.getRight()
                    )
                }
            }

            return 0
        }

    private val halfwayPivotHorizontal: Int
        get() = if (mDragEdge == DRAG_EDGE_LEFT) {
            mRectMainClose.left + mSecondaryView!!.getWidth() / 2
        } else {
            mRectMainClose.right - mSecondaryView!!.getWidth() / 2
        }

    private val mDragHelperCallback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {

            if (isDragLocked)
                return false

            mDragHelper!!.captureChildView(mMainView!!, pointerId)
            return false
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            when (mDragEdge) {
                DRAG_EDGE_RIGHT -> return Math.max(
                        Math.min(left, mRectMainClose.left),
                        mRectMainClose.left - mSecondaryView!!.getWidth()
                )

                DRAG_EDGE_LEFT -> return Math.max(
                        Math.min(left, mRectMainClose.left + mSecondaryView!!.getWidth()),
                        mRectMainClose.left
                )

                else -> return child.getLeft()
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val velRightExceeded = pxToDp(xvel.toInt()) >= mMinFlingVelocity
            val velLeftExceeded = pxToDp(xvel.toInt()) <= -mMinFlingVelocity

            val pivotHorizontal = halfwayPivotHorizontal

            when (mDragEdge) {
                DRAG_EDGE_RIGHT -> if (velRightExceeded) {
                    close(true)
                } else if (velLeftExceeded) {
                    open(true)
                } else {
                    if (mMainView!!.getRight() < pivotHorizontal) {
                        open(true)
                    } else {
                        close(true)
                    }
                }

                DRAG_EDGE_LEFT -> if (velRightExceeded) {
                    open(true)
                } else if (velLeftExceeded) {
                    close(true)
                } else {
                    if (mMainView!!.getLeft() < pivotHorizontal) {
                        close(true)
                    } else {
                        open(true)
                    }
                }
            }
        }

        override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
            super.onEdgeDragStarted(edgeFlags, pointerId)

            if (isDragLocked) {
                return
            }

            val edgeStartLeft = mDragEdge == DRAG_EDGE_RIGHT && edgeFlags == ViewDragHelper.EDGE_LEFT

            val edgeStartRight = mDragEdge == DRAG_EDGE_LEFT && edgeFlags == ViewDragHelper.EDGE_RIGHT

            if (edgeStartLeft || edgeStartRight) {
                mDragHelper!!.captureChildView(mMainView!!, pointerId)
            }
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            if (mMode == MODE_SAME_LEVEL) {
                if (mDragEdge == DRAG_EDGE_LEFT || mDragEdge == DRAG_EDGE_RIGHT) {
                    mSecondaryView!!.offsetLeftAndRight(dx)
                } else {
                    mSecondaryView!!.offsetTopAndBottom(dy)
                }
            }
            ViewCompat.postInvalidateOnAnimation(this@SwipeRevealLayout)
        }
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @Nullable
    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(SUPER_INSTANCE_STATE, super.onSaveInstanceState())
        return super.onSaveInstanceState()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var state = state
        val bundle = state as Bundle?
        state = bundle!!.getParcelable(SUPER_INSTANCE_STATE)
        super.onRestoreInstanceState(state)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mGestureDetector!!.onTouchEvent(event)
        mDragHelper!!.processTouchEvent(event)
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (isDragLocked) {
            return super.onInterceptTouchEvent(ev)
        }

        mDragHelper!!.processTouchEvent(ev)
        mGestureDetector!!.onTouchEvent(ev)
        accumulateDragDist(ev)

        val couldBecomeClick = couldBecomeClick(ev)
        val settling = mDragHelper!!.viewDragState == ViewDragHelper.STATE_SETTLING
        val idleAfterScrolled = mDragHelper!!.viewDragState == ViewDragHelper.STATE_IDLE && mIsScrolling

        // must be placed as the last statement
        mPrevX = ev.x

        // return true => intercept, cannot trigger onClick event
        return !couldBecomeClick && (settling || idleAfterScrolled)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // get views
        if (childCount >= 2) {
            mSecondaryView = getChildAt(0)
            mMainView = getChildAt(1)
        } else if (childCount == 1) {
            mMainView = getChildAt(0)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (index in 0 until childCount) {
            val child = getChildAt(index)

            var left: Int
            var right: Int
            var top: Int
            var bottom: Int
            bottom = 0
            top = bottom
            right = top
            left = right

            val minLeft = paddingLeft
            val maxRight = Math.max(r - paddingRight - l, 0)
            val minTop = paddingTop
            val maxBottom = Math.max(b - paddingBottom - t, 0)

            var measuredChildHeight = child.measuredHeight
            var measuredChildWidth = child.measuredWidth

            // need to take account if child size is match_parent
            val childParams = child.layoutParams
            var matchParentHeight = false
            var matchParentWidth = false

            if (childParams != null) {
                matchParentHeight = childParams.height == ViewGroup.LayoutParams.MATCH_PARENT || childParams.height == ViewGroup.LayoutParams.FILL_PARENT
                matchParentWidth = childParams.width == ViewGroup.LayoutParams.MATCH_PARENT || childParams.width == ViewGroup.LayoutParams.FILL_PARENT
            }

            if (matchParentHeight) {
                measuredChildHeight = maxBottom - minTop
                childParams!!.height = measuredChildHeight
            }

            if (matchParentWidth) {
                measuredChildWidth = maxRight - minLeft
                childParams!!.width = measuredChildWidth
            }

            when (mDragEdge) {
                DRAG_EDGE_RIGHT -> {
                    left = Math.max(r - measuredChildWidth - paddingRight - l, minLeft)
                    top = Math.min(paddingTop, maxBottom)
                    right = Math.max(r - paddingRight - l, minLeft)
                    bottom = Math.min(measuredChildHeight + paddingTop, maxBottom)
                }

                DRAG_EDGE_LEFT -> {
                    left = Math.min(paddingLeft, maxRight)
                    top = Math.min(paddingTop, maxBottom)
                    right = Math.min(measuredChildWidth + paddingLeft, maxRight)
                    bottom = Math.min(measuredChildHeight + paddingTop, maxBottom)
                }
            }

            child.layout(left, top, right, bottom)
        }

        // taking account offset when mode is SAME_LEVEL
        if (mMode == MODE_SAME_LEVEL) {
            when (mDragEdge) {
                DRAG_EDGE_LEFT -> mSecondaryView!!.offsetLeftAndRight(-mSecondaryView!!.getWidth())

                DRAG_EDGE_RIGHT -> mSecondaryView!!.offsetLeftAndRight(mSecondaryView!!.getWidth())
            }
        }

        initRects()

        if (mIsOpenBeforeInit) {
            open(false)
        } else {
            close(false)
        }

    }

    /**
     * {@inheritDoc}
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        if (childCount < 2) {
            throw RuntimeException("Layout must have two children")
        }

        val params = layoutParams

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var desiredWidth = 0
        var desiredHeight = 0

        // first find the largest child
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            desiredWidth = Math.max(child.measuredWidth, desiredWidth)
            desiredHeight = Math.max(child.measuredHeight, desiredHeight)
        }
        // create new measure spec using the largest child width
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(desiredWidth, widthMode)
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, heightMode)

        val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measuredHeight = MeasureSpec.getSize(heightMeasureSpec)

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childParams = child.layoutParams

            if (childParams != null) {
                if (childParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                    child.minimumHeight = measuredHeight
                }

                if (childParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                    child.minimumWidth = measuredWidth
                }
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            desiredWidth = Math.max(child.measuredWidth, desiredWidth)
            desiredHeight = Math.max(child.measuredHeight, desiredHeight)
        }

        // taking accounts of padding
        desiredWidth += paddingLeft + paddingRight
        desiredHeight += paddingTop + paddingBottom

        // adjust desired width
        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = measuredWidth
        } else {
            if (params.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                desiredWidth = measuredWidth
            }

            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = if (desiredWidth > measuredWidth) measuredWidth else desiredWidth
            }
        }

        // adjust desired height
        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = measuredHeight
        } else {
            if (params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                desiredHeight = measuredHeight
            }

            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = if (desiredHeight > measuredHeight) measuredHeight else desiredHeight
            }
        }

        setMeasuredDimension(desiredWidth, desiredHeight)
    }

    override fun computeScroll() {
        if (mDragHelper!!.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    /**
     * Open the panel to show the secondary view
     */
    fun open(animation: Boolean) {
        mIsOpenBeforeInit = true

        if (animation) {
            mDragHelper!!.smoothSlideViewTo(mMainView!!, mRectMainOpen.left, mRectMainOpen.top)
        } else {
            mDragHelper!!.abort()

            mMainView!!.layout(
                    mRectMainOpen.left,
                    mRectMainOpen.top,
                    mRectMainOpen.right,
                    mRectMainOpen.bottom
            )

            mSecondaryView!!.layout(
                    mRectSecOpen.left,
                    mRectSecOpen.top,
                    mRectSecOpen.right,
                    mRectSecOpen.bottom
            )
        }

        ViewCompat.postInvalidateOnAnimation(this)
    }

    /**
     * Close the panel to hide the secondary view
     */
    fun close(animation: Boolean) {
        mIsOpenBeforeInit = false

        if (animation) {
            mDragHelper!!.smoothSlideViewTo(mMainView!!, mRectMainClose.left, mRectMainClose.top)
        } else {
            mDragHelper!!.abort()
            mMainView!!.layout(
                    mRectMainClose.left,
                    mRectMainClose.top,
                    mRectMainClose.right,
                    mRectMainClose.bottom
            )
            mSecondaryView!!.layout(
                    mRectSecClose.left,
                    mRectSecClose.top,
                    mRectSecClose.right,
                    mRectSecClose.bottom
            )
        }

        ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun initRects() {
        // close position of main view
        mRectMainClose.set(
                mMainView!!.getLeft(),
                mMainView!!.getTop(),
                mMainView!!.getRight(),
                mMainView!!.getBottom()
        )

        // close position of secondary view
        mRectSecClose.set(
                mSecondaryView!!.getLeft(),
                mSecondaryView!!.getTop(),
                mSecondaryView!!.getRight(),
                mSecondaryView!!.getBottom()
        )

        // open position of the main view
        mRectMainOpen.set(
                mainOpenLeft,
                mainOpenTop,
                mainOpenLeft + mMainView!!.getWidth(),
                mainOpenTop + mMainView!!.getHeight()
        )

        // open position of the secondary view
        mRectSecOpen.set(
                secOpenLeft,
                secOpenTop,
                secOpenLeft + mSecondaryView!!.getWidth(),
                secOpenTop + mSecondaryView!!.getHeight()
        )
    }

    private fun couldBecomeClick(ev: MotionEvent): Boolean {
        return isInMainView(ev) && !shouldInitiateADrag()
    }

    private fun isInMainView(ev: MotionEvent): Boolean {
        val x = ev.x
        val y = ev.y

        val withinVertical = mMainView!!.getTop() <= y && y <= mMainView!!.getBottom()
        val withinHorizontal = mMainView!!.getLeft() <= x && x <= mMainView!!.getRight()

        return withinVertical && withinHorizontal
    }

    private fun shouldInitiateADrag(): Boolean {
        val minDistToInitiateDrag = mDragHelper!!.touchSlop.toFloat()
        return mDragDist >= minDistToInitiateDrag
    }

    private fun accumulateDragDist(ev: MotionEvent) {
        val action = ev.action
        if (action == MotionEvent.ACTION_DOWN) {
            mDragDist = 0f
            return
        }

        val dragged = Math.abs(ev.x - mPrevX)

        mDragDist += dragged
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        if (attrs != null && context != null) {
            val a = context!!.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.SwipeRevealLayout,
                    0, 0
            )

            mDragEdge = a.getInteger(R.styleable.SwipeRevealLayout_dragFromEdge, DRAG_EDGE_LEFT)
            mMode = MODE_NORMAL
            mMinFlingVelocity = DEFAULT_MIN_FLING_VELOCITY
            mMinDistRequestDisallowParent = DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT
        }

        mDragHelper = ViewDragHelper.create(this, 1.0f, mDragHelperCallback)
        mDragHelper!!.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL)

        mGestureDetector = GestureDetectorCompat(context, mGestureListener)
    }

    private fun pxToDp(px: Int): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    companion object {

        private val SUPER_INSTANCE_STATE = "saved_instance_state_parcelable"

        private val DEFAULT_MIN_FLING_VELOCITY = 300 // dp per second
        private val DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT = 1 // dp

        val DRAG_EDGE_LEFT = 0x1
        val DRAG_EDGE_RIGHT = 0x1 shl 1

        /**
         * The secondary view will be under the main view.
         */
        val MODE_NORMAL = 0

        /**
         * The secondary view will stick the edge of the main view.
         */
        val MODE_SAME_LEVEL = 1
    }
}