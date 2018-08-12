package org.hackru.oneapp.hackru.di

import dagger.Component
import org.hackru.oneapp.hackru.ui.main.announcements.AnnouncementsFragment
<<<<<<< HEAD
import org.hackru.oneapp.hackru.ui.main.events.EventsFragment
=======
>>>>>>> d69d810010cf862c0eddee67b3a7d58691f5f572
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    abstract fun inject(announcementsFragment: AnnouncementsFragment)
<<<<<<< HEAD
    abstract fun inject(eventsFragment: EventsFragment)
=======
>>>>>>> d69d810010cf862c0eddee67b3a7d58691f5f572
}