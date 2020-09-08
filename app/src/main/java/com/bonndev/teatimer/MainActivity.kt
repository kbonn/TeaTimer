package com.bonndev.teatimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.bonndev.teatimer.util.PrefUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

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

        black_tea_button.setOnClickListener {
            if (timerState == TimerState.Running || timerState == TimerState.Paused) {
                timer.cancel()
                onTimerFinished()
            }
            PrefUtil.setTimerLength(4L, this)
            initTimer()
            startTimer()
            timerState = TimerState.Running
        }

        green_tea_button.setOnClickListener {
            if (timerState == TimerState.Running || timerState == TimerState.Paused) {
                timer.cancel()
                onTimerFinished()
            }
            PrefUtil.setTimerLength(3L, this)
            initTimer()
            startTimer()
            timerState = TimerState.Running
        }

        white_tea_button.setOnClickListener {
            if (timerState == TimerState.Running || timerState == TimerState.Paused) {
                timer.cancel()
                onTimerFinished()
            }
            PrefUtil.setTimerLength(2L, this)
            initTimer()
            startTimer()
            timerState = TimerState.Running
        }

        play_button.setOnClickListener {
            startTimer()
            timerState = TimerState.Running
        }

        pause_button.setOnClickListener {
            timer.cancel()
            timerState = TimerState.Paused
        }

        stop_button.setOnClickListener {
            timer.cancel()
            onTimerFinished()
        }
    }

    override fun onResume() {
        super.onResume()

        initTimer()
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.Running) {
            timer.cancel()
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
    }

    private fun startTimer() {
        timerState = TimerState.Running

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }

            override fun onFinish() {
                onTimerFinished()
            }
        }.start()
    }

    private fun onTimerFinished() {
        timerState = TimerState.Stopped
        setNewTimerLength()

        PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds

        updateCountdownUI()
    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        countdown_textview.text = "$minutesUntilFinished:${
            if (secondsStr.length == 2) secondsStr
            else "0" + secondsStr}"
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
    }

    private fun initTimer() {
        timerState = PrefUtil.getTimerState(this)

        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(this)
        else
            timerLengthSeconds

        if (timerState == TimerState.Running)
            startTimer()

        updateCountdownUI()
    }
}