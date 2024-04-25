package com.ankitgupta.kchatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitgupta.kchatapp.ProfileData
import com.ankitgupta.kchatapp.model.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {
    private val _userState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val userState: StateFlow<Boolean> = _userState


    fun updateUserState(userLoggedIn: Boolean) {
        _userState.update {
            userLoggedIn
        }
    }

    fun saveUserProfileDataInLocalStorage(profileData: ProfileData) {
        viewModelScope.launch {
            useCase.profileDataManager.saveProfileData(
                profileData = profileData,
                key = "PROFILE_DATA"
            )
        }
    }

}