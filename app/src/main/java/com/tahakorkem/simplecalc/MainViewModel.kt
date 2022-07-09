package com.tahakorkem.simplecalc

import android.app.Application
import androidx.lifecycle.*
import com.tahakorkem.simplecalc.calculator.Calculator
import com.tahakorkem.simplecalc.calculator.Calculator.operators
import com.tahakorkem.simplecalc.util.fmt
import com.tahakorkem.simplecalc.util.getString
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _input = MutableStateFlow("")
    val input = _input.asStateFlow()

    private val _result = MutableStateFlow("")
    val result = _result.asStateFlow()

    private val _toastState = MutableSharedFlow<String>()
    val toastState = _toastState.asSharedFlow()

    private val _slideTextState = MutableSharedFlow<String>()
    val slideTextState = _slideTextState.asSharedFlow()

    fun insertComma() {
        if (_input.value.isEmpty()) {
            _input.value += '0'
        }
        if (_input.value.last() == ',') {
            return
        }
        if (isOperator(_input.value.last())) {
            _input.value += '0'
        }
        insertIntoInput(',')
    }

    fun insertOperator(o: Calculator.Operators) {
        if (_input.value.isEmpty()) {
            return
        }
        if (_input.value.last() == o.symbol) {
            return
        }
        if (isOperator(_input.value.last())) {
            _input.value = _input.value.dropLast(1)
        }
        insertIntoInput(o.symbol)
    }

    private fun isOperator(c: Char): Boolean = c in operators

    fun insertIntoInput(t: Char) {
        _input.value += t
        _result.value = calculate() ?: ""
    }

    fun clear() {
        _input.value = ""
        _result.value = ""
    }

    fun delete() {
        if (_input.value.isEmpty()) {
            return
        }
        _input.value = _input.value.dropLast(1)
        _result.value = calculate() ?: ""
    }

    private fun calculate(): String? {
        if (_input.value.isEmpty()) {
            return null
        }
        if (isOperator(_input.value.last())) {
            return null
        }
        if (_input.value.last() == ',') {
            return null
        }
        if (operators.none { it in _input.value }) {
            return null
        }
        val res = Calculator.calculate(_input.value) ?: return null
        return res.fmt()
    }

    fun takeResult() {
        val result = calculate() ?: run {
            viewModelScope.launch { _toastState.emit(getString(R.string.invalid_input)) }
            return
        }
        _input.value = ""
        viewModelScope.launch { _slideTextState.emit(result) }
    }

    fun onSlideComplete(result: String) {
        _input.value = result
        _result.value = ""
    }

}