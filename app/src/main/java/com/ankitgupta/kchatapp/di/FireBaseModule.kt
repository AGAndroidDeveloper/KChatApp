package com.ankitgupta.kchatapp.di

import com.ankitgupta.kchatapp.repository.FirebaseOperationRepository
import com.ankitgupta.kchatapp.storage.FireBaseStorageHandler
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FireBaseModule {


    @Provides
    @Singleton
    fun provideDbInstance() =
        FirebaseDatabase.getInstance("https://ankit-chat-app-9cf7c-default-rtdb.asia-southeast1.firebasedatabase.app")

    @Provides
    @Singleton
    fun provideFirebaseStorage() = Firebase.storage

    @Provides
    @Singleton
    fun provideFirebaseStorageRef(storage: FirebaseStorage): FireBaseStorageHandler {
        return FireBaseStorageHandler(storage)
    }


    @Provides
    @Singleton
    fun provideFirebaseOpRepoInstance(db: FirebaseDatabase) = FirebaseOperationRepository(db)
}