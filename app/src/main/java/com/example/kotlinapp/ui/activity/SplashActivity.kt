package com.example.kotlinapp.ui.activity

import android.content.Intent
import android.os.Bundle
import com.example.kotlinapp.MainActivity
import com.example.kotlinapp.R
import com.example.kotlinapp.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        binding= ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.apply {
            startBtn.setOnClickListener {
                startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            }
        }

    }
}