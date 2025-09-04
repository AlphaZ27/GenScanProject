package com.example.di

import com.example.domain.repository.AuthRepository
import com.example.domain.repository.QrCodeRepository
import com.example.domain.usecase.GetQrCodesUseCase
import com.example.domain.usecase.SaveQrCodeUseCase
import com.example.domain.usecase.GetCurrentUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideSaveQrCodeUseCase(qrCodeRepository: QrCodeRepository): SaveQrCodeUseCase {
        return SaveQrCodeUseCase(qrCodeRepository)
    }

    @Provides
    fun provideGetQrCodesUseCase(qrCodeRepository: QrCodeRepository): GetQrCodesUseCase {
        return GetQrCodesUseCase(qrCodeRepository)
    }

    @Provides
    fun provideGetCurrentUserUseCase(repository: AuthRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(repository)
    }


    //Other use cases
}