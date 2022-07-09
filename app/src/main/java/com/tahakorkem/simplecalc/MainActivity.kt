package com.tahakorkem.simplecalc

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.tahakorkem.simplecalc.calculator.Calculator.operatorRegex
import com.tahakorkem.simplecalc.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.tahakorkem.simplecalc.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this).get()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // shared stateler toplanır
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.slideTextState.collect { resultText -> animateText(resultText) }
                }
                launch {
                    viewModel.toastState.collect { this@MainActivity.toast(it) }
                }
            }
        }

        // edittext içerindeki operatörler renklendirilir
        binding.input.doAfterTextChanged { text ->
            if (text == null)
                return@doAfterTextChanged

            val results = operatorRegex.findAll(text)

            results.forEach { text.setColorSpan(it.range, themeColor(R.attr.colorSecondary)) }
        }
    }

    private fun animateText(finalText: String) {
        val pvhY = PropertyValuesHolder.ofFloat("y", binding.input.y)
        val pvhTextSize = PropertyValuesHolder.ofFloat(
            "textSize",
            binding.result.textSize.dp(this),
            binding.input.textSize.dp(this),
        )
        ObjectAnimator.ofPropertyValuesHolder(binding.result, pvhY, pvhTextSize).apply {
            duration = 500
            doOnEnd { animator ->
                viewModel.onSlideComplete(finalText)
                animator.reset()
            }
            start()
        }
    }
}