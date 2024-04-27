package com.ankitgupta.kchatapp.di

import android.content.Context
import com.ankitgupta.kchatapp.model.UseCase
import com.ankitgupta.kchatapp.repository.FirebaseOperationRepository
import com.ankitgupta.kchatapp.sharedpref.ProfileDataManager
import com.ankitgupta.kchatapp.storage.FireBaseStorageHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalStorage {

    @Provides
    @Singleton
    fun profileProfileDataManager(@ApplicationContext context: Context): ProfileDataManager {
        return ProfileDataManager(context)
    }


    @Provides
    @Singleton
    fun provideUseCase(
        profileDataManager: ProfileDataManager,
        firebaseOperationRepository: FirebaseOperationRepository,
        fireBaseStorageHandler: FireBaseStorageHandler
    ): UseCase =
        UseCase(
            profileDataManager = profileDataManager,
            firebaseOperationRepository,
            fireBaseStorageHandler
        )
}