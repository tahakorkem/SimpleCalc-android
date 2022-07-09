package com.tahakorkem.simplecalc.util

import java.math.RoundingMode
import java.text.DecimalFormat

fun Double.fmt(): String {
    val df = DecimalFormat("#.##########")
    df.roundingMode = RoundingMode.FLOOR
    return df.format(this).replace('.', ',')
}
//    if (this == toLong().toDouble())
//        String.format("%d", toLong())
//    else
//        String.format("%s", this).replace('.', ',')
