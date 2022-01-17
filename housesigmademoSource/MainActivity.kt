package com.example.housesigmademo

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        map.setOnClickListener {
            startActivity(Intent(this,MapsActivity::class.java))
        }
        web.setOnClickListener {
            startActivity(Intent(this,WebViewActivity::class.java))
        }
        data.setOnClickListener {
            startActivity(Intent(this,LargeDataListActivity::class.java))
        }
        dayNight.setOnClickListener {

            val currentNightMode = (resources.configuration.uiMode
                    and Configuration.UI_MODE_NIGHT_MASK)
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
                Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
                Configuration.UI_MODE_NIGHT_UNDEFINED -> AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
            }

            finish()
            startActivity(Intent(this, this.javaClass))
            overridePendingTransition(R.anim.in_bottom, R.anim.out_bottom)
        }
    }
}