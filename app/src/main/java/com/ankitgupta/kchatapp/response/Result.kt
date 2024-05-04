package com.ankitgupta.kchatapp.response
//
sealed class MyResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : MyResult<T>()
    data class Error(val exception: Exception) : MyResult<Nothing>()
}


sealed class FirebaseResultState() {
    data object Idle : FirebaseResultState()
    data object Loading : FirebaseResultState()
    data class Success(val data: Any) : FirebaseResultState()
    data class Failure(val errorMessage: String) : FirebaseResultState()
}