package com.example.jvmori.repobrowser.di.component

import android.app.Application
import com.example.jvmori.repobrowser.application.BaseApplication
import com.example.jvmori.repobrowser.di.modules.app.ActivityBuildersModule
import com.example.jvmori.repobrowser.di.modules.app.DatabaseModule
import com.example.jvmori.repobrowser.di.modules.app.NetworkModule
import com.example.jvmori.repobrowser.di.scopes.ApplicationScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@ApplicationScope
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        ActivityBuildersModule::class,
        NetworkModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        @BindsInstance
        fun application(application: Application): Builder
    }
}