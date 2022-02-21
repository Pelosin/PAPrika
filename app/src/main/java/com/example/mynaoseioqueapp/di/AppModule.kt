package com.example.mynaoseioqueapp.di

import com.example.mynaoseioqueapp.common.Constant
import com.example.mynaoseioqueapp.common.DispatcherProvider
import com.example.mynaoseioqueapp.data.remote.FoodApi
import com.example.mynaoseioqueapp.data.remote.UserApi
import com.example.mynaoseioqueapp.data.repository.FoodRepoImpl
import com.example.mynaoseioqueapp.data.repository.UserRepoImpl
import com.example.mynaoseioqueapp.domain.repository.FoodRepository
import com.example.mynaoseioqueapp.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesUserApi(): UserApi = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserApi::class.java)

    @Singleton
    @Provides
    fun providesUserRepo(api: UserApi): UserRepository = UserRepoImpl(api)

    @Singleton
    @Provides
    fun providesFoodApi(): FoodApi = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FoodApi::class.java)

    @Singleton
    @Provides
    fun providesFoodRepo(api: FoodApi): FoodRepository = FoodRepoImpl(api)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }
}