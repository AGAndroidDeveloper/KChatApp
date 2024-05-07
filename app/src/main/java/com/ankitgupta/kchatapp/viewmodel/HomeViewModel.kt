package com.ankitgupta.kchatapp.viewmodel

import androidx.lifecycle.ViewModel
import com.ankitgupta.kchatapp.model.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(private val useCase: UseCase) :ViewModel() {



}