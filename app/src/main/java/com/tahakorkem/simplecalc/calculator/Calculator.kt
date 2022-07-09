package com.tahakorkem.simplecalc.calculator

import java.lang.Exception
import kotlin.math.pow

object Calculator {

    fun calculate(input: String): Double? = try {
        val operators = operatorRegex.findAll(input)
            .map { it.value.single() }
            .map { s -> Operators.values().find { it.symbol == s }!! }
            .toMutableList()
        val operands = input.split(operatorRegex)
            .map { it.replace(",", ".").toDouble() }
            .toMutableList()

        fun op(vararg operatorsAllowed: Operators) {
            var i = 0
            while (i < operators.size) {
                val operator = operators[i]

                if (operator !in operatorsAllowed) {
                    i++
                    continue
                }

                val operand1 = operands[i]
                val operand2 = operands[i + 1]

                val r = operator.operation(operand1, operand2)
                operators.removeAt(i)
                operands.removeAt(i)
                operands.removeAt(i)
                operands.add(i, r)
            }
        }

        op(Operators.EXPONENTIATE)
        op(Operators.MULTIPLY, Operators.DIVIDE)
        op(Operators.ADD, Operators.SUBTRACT)

        operands.single()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    enum class Operators(val symbol: Char, val operation: (Double, Double) -> Double) {
        MULTIPLY(symbol = '×', operation = { o1, o2 -> o1 * o2 }),
        DIVIDE(symbol = '÷', operation = { o1, o2 -> o1 / o2 }),
        ADD(symbol = '+', operation = { o1, o2 -> o1 + o2 }),
        SUBTRACT(symbol = '-', operation = { o1, o2 -> o1 - o2 }),
        EXPONENTIATE(symbol = '^', operation = { o1, o2 -> o1.pow(o2) }),
    }

    val operators = Operators.values().map { it.symbol } //arrayOf('÷', '×', '+', '-', '^')
    val operatorRegex =
        operators.joinToString(prefix = "(?<!^)[", postfix = "]", separator = "") { "\\$it" }.toRegex()

}