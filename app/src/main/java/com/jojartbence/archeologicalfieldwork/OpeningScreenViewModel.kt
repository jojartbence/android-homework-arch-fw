package com.jojartbence.archeologicalfieldwork

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OpeningScreenViewModel: ViewModel() {

    val isTimerExpired = MutableLiveData<Boolean>(false)

    fun startTimer(timeInMs: Long) {
        val timer = object: CountDownTimer(timeInMs, timeInMs) {

            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                isTimerExpired.value = true
            }
        }
        timer.start()
    }
}