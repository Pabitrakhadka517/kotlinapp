package com.example.kotlinapp.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinapp.Adapter.NearDoctorsAdapter
import com.example.kotlinapp.databinding.ActivityDashboardBinding
import com.example.kotlinapp.viewmodel.MainViewModel

class DashboardActivity : BaseActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val viewModel=MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNearByDoctor()


    }

    private fun initNearByDoctor() {
        binding.apply {
            progressBar.visibility=View.VISIBLE
            viewModel.loadDoctors().observe(this@DashboardActivity, Observer {
                topView.layoutManager=
                    LinearLayoutManager(this@DashboardActivity,LinearLayoutManager.VERTICAL,false)
                topView.adapter=NearDoctorsAdapter(it)
                progressBar.visibility=View.GONE


            })
        }
    }
}
