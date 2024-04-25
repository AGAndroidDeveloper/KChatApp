package com.ankitgupta.kchatapp.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ankitgupta.kchatapp.ProfileData
import com.ankitgupta.kchatapp.model.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {
    private val _profileData: MutableStateFlow<ProfileData> = MutableStateFlow(ProfileData())
    val profileData: StateFlow<ProfileData> = _profileData.asStateFlow()

    init {
        try {
            _profileData.value = useCase.profileDataManager.getProfileData("PROFILE_DATA")!!
        } catch (e: Exception) {
            Log.e("ee", e.message.toString())
        }
    }


}