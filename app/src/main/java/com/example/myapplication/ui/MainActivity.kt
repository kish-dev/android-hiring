package com.example.myapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding

const val MAIN_ACTIVITY_TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: GenderAgeViewModel by viewModels { GenderAgeViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        binding.greetingsTextview.text = getText(R.string.greetings)
        binding.describeYourselfTextview.text = getText(R.string.describe_yourself)
        binding.sexTextview.text = getText(R.string.sex)
        binding.ageTextview.text = getText(R.string.age)

        binding.greetingsTextview.text = getText(R.string.greetings)

        binding.agePicker.minValue = 0
        binding.agePicker.maxValue = 99
        binding.agePicker.setOnValueChangedListener { picker, oldVal, newVal ->
            picker.value = newVal
            viewModel.pickAge(newVal)
        }

        binding.frameMaleBackground.setOnClickListener {
            viewModel.pickGender(Gender.MALE)
        }

        binding.frameFemaleBackground.setOnClickListener {
            viewModel.pickGender(Gender.FEMALE)
        }

        binding.nextButton.isActivated

        binding.nextButton.setOnClickListener {
            viewModel.getAllowState()
        }

        observe()
    }

    private fun observe() {
        viewModel.uiState.observe(this) { uiState ->
            when(uiState) {
                is UiState.Error -> {
                    binding.errorTextview.visibility = View.VISIBLE
                    binding.errorTextview.text = getText(R.string.error)
                    binding.allowedTextview.visibility = View.GONE
                    binding.nextButton.text = getText(R.string.next)
                    binding.nextButton.isActivated = false
                    binding.nextButton.setBackgroundColor(getColor(R.color.secondary_gray))
                    observeGender(Gender.UNKNOWN)
                    observeAge(0)
                    Log.e(MAIN_ACTIVITY_TAG, uiState.exception)
                }
                UiState.Idle -> {
                    binding.nextButton.isActivated = false
                    binding.nextButton.setBackgroundColor(getColor(R.color.secondary_gray))
                    binding.errorTextview.visibility = View.GONE
                    binding.allowedTextview.visibility = View.GONE
                    binding.nextButton.setBackgroundColor(getColor(R.color.primary_green))
                    observeGender(Gender.UNKNOWN)
                    observeAge(0)
                }
                is UiState.Data -> {
                    binding.errorTextview.visibility = View.GONE
                    if(uiState.isLoading) {
                        binding.nextButton.isActivated = false
                        binding.nextButton.setBackgroundColor(getColor(R.color.secondary_gray))
                        binding.nextButton.text = getText(R.string.loading)
                    } else {
                        observeGender(uiState.gender)
                        observeAge(uiState.pickedAge)
                        observeButton(uiState.gender, uiState.pickedAge)
                        observeResult(uiState.allowed)
                    }
                }
            }
        }
    }

    private fun observeResult(allowedState: ALLOWED_STATE) {
        when(allowedState) {
            ALLOWED_STATE.ALLOWED -> {
                binding.allowedTextview.text = getText(R.string.allowed)
                binding.allowedTextview.visibility = View.VISIBLE
            }
            ALLOWED_STATE.DISALLOWED -> {
                binding.allowedTextview.text = getText(R.string.disallowed)
                binding.allowedTextview.visibility = View.VISIBLE
            }
            ALLOWED_STATE.UNKNOWN -> {
                binding.allowedTextview.visibility = View.GONE
            }
        }
    }

    private fun observeButton(gender: Gender, pickedAge: Int) {
        if (gender != Gender.UNKNOWN && pickedAge in 1..99) {
            binding.nextButton.isActivated = true
            binding.nextButton.setBackgroundColor(getColor(R.color.primary_green))
        } else {
            binding.nextButton.isActivated = false
            binding.nextButton.setBackgroundColor(getColor(R.color.secondary_gray))
        }
        binding.nextButton.text = getText(R.string.next)
    }

    private fun observeGender(gender: Gender) {
        when (gender) {
            Gender.FEMALE -> {
                binding.frameFemaleBackground
                    .setBackgroundResource(R.drawable.pink_background)
                binding.frameMaleBackground
                    .setBackgroundResource(R.drawable.shadow_background)
            }
            Gender.MALE -> {
                binding.frameFemaleBackground
                    .setBackgroundResource(R.drawable.shadow_background)
                binding.frameMaleBackground
                    .setBackgroundResource(R.drawable.blue_background)
            }
            Gender.UNKNOWN -> {
                binding.frameFemaleBackground
                    .setBackgroundResource(R.drawable.shadow_background)
                binding.frameMaleBackground
                    .setBackgroundResource(R.drawable.shadow_background)
            }
        }
    }

    private fun observeAge(age: Int) {
        binding.agePicker.value = age
    }
}