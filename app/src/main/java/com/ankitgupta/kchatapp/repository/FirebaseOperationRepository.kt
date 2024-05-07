package com.ankitgupta.kchatapp.repository

import android.util.Log
import com.ankitgupta.kchatapp.model.ProfileData
import com.ankitgupta.kchatapp.response.MyResult
import com.ankitgupta.kchatapp.utill.Constant.TAG
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseOperationRepository(private val db: FirebaseDatabase) {
    suspend fun saveUserProfileInRealTimeDb(profileData: ProfileData): MyResult<String> {
        return suspendCoroutine { continuation ->
            val userMapData = profileData.profileDataToMap()
            val reference = db.getReference("users")

            profileData.uid?.let { uid ->
                reference.child(uid).setValue(profileData)
                    .addOnSuccessListener {
                        continuation.resume(MyResult.Success("Data added in DB successfully"))
                        Log.e(TAG, "Data added in DB successfully")
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(MyResult.Error(Exception(exception)))
                        Log.e(TAG, "Exception: ${exception.message}")
                    }
            } ?: continuation.resume(MyResult.Error(Exception("UID is null")))
        }
    }


}