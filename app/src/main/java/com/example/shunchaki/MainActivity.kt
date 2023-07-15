package com.example.shunchaki

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shunchaki.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startstopButton.setOnClickListener { startStopTime() }
        binding.reserButton.setOnClickListener { resetTimer() }

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
    }

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        binding.timeTv.text = getTimeStringFromDouble(time)
    }

    private fun startStopTime() {
        if (timerStarted) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIMER_EXTRA, time)
        startService(serviceIntent)
        binding.startstopButton.text = "stop"
        timerStarted = true
    }

    private fun stopTimer() {
        stopService(serviceIntent)
        binding.startstopButton.text = "start"
        timerStarted = false
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIMER_EXTRA, 0.0)
            binding.timeTv.text = getTimeStringFromDouble(time)
        }

    }

    private fun getTimeStringFromDouble(time: Double): String {
        var resultInt = time.roundToInt()
        var hours = resultInt % 86400 / 3600
        var minutes = resultInt % 86400 % 3600 / 60
        var seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String =
        String.format("%02d:%02d:%02d", hour, min, sec)


}