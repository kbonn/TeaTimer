package com.bonndev.teatimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    enum class TimerState {
        Running, Stopped, Paused
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState = TimerState.Stopped
    private var secondsRemaining = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        black_tea_button.setOnClickListener { v ->
            startTimer()
            timerState = TimerState.Running
        }
    }

    private fun startTimer() {
        timerState = TimerState.Running


    }


}