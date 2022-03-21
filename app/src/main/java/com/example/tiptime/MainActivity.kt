package com.example.tiptime

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDecreasePct.setOnClickListener{
            decreasePercent()
            hideKeyboard()
        }

        binding.btnIncreasePct.setOnClickListener{
            increasePercent()
            hideKeyboard()
        }

        binding.btnDecreasePer.setOnClickListener{
            decreasePeople()
            hideKeyboard()
        }

        binding.btnIncreasePer.setOnClickListener{
            increasePeople()
            hideKeyboard()
        }

        binding.btnCalculate.setOnClickListener{
            calculateTip()
            hideKeyboard()
        }

        binding.etCostOfService.onFocusChangeListener = View.OnFocusChangeListener { _ , b ->
            if (b) {
                binding.etCostOfService.textSize = 26f
                showKeyboard()
            }
        }

        binding.etCostOfService.setOnKeyListener {view, keyCode, _ -> handleKeyEvent(view, keyCode)}
    }

    private fun calculateTip() {

        val stringInCostField = binding.etCostOfService.text.toString()
        val cost = stringInCostField.toDoubleOrNull()
        val percentString = binding.tvTipPercentage.text.toString().filter { it.isDigit() }
        val pct = percentString.toDouble()/100
        val num = binding.tvNumberPeople.text.toString().toInt()
        val tip: Double
        val splitTip: Double

        if (cost == null) {
            binding.tvTipResultAmount.text = ""
            binding.tvTipResultPerPersonAmt.text = ""
            binding.tvTotalAmount.text = ""
            binding.tvTotalAmountPer.text = ""
            return
        }

        if (!binding.swRoundUp.isChecked) {
            tip = cost*pct
            splitTip = tip/num
        } else {
            tip = ceil(cost*pct)
            splitTip = tip/num
        }

        val total = cost + tip

        binding.tvTipResultAmount.text = NumberFormat.getCurrencyInstance().format(tip).toString()
        binding.tvTipResultPerPersonAmt.text = NumberFormat.getCurrencyInstance().format(splitTip).toString()
        binding.tvTotalAmount.text = NumberFormat.getCurrencyInstance().format(total).toString()
        binding.tvTotalAmountPer.text = NumberFormat.getCurrencyInstance().format(total/num).toString()

    }

    private fun increasePercent() {

        val percentString = binding.tvTipPercentage.text.toString().filter { it.isDigit() }
        var pct = percentString.toInt()

        if (pct < 100) {
            pct++
            Log.i("debug", "$pct")
            binding.tvTipPercentage.text = getString(R.string.percent_num, pct)
        }

    }

    private fun decreasePercent() {

        val percentString = binding.tvTipPercentage.text.toString().filter { it.isDigit() }
        var pct = percentString.toInt()

        if (pct > 0) {
            pct--
            binding.tvTipPercentage.text = getString(R.string.percent_num, pct)
        }

    }

    private fun increasePeople() {

        var num = binding.tvNumberPeople.text.toString().toInt()
        num++
        binding.tvNumberPeople.text = num.toString()

        if (num > 1) {
            binding.tvTipResultPerPersonAmt.visibility = View.VISIBLE
            binding.tvTipResultPerPerson.visibility = View.VISIBLE
            binding.tvTotalAmountPer.visibility = View.VISIBLE
            binding.tvTotalPer.visibility = View.VISIBLE
        }
    }

    private fun decreasePeople() {

        var num = binding.tvNumberPeople.text.toString().toInt()
        if (num > 1) {
            num--
            binding.tvNumberPeople.text = num.toString()
        }

        if (num == 1) {
            binding.tvTipResultPerPersonAmt.visibility = View.INVISIBLE
            binding.tvTipResultPerPerson.visibility = View.INVISIBLE
            binding.tvTotalAmountPer.visibility = View.INVISIBLE
            binding.tvTotalPer.visibility = View.INVISIBLE
        }

    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManger = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManger.hideSoftInputFromWindow(view.windowToken, 0)
    }


    private fun Activity.showKeyboard() {
        showKeyboard(currentFocus ?: View(this))
    }

    private fun Context.showKeyboard(view: View) {
        val inputMethodManger = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManger.showSoftInput(view,0)
    }

    private fun handleKeyEvent(view:View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

}