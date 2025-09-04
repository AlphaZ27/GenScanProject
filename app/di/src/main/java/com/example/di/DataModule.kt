package com.example.di

import com.example.data.repository.AdminRepositoryImpl
import com.example.domain.repository.AdminRepository
import com.example.data.repository.QrCodeRepositoryImpl
import com.example.domain.repository.QrCodeRepository
import com.example.data.repository.AuthRepositoryImpl
import com.example.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindQrCodeRepository(
        qrCodeRepositoryImpl: QrCodeRepositoryImpl
    ): QrCodeRepository

    //Binds for other repositories

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

//    @Binds
//    @Singleton
//    abstract fun bindAdminRepository(
//        adminRepositoryImpl: AdminRepositoryImpl
//    ): AdminRepository

    @Binds
    abstract fun bindAdminRepository(
        impl: AdminRepositoryImpl
    ): AdminRepository

}