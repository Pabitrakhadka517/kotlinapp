package com.example.kotlinapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinapp.model.DoctorsModel
import com.example.kotlinapp.repository.MainRepository

class MainViewModel():ViewModel() {

    private val repository=MainRepository()

    fun loadDoctors(): LiveData<MutableList<DoctorsModel>> {
        return repository.load()
    }
}