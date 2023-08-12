package me.superpenguin.superglue.superfoundations

import java.text.DecimalFormat
import kotlin.math.floor


fun Double.round(precision: Int): Double = 10.pow(precision).let { if (precision == 0) floor(this) else (this*it)/it }
fun Float.round(precision: Int): Float = toDouble().round(precision).toFloat()
fun Double.format(decimals: Int = 2) = DecimalFormat.getInstance().apply { maximumFractionDigits = decimals }.apply { isGroupingUsed = true }.apply { minimumFractionDigits = 2 }.format(this)
fun Int.format() = DecimalFormat.getIntegerInstance().apply { isGroupingUsed = true }.format(this)
infix fun Int.pow(other: Int): Int = (1..other).fold(1) { acc, _ -> acc * this }