package com.example.gaugeapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.example.gaugeapp.dataSource.authentification.remote.AuthFirestoreService
import com.example.gaugeapp.dataSource.authentification.remote.AuthRemoteDataSourceImpl
import com.example.gaugeapp.dataSource.authentification.remote.IAuthRemoteDataSource
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local.AirtimeCreditLineLocalDataSource
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local.AirtimeCreditLineLocalDataSourceImpl
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local.AirtimesCreditLineDao
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local.AirtimesCreditLineLocalMapper
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.remote.AirTimeCreditLineRemoteDataSource
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.remote.AirTimeCreditLineRemoteDataSourceImpl
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.remote.AirtimeCreditLineService
import com.example.gaugeapp.dataSource.credit.airtimeCreditRequest.remote.AirTimeCreditRequestRemoteDataSource
import com.example.gaugeapp.dataSource.credit.airtimeCreditRequest.remote.AirTimeCreditRequestRemoteDataSourceImpl
import com.example.gaugeapp.dataSource.credit.airtimeCreditRequest.remote.AirtimeCreditRequestService
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.*
import com.example.gaugeapp.dataSource.credit.shoppingCredit.remote.ShoppingCreditLineRemoteDataSource
import com.example.gaugeapp.dataSource.credit.shoppingCredit.remote.ShoppingCreditLineRemoteDataSourceImpl
import com.example.gaugeapp.dataSource.credit.shoppingCredit.remote.ShoppingCreditLineService
import com.example.gaugeapp.dataSource.credit.shoppingCreditLineRequest.remote.ShoppingCreditRequestRemoteDataSource
import com.example.gaugeapp.dataSource.credit.shoppingCreditLineRequest.remote.ShoppingCreditRequestRemoteDataSourceImpl
import com.example.gaugeapp.dataSource.credit.shoppingCreditLineRequest.remote.ShoppingCreditRequestService
import com.example.gaugeapp.dataSource.storage.StorageDataSource
import com.example.gaugeapp.dataSource.storage.StorageDataSourceImpl
import com.example.gaugeapp.dataSource.storage.StorageService


import com.example.gaugeapp.dataSource.user.local.UserDao
import com.example.gaugeapp.dataSource.user.local.UserLocalDataSource
import com.example.gaugeapp.dataSource.user.local.UserLocalDataSourceImpl
import com.example.gaugeapp.dataSource.user.local.UserLocalMapper

import com.example.gaugeapp.dataSource.user.remote.UserRemoteDataSource
import com.example.gaugeapp.dataSource.user.remote.UserRemoteDataSourceImpl

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InstallIn(ApplicationComponent::class)
@Module
object DataSourceModule {
    /**
     * Provide firestore instance
     *
     * @return FirebaseFirestore
     */
    @Singleton
    @Provides
    fun provideFirestoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    /**
     * Provide firestore storage instance
     *
     * @return FirebaseStorage
     */
    @Singleton
    @Provides
    fun provideFirestoreStorageInstance(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    /**
     * Provide firestore reference
     *
     * @return StorageReference
     */
    @Singleton
    @Provides
    fun provideFirestoreReference(): StorageReference {
        return FirebaseStorage.getInstance().reference
    }

    /**
     * Get firebase auth instance
     *
     * @return FirebaseAuth
     */
    @Singleton
    @Provides
    fun getFirebaseAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    /**
     * Provide firebase storage instance
     *
     * @return FirebaseStorage
     */
    @Singleton
    @Provides
    fun provideFirebaseStorageInstance(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }


    /**
     * Provide auth data soure
     *
     * @param firebaseAuthInstance
     * @param authService
     */
    @Singleton
    @Provides
    fun provideAuthDataSoure(
        firebaseAuthInstance: FirebaseAuth,
        authService: AuthFirestoreService
    ) =
        AuthRemoteDataSourceImpl(firebaseAuthInstance, authService)


    /**
     * Provide common upload file data source
     * This function provide an interface that permit to
     * upload files for any type on the server
     * @param uploadFileService
     * @return
     */
    @Singleton
    @Provides
    fun provideCommonUploadFileDataSource(
        storageService: StorageService
    ): StorageDataSource {
        return StorageDataSourceImpl(storageService)
    }


    //providind auth datasource instantiation

    @Singleton
    @Provides
    fun provideAuthRemoteDataSource(
        firebaseAuthInstance: FirebaseAuth,
        service: AuthFirestoreService
    ): IAuthRemoteDataSource {
        return AuthRemoteDataSourceImpl(firebaseAuthInstance, service)
    }


    @Singleton
    @Provides
    fun provideUserLocalDataSource(
        userLocalMapper: UserLocalMapper,
        userDao: UserDao
    ): UserLocalDataSource {
        return UserLocalDataSourceImpl(userLocalMapper, userDao)
    }


    @Singleton
    @Provides
    fun provideUserRemoteDateSource(
        firestore: FirebaseFirestore
    ): UserRemoteDataSource = UserRemoteDataSourceImpl(firestore)


    //---------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------SHOPPING CREDIT DATA SOURCE PROVIDING-----------------------------------------
    //---------------------------------------------------------------------------------------------------------------------

    /**
     * Provide shopping credit local data source
     *
     * @param dao
     * @param creditLineMapper
     * @return
     */
    @Singleton
    @Provides
    fun provideShoppingCreditLocalDataSource(
        creditLineMapper: ShoppingCreditLineLocalMapper,
        dao: ShoppingCreditLineDao
    ): ShoppingCreditLineLocalDataSource {
        return ShoppingCreditLineLocalDataSourceImpl(creditLineMapper, dao)
    }


    /**
     * Provide shopping credit remote data source
     *
     * @param service
     * @return
     */
    @Singleton
    @Provides
    fun provideShoppingCreditRemoteDataSource(service: ShoppingCreditLineService): ShoppingCreditLineRemoteDataSource {
        return ShoppingCreditLineRemoteDataSourceImpl(service)
    }



    //---------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------SHOPPING CREDIT REQUEST DATA SOURCE PROVIDING-----------------------------------------
    //---------------------------------------------------------------------------------------------------------------------


    @Singleton
    @Provides
    fun provideShoppingCreditRequestRemoteDataSource(service: ShoppingCreditRequestService): ShoppingCreditRequestRemoteDataSource {
        return ShoppingCreditRequestRemoteDataSourceImpl(service)
    }


    //---------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------AIRTIME CREDIT LINE DATA SOURCE PROVIDING-----------------------------------------
    //---------------------------------------------------------------------------------------------------------------------


    /**
     * Provide airtime credit line local data source
     *
     * @param dao
     * @param mapper
     * @return
     */
    @Singleton
    @Provides
    fun provideAirtimeCreditLineLocalDataSource(
        dao: AirtimesCreditLineDao,
        mapper: AirtimesCreditLineLocalMapper
    ): AirtimeCreditLineLocalDataSource {
        return AirtimeCreditLineLocalDataSourceImpl(mapper, dao)
    }


    /**
     * Provide airtime credit line remote data source
     *
     * @param service
     * @return
     */
    @Singleton
    @Provides
    fun provideAirtimeCreditLineRemoteDataSource(service: AirtimeCreditLineService): AirTimeCreditLineRemoteDataSource {
        return AirTimeCreditLineRemoteDataSourceImpl(service)
    }


    //---------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------AIRTIME CREDIT REQUEST DATA SOURCE PROVIDING-----------------------------------------
    //---------------------------------------------------------------------------------------------------------------------


    @Singleton
    @Provides
    fun provideAirtimeCreditRequestRemoteDataSource(service: AirtimeCreditRequestService): AirTimeCreditRequestRemoteDataSource {
        return AirTimeCreditRequestRemoteDataSourceImpl(service)
    }


}