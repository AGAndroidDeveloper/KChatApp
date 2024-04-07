package com.ankitgupta.kchatapp

import android.net.Uri

data class ProfileData(
    val email: String? = null,
    val uid: String? = null,
    val imageURL: Uri? = null,
    val userName :String? = null
)
