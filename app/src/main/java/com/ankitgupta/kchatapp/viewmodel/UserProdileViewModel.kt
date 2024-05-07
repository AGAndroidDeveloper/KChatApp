package com.ankitgupta.kchatapp.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitgupta.kchatapp.model.ProfileData
import com.ankitgupta.kchatapp.model.UseCase
import com.ankitgupta.kchatapp.response.FirebaseResultState
import com.ankitgupta.kchatapp.utill.Constant.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {
    private val _profileData: MutableStateFlow<ProfileData> = MutableStateFlow(ProfileData())
    val profileData: StateFlow<ProfileData> = _profileData.asStateFlow()

    private val _uploadProfileState: MutableStateFlow<FirebaseResultState> =
        MutableStateFlow(FirebaseResultState.Idle)
    val uploadProfileState: StateFlow<FirebaseResultState> = _uploadProfileState.asStateFlow()

    init {
        try {
            _profileData.value = useCase.profileDataManager.getProfileData("PROFILE_DATA")!!
        } catch (e: Exception) {
            Log.e("ee", e.message.toString())
        }
    }

    fun getUpdatedUserFromLocalStorage() {
        _profileData.value = useCase.profileDataManager.getProfileData("PROFILE_DATA")!!
    }

    private fun updateProfileDataInLocalStorage(profileData: ProfileData) {
        useCase.profileDataManager.saveProfileData(profileData, "PROFILE_DATA")
        getUpdatedUserFromLocalStorage()
    }

    fun uploadProfileInFirebaseStorage(uri: Uri) {
        updateProfileUploadingState(FirebaseResultState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val profileUrl =
                useCase.firebaseStorageHandler.uploadProfile(uri, _profileData.value.userName)
            if (profileUrl != null) {
                updateProfileUploadingState(FirebaseResultState.Success(profileUrl))
            } else {
                updateProfileUploadingState(FirebaseResultState.Failure("error in uploading profile image"))
            }
        }
    }

    private fun updateProfileUploadingState(state: FirebaseResultState) {
        _uploadProfileState.update {
            state
        }
    }

    fun updateUser(uri: Uri, auth: FirebaseAuth) {
        val user = auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            photoUri = uri
            displayName = "Ankit Gupta Android Developer"
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // save updated user detail on sharedPREF
                    Log.d(TAG, "new profile url,:${user.photoUrl}")
                    val profileData = ProfileData(
                        email = user.email,
                        uid = user.uid,
                        imageURL = user.photoUrl.toString(),
                        userName = user.displayName,
                        phoneNumber = user.phoneNumber
                    )

                    // update user also in firebase user node
                    updateUserInFirebaseRef(profileData)
                    updateProfileDataInLocalStorage(profileData)
                    Log.d(TAG, "User profile updated in firebase as well local")
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "exception ,$it")
            }
    }

    private fun updateUserInFirebaseRef(profileData: ProfileData) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.firebaseOperationRepository.saveUserProfileInRealTimeDb(profileData)
        }
    }

    override fun onCleared() {
        super.onCleared()

    }


}