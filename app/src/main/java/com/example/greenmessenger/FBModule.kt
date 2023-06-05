package com.example.greenmessenger

import android.content.Context
import com.example.greenmessenger.model.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object FBModule {
    @Provides
    @Singleton
    fun provideLoginRepository(@ApplicationContext context: Context): LoginRepository {
        return LoginRepository(context)
    }
}