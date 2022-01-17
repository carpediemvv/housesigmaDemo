package com.example.housesigmademo

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        wb_content.loadUrl("https://test.housesigma.com/static/dark_test.html")
        if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            val currentNightMode = (resources.configuration.uiMode
                    and Configuration.UI_MODE_NIGHT_MASK)
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    WebSettingsCompat.setForceDark(wb_content.settings, WebSettingsCompat.FORCE_DARK_OFF)
                }
                Configuration.UI_MODE_NIGHT_YES ->{
                    WebSettingsCompat.setForceDark(wb_content.settings, WebSettingsCompat.FORCE_DARK_ON)
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    WebSettingsCompat.setForceDark(wb_content.settings, WebSettingsCompat.FORCE_DARK_AUTO)
                }
            }

        }
    }
}