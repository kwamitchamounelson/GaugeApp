package com.example.gaugeapp.di

import com.google.gson.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import java.util.*
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object OtherInterfacesModule {

    private var jsonSerializer: JsonSerializer<Date?> = JsonSerializer<Date?> { src, typeOfSrc, context ->
        if (src == null) {
            null
        } else {
            JsonPrimitive(
                src.time
            )
        }
    }

    private var jsonDeserializer: JsonDeserializer<Date?> =
        JsonDeserializer<Date?> { json, typeOfT, context ->
            if (json == null) null else Date(json.asLong)
        }

    @Provides
    @Singleton
    fun provideGsonBuilder () : Gson{
        return GsonBuilder()
            .registerTypeAdapter(
                Date::class.java,
                jsonSerializer
            )
            .registerTypeAdapter(
                Date::class.java,
                jsonDeserializer
            )
            .create()
    }
}