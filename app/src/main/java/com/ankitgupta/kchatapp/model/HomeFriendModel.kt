package com.ankitgupta.kchatapp.model

import android.net.Uri

data class HomeFriendModel(
    val uri: Uri? = null,
    val name: String? = null,
    val lastMessage: String? = null
)
