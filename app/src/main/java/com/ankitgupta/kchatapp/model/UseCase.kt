package com.ankitgupta.kchatapp.model

import com.ankitgupta.kchatapp.repository.FirebaseOperationRepository
import com.ankitgupta.kchatapp.sharedpref.ProfileDataManager

data class UseCase(
    val profileDataManager: ProfileDataManager,
    val firebaseOperationRepository: FirebaseOperationRepository
) {

}