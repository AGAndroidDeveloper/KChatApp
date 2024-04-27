package com.ankitgupta.kchatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitgupta.kchatapp.ProfileData
import com.ankitgupta.kchatapp.model.UseCase
import com.ankitgupta.kchatapp.response.FirebaseResultState
import com.ankitgupta.kchatapp.response.MyResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {
    private val _userState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val userState: StateFlow<Boolean> = _userState

    private val _userProfileState: MutableStateFlow<FirebaseResultState> =
        MutableStateFlow(FirebaseResultState.Idle)

    val observeProfileResponseState: StateFlow<FirebaseResultState> = _userProfileState.asStateFlow()

    fun updateUserState(userLoggedIn: Boolean) {
        _userState.update {
            userLoggedIn
        }
    }

    private fun updateUserProfileResponseState(state: FirebaseResultState) {
        _userProfileState.update {
            state
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

    fun saveUserInFireBase(profileData: ProfileData) {
        updateUserProfileResponseState(FirebaseResultState.Loading)
        viewModelScope.launch {
            val result =
                useCase.firebaseOperationRepository.saveUserProfileInRealTimeDb(profileData)

            when (result) {
                is MyResult.Success -> {
                    updateUserProfileResponseState(FirebaseResultState.Success(result.data))
                }

                is MyResult.Error -> {
                    result.exception.message?.let {
                        FirebaseResultState.Failure(it)
                    }?.let { updateUserProfileResponseState(it) }
                }
            }
        }

    }

}