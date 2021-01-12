package com.example.gaugeapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.work.Configuration
import com.blongho.country_data.World
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kola.kola_entities_features.entities.KUser
import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef
import com.example.gaugeapp.commonRepositories.roomRep.AppDatabase
import com.example.gaugeapp.di.RepositoryProviderEntryPoint
import com.example.gaugeapp.utils.extentions.getDataFlow
import com.example.gaugeapp.utils.userPrefUtils.UserSharedPref
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltAndroidApp
class KolaWhalletApplication : Application(),
    Configuration.Provider {//Configuration.Provider is for workManager injections


    companion object {
        lateinit var userPref: UserSharedPref
        lateinit var database: AppDatabase
        val executorService: ExecutorService = Executors.newFixedThreadPool(4)
        var currentUser = KUser()
        lateinit var firebaseAnalytics: FirebaseAnalytics
        var currentUser2 = MutableLiveData<KUser>().apply {
            value = KUser()
        }
        var numberOfSims = MutableLiveData<Int>().apply {
            value = 0
        }


        @ExperimentalCoroutinesApi
        fun loadCurentUser(onComplete: (KUser) -> Unit) {

            getCurrentUserFromFiresTore().onEach { userDataState ->
                currentUser2.postValue(userDataState)
                onComplete(userDataState)
                crashlytics.setCustomKey("userUid", userDataState.userUid)
                crashlytics.setCustomKey("userAuthPhoneNumber", userDataState.authPhoneNumber)
                crashlytics.setCustomKey("userName", userDataState.userName)

            }.flowOn(Dispatchers.IO).launchIn(GlobalScope)
/*            try {
                FireStoreAuthUtil.getCurentUserFromFiresTore(onSucess = {
                    currentUser2.value = it
                    onComplete(it)
                    crashlytics.setCustomKey("userUid", it.userUid)
                    crashlytics.setCustomKey("userAuthPhoneNumber", it.authPhoneNumber)
                    crashlytics.setCustomKey("userName", it.userName)
                }, onError = {

                })
            } catch (e: Exception) {
                e.printStackTrace()
            }*/
        }

        @ExperimentalCoroutinesApi
        private fun getCurrentUserFromFiresTore(): Flow<KUser> {
            return FireStoreCollDocRef.curentUserDocRef.getDataFlow { documentSnapshot ->
                documentSnapshot?.toObject(KUser::class.java)!!
            }
        }

        lateinit var crashlytics: FirebaseCrashlytics

        lateinit var hiltEntryPoint : RepositoryProviderEntryPoint
    }


    //to inject fields into workManager class
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }


    override fun onCreate() {
        super.onCreate()
        crashlytics = FirebaseCrashlytics.getInstance()
        try {
            //database = Room.databaseBuilder(this, AppDatabase::class.java, "kola_wallet_database").addMigrations(AppDatabase.MIGRATION_1_2).build()
            database = Room.databaseBuilder(this, AppDatabase::class.java, "kola_wallet_database")
                .fallbackToDestructiveMigrationFrom(1)//On demande de détruire la base de données sqlite si la version courante est 1
                //.addMigrations(AppDatabase.MIGRATION_2_3)
                .build()
            userPref = UserSharedPref(this)
            hiltEntryPoint = EntryPointAccessors.fromApplication(this.applicationContext,
                RepositoryProviderEntryPoint::class.java)
        } catch (ex: Exception) {
            ex.printStackTrace()
            FirebaseCrashlytics.getInstance().recordException(ex)
        }

        World.init(applicationContext)
    }

}