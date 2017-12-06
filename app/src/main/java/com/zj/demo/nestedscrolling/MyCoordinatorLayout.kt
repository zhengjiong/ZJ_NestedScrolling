package com.zj.demo.nestedscrolling

import android.content.Context
import android.support.v4.view.NestedScrollingParent2
import android.support.v4.view.ViewCompat
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 * Created by zhengjiong
 * date: 2017/12/5 21:22
 */
class MyCoordinatorLayout : LinearLayout, NestedScrollingParent2 {

    lateinit var scrollView: NestedScrollView

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * onStartNestedScroll该方法，一定要按照自己的需求返回true，该方法决定了当前控件是
     * 否能接收到其内部View(非并非是直接子View)滑动时的参数；假设你只涉及到纵向滑动，这里
     * 可以根据nestedScrollAxes这个参数，进行纵向判断。
     * @type the type of input which cause this scroll event
     */
    override fun onStartNestedScroll(child: View, target: View, @ViewCompat.ScrollAxis axes: Int, @ViewCompat.NestedScrollType type: Int): Boolean {

        val result = (axes == ViewCompat.SCROLL_AXIS_VERTICAL)
        println("onStartNestedScroll result=${result} type=${type}")
        return result
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        println("onNestedScrollAccepted")
    }

    /**
     * onNestedPreScroll该方法的会传入内部View移动的dx,dy，如果你需要消耗一定的dx,dy，
     * 就通过最后一个参数consumed进行指定，例如我要消耗一半的dy，就可以写consumed[1]=dy/2
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray?, type: Int) {
        println("onNestedPreScroll  dy=${dy}    scrollY=${scrollY}   150=${dp2px(context, 150)}")

        //dy>0, 从下往上滑动,dy<0,从上往上滑动

        //hidenTop为true代表:从下向上滑动并且头部还显示出来了
        val hiddenTop = dy > 0 && (scrollY < dp2px(context, 150))

        //showTop为true代表:从上往下滑动并且头部还没有全部显示出来并且下面的scrollview不能从上向下滑动
        val showTop = dy < 0 && scrollY > 0 && !ViewCompat.canScrollVertically(target, -1)//-1代表从上向下滑动, 1代表从下向上滑动
        if (hiddenTop) {
            if (dy + scrollY > dp2px(context, 150)) {//防止超过最大值
                consumed!![1] = dp2px(context, 150) - scrollY
            } else {
                consumed!![1] = dy
            }
            scrollBy(0, consumed[1])
        } else if (showTop) {
            if (scrollY + dy < 0) {
                consumed!![1] = scrollY
            } else {
                consumed!![1] = dy
            }
            consumed!![1] = dy
            if (scrollY + consumed[1] <= 0) {
                scrollBy(0, -scrollY)
            } else {
                scrollBy(0, consumed[1])
            }

        }

    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {

    }

    override fun onStopNestedScroll(target: View, type: Int) {

    }

    /**
     * 用户松开手指并且会发生惯性动作之前调用，参数提供了速度信息，可以根据这些速度信息
     * 决定最终状态，比如滚动Header，是让Header处于展开状态还是折叠状态。返回true 表
     * 示消费了fling, onNestedFling将不会被调用, childview也不会有惯性效果
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param velocityX         x 方向的速度
     * @param velocityY         y 方向的速度
     * @return 返回true 表示消费了fling, onNestedFling将不会被调用
     */
    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    /**
     * onNestedFling你可以捕获对内部View的fling事件，如果return true则表示拦截掉内部View的事件。
     * 你可以捕获对内部View的fling事件，如果return true则表示拦截掉内部View的事件。
     */
    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        scrollView = findViewById(R.id.nestedscrollview)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val scrollViewParams = scrollView.layoutParams

        //设置scrollview的高度为总高度-顶部tab的高度(因为nexus6p有问题, 故先这样写)
        scrollViewParams.height = measuredHeight + dp2px(context, 25)// - dp2px(context, 50)

        //设置总高度
        //setMeasuredDimension(measuredWidth, measuredHeight + dp2px(context, 200))
        //setMeasuredDimension(measuredWidth, getChildAt(0).measuredHeight + getChildAt(1).measuredHeight + scrollViewParams.height)
    }
}