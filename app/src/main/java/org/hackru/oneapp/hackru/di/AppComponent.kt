package org.hackru.oneapp.hackru.di

import dagger.Component
import org.hackru.oneapp.hackru.ui.drawer.LoginActivity
import org.hackru.oneapp.hackru.ui.main.announcements.AnnouncementsFragment
import org.hackru.oneapp.hackru.ui.main.events.SaturdayFragment
import org.hackru.oneapp.hackru.ui.main.events.SundayFragment
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun inject(announcementsFragment: AnnouncementsFragment)
    fun inject(loginActivity: LoginActivity)
    fun inject(saturdayFragment: SaturdayFragment)
    fun inject(sundayFragment: SundayFragment)
}