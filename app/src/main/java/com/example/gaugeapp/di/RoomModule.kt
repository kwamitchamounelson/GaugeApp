package com.example.gaugeapp.di

import android.content.Context
import androidx.room.Room
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local.AirtimesCreditLineDao
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.ShoppingCreditLineDao
import com.example.gaugeapp.dataSource.roomPersistence.KWalletDataBase
import com.example.gaugeapp.dataSource.user.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideKWallerDataBase(@ApplicationContext context: Context): KWalletDataBase {
        return Room.databaseBuilder(
            context,
            KWalletDataBase::class.java,
            KWalletDataBase.DATA_BASE_NAME
        )
            .fallbackToDestructiveMigrationFrom(1)//On demande de détruire la base de données sqlite si la version courante est 1
            //.addMigrations(KWalletDataBase.MIGRATION_2_3)
            .build()

    }




    /**
     * Provide user dao
     *
     * @param kWalletDataBase
     * @return UserDao
     */
    @Singleton
    @Provides
    fun provideUserDao(kWalletDataBase: KWalletDataBase): UserDao {
        return kWalletDataBase.userDao()
    }


    /**
     * Provide shopping credit dao
     *
     * @param kWalletDataBase
     * @return ShoppingCreditDao
     */
    @Singleton
    @Provides
    fun provideShoppingCreditDao(kWalletDataBase: KWalletDataBase): ShoppingCreditLineDao {
        return kWalletDataBase.shoppingCreditDao()
    }

    /**
     * Provide airtime credit line dao
     *
     * @param kWalletDataBase
     * @return
     */
    @Singleton
    @Provides
    fun provideAirtimeCreditLineDao(kWalletDataBase: KWalletDataBase): AirtimesCreditLineDao {
        return kWalletDataBase.airtimeCreditLineDao()
    }
}