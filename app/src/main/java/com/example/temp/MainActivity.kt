package com.example.temp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.temp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val exchangeRate = mapOf("USD" to 1.0, "VND" to 23000.0, "EUR" to 0.85)

    private lateinit var sourceEditText: EditText
    private lateinit var targetEditText: EditText
    private lateinit var sourceSpinner: Spinner
    private lateinit var targetSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sourceEditText = binding.etSourceAmount
        targetEditText = binding.etTargetAmount
        sourceSpinner = binding.spSourceCurrency
        targetSpinner = binding.spTargetCurrency

        val currenies = exchangeRate.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currenies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spSourceCurrency.adapter = adapter
        binding.spTargetCurrency.adapter = adapter

        fun convertCurrency() {
            val sourceAmount = sourceEditText.text.toString().toDoubleOrNull() ?: return
            val sourceCurrency = sourceSpinner.selectedItem.toString()
            val targetCurrency = targetSpinner.selectedItem.toString()

            val sourceRate = exchangeRate[sourceCurrency] ?: return
            val targetRate = exchangeRate[targetCurrency] ?: return

            val targetAmount = sourceAmount * targetRate / sourceRate
            targetEditText.setText("%.2f".format(targetAmount))
        }

        binding.etSourceAmount.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // Nếu etSourceAmount được chọn, đặt nó là tiền tệ nguồn
                sourceEditText = binding.etSourceAmount
                sourceSpinner = binding.spSourceCurrency
                targetEditText = binding.etTargetAmount
                targetSpinner = binding.spTargetCurrency
            }
        }

        binding.etTargetAmount.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // Nếu etTargetAmount được chọn, đặt nó là tiền tệ nguồn
                sourceEditText = binding.etTargetAmount
                sourceSpinner = binding.spTargetCurrency
                targetEditText = binding.etSourceAmount
                targetSpinner = binding.spSourceCurrency
            }
        }

        // Thêm TextWatcher để cập nhật kết quả chuyển đổi
        binding.etSourceAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Chỉ chuyển đổi khi etSourceAmount là đơn vị nguồn
                if (binding.etSourceAmount.hasFocus()) {
                    convertCurrency()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etTargetAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Chỉ chuyển đổi khi etTargetAmount là đơn vị nguồn
                if (binding.etTargetAmount.hasFocus()) {
                    convertCurrency()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        binding.spSourceCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                convertCurrency()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.spTargetCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                convertCurrency()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }
}