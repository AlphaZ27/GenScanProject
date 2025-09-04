package com.example.di

import com.example.domain.repository.AdminRepository
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.QrCodeRepository
import com.example.domain.usecase.*  // Import all use cases
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

    @Provides
    fun provideRegisterUserUseCase(repository: AuthRepository): RegisterUserUseCase {
        return RegisterUserUseCase(repository)
    }

    @Provides
    fun provideGetPendingUsersUseCase(repository: AdminRepository): GetPendingUsersUseCase {
        return GetPendingUsersUseCase(repository)
    }

    @Provides
    fun provideApprovePendingUserUseCase(repository: AdminRepository): ApprovePendingUserUseCase {
        return ApprovePendingUserUseCase(repository)
    }

    @Provides
    fun provideDeleteUserUseCase(repository: AdminRepository): DeleteUserUseCase {
        return DeleteUserUseCase(repository)
    }

}