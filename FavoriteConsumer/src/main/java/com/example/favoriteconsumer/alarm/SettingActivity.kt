package com.example.favoriteconsumer.alarm

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.favoriteconsumer.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    companion object {
        const val PREFERENCE_NAME = "SettingActivity"
    }
    private lateinit var binding: ActivitySettingBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Setting"

        alarmReceiver = AlarmReceiver()
        sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        setSwitch()
        binding.switchAction.setOnCheckedChangeListener{_, isChecked->
            if (isChecked){
                alarmReceiver.setRepeatingAlarm(
                    this,
                    "type",
                    "Come Back and Find Other User in Github",
                )
                sharedPreferences.edit().putBoolean("alarm",true).apply()
            }else{
                alarmReceiver.cancelAlarm(this)
                sharedPreferences.edit().putBoolean("alarm",false).apply()
            }
        }

    }

    private fun setSwitch(){
        val value = sharedPreferences.getBoolean("alarm", true)
        if (value == true){
            binding.switchAction.isChecked = true
        }
    }
}