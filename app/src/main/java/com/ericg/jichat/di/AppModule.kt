package com.ericg.jichat.di

import android.app.Application
import androidx.room.Room
import com.ericg.jichat.data.local.ChatDatabase
import com.ericg.jichat.data.remote.ApiService
import com.ericg.jichat.data.repository.ChatRepository
import com.ericg.jichat.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .callTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun providesAPIService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesChatRepository(apiService: ApiService, chatsDatabase: ChatDatabase): ChatRepository {
        return ChatRepository(apiService, chatsDatabase)
    }

    @Singleton
    @Provides
    fun providesChatsDatabase(application: Application): ChatDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            ChatDatabase::class.java,
            "chats_database",
        ).fallbackToDestructiveMigration().build()
    }
}
