package com.ankitgupta.kchatapp.di

import com.ankitgupta.kchatapp.repository.FirebaseOperationRepository
import com.google.firebase.database.FirebaseDatabase
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
    fun provideFirebaseOpRepoInstance(db: FirebaseDatabase) = FirebaseOperationRepository(db)
}