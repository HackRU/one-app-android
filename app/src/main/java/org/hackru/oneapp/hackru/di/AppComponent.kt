package org.hackru.oneapp.hackru.di

import dagger.Component
import org.hackru.oneapp.hackru.ui.main.announcements.AnnouncementsFragment
import org.hackru.oneapp.hackru.ui.main.events.EventsFragment
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    abstract fun inject(announcementsFragment: AnnouncementsFragment)
    abstract fun inject(eventsFragment: EventsFragment)
}