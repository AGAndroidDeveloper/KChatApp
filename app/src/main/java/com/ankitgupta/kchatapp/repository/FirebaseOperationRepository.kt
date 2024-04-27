package com.ankitgupta.kchatapp.repository

import android.util.Log
import com.ankitgupta.kchatapp.ProfileData
import com.ankitgupta.kchatapp.authentication.MainActivity
import com.ankitgupta.kchatapp.response.MyResult
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseOperationRepository(private val db: FirebaseDatabase) {
    suspend fun saveUserProfileInRealTimeDb(profileData: ProfileData): MyResult<String> {
        return suspendCoroutine { continuation ->
            val userMapData = profileData.profileDataToMap()
            val reference = db.getReference("users")
            profileData.uid?.let { uid ->
                reference.child(uid).setValue(userMapData)
                    .addOnSuccessListener {
                        continuation.resume(MyResult.Success("Data added in DB successfully"))
                        Log.e(MainActivity.TAG, "Data added in DB successfully")
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(MyResult.Error(Exception(exception)))
                        Log.e(MainActivity.TAG, "Exception: ${exception.message}")
                    }
            } ?: continuation.resume(MyResult.Error(Exception("UID is null")))
        }
    }


}