package com.zj.demo.nestedscrolling

import android.content.Context

/**
 * Created by zhengjiong
 * date: 2017/12/5 22:16
 */
fun dp2px(context: Context, dp: Int): Int {
    return (context.resources.displayMetrics.density * dp + 0.5).toInt()
}