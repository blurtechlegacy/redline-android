package tech.blur.redline

import android.app.Application
import android.content.Context
import tech.blur.redline.core.AppComponent
import tech.blur.redline.core.DaggerAppComponent
import tech.blur.redline.core.modules.SharedPreferencesModule

class App : Application() {

    private lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        component = DaggerAppComponent.builder()
            .sharedPreferencesModule(SharedPreferencesModule(this))
            .build()

    }

    fun getAppComponent(): AppComponent {
//        if (::component.isInitialized){
//            component = DaggerAppComponent.builder()
//                .sharedPreferencesModule(SharedPreferencesModule(this))
//                .build()
//        }
        return component
    }

    companion object {

        private fun getApp(context: Context): App {
            return context.applicationContext as App
        }

        lateinit var INSTANCE: App


    }
}