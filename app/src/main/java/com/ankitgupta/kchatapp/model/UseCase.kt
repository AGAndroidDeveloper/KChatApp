package com.ankitgupta.kchatapp.model

import com.ankitgupta.kchatapp.repository.FirebaseOperationRepository
import com.ankitgupta.kchatapp.sharedpref.ProfileDataManager
import com.ankitgupta.kchatapp.storage.FireBaseStorageHandler
import com.google.firebase.storage.FirebaseStorage

data class UseCase(
    val profileDataManager: ProfileDataManager,
    val firebaseOperationRepository: FirebaseOperationRepository,
    val firebaseStorageHandler : FireBaseStorageHandler
) {

}