package ua.palamarenko.cozyandroid2.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val appContext: Context) {

    @Provides
    internal fun provideContext(): Context {
        return appContext
    }
}