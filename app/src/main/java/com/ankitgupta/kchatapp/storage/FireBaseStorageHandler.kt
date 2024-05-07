package com.ankitgupta.kchatapp.storage

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FireBaseStorageHandler(private val storage: FirebaseStorage) {
    private val TAG = "FireBaseStorageHandler"
    private val storageRef = storage.reference
    suspend fun uploadProfile(uri: Uri, userName: String?): Uri? {
        Log.e(TAG,"IMAGEURI :$uri")
        val profileRef = if (userName != null) {
            storageRef.child("images/$userName/${UUID.randomUUID()}.jpg")
        } else {
            storageRef.child("images/${UUID.randomUUID()}.jpg")
        }

        return try {
            profileRef.putFile(uri).await()
            val downloadUri = profileRef.downloadUrl.await()
            downloadUri
        } catch (e: Exception) {
            Log.e(TAG, "Error uploading profile: ${e.message}")
            null
        }
    }

}