package org.hackru.oneapp.hackru.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hackru.oneapp.hackru.BuildConfig
import org.hackru.oneapp.hackru.api.services.LcsService
import org.hackru.oneapp.hackru.db.AnnouncementsDao
import org.hackru.oneapp.hackru.db.Database
import org.hackru.oneapp.hackru.db.EventsDao
import org.hackru.oneapp.hackru.repositories.AnnouncementsRepository
import org.hackru.oneapp.hackru.repositories.EventsRepository
import org.hackru.oneapp.hackru.ui.main.announcements.AnnouncementsViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {

    @Provides
    fun provideApplicationContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideDatabase(applicationContext: Context): Database {
        return Room.databaseBuilder(applicationContext, Database::class.java, "database").build()
    }

    @Provides
    @Singleton
    fun provideAnnouncementsDao(database: Database) = database.announcementsDao()

    @Provides
    @Singleton
    fun provideEventsDao(database: Database) = database.eventsDao()

    @Provides
    @Singleton
    fun provideLcsService(): LcsService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        return Retrofit.Builder()
                .baseUrl(BuildConfig.LcsEndpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(LcsService::class.java)
    }

    @Provides
    @Singleton
    fun provideAnnouncementsRepository(announcementsDao: AnnouncementsDao, lcsService: LcsService, context: Context): AnnouncementsRepository {
        return AnnouncementsRepository(announcementsDao, lcsService, context)
    }

    @Provides
    @Singleton
    fun provideEventsRepository(eventsDao: EventsDao, lcsService: LcsService, context: Context): EventsRepository {
        return EventsRepository(eventsDao, lcsService, context)
    }

    @Provides
    @Singleton
    fun provideAnnouncementsViewModelFactory(announcementsRepository: AnnouncementsRepository): AnnouncementsViewModelFactory {
        return AnnouncementsViewModelFactory(announcementsRepository)
    }
}