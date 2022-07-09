package com.tahakorkem.simplecalc.util

import android.animation.Animator
import android.view.animation.LinearInterpolator

fun Animator.reset() {
    removeAllListeners()
    startDelay = 5
    duration = 0
    setInterpolator {
        1 - LinearInterpolator().getInterpolation(it)
    }
    start()
}