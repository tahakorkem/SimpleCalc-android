package com.tahakorkem.simplecalc.util

import android.app.Application
import android.content.Context
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel

@ColorInt
fun Context.themeColor(@AttrRes attrRes: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
}

fun Spannable.setColorSpan(range: IntRange, @ColorInt color: Int) {
    this.setSpan(
        ForegroundColorSpan(color),
        range.first,
        range.last + 1,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun AndroidViewModel.getString(@StringRes resId: Int): String =
    getApplication<Application>().getString(resId)

fun Number.dp(context: Context): Float {
    val density: Float = context.resources.displayMetrics.density
    return this.toFloat() / density
}