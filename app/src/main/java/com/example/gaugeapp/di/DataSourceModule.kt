package com.example.gaugeapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.example.gaugeapp.dataSource.authentification.remote.AuthFirestoreService
import com.example.gaugeapp.dataSource.authentification.remote.AuthRemoteDataSourceImpl
import com.example.gaugeapp.dataSource.authentification.remote.IAuthRemoteDataSource
import com.example.gaugeapp.dataSource.communityLoan.campaign.local.CampaignComLoanDao
import com.example.gaugeapp.dataSource.communityLoan.campaign.local.CampaignComLoanLocalDataSource
import com.example.gaugeapp.dataSource.communityLoan.campaign.local.CampaignComLoanLocalDataSourceImpl
import com.example.gaugeapp.dataSource.communityLoan.campaign.local.CampaignComLoanLocalMapper
import com.example.gaugeapp.dataSource.communityLoan.campaign.remote.CampaignComLoanRemoteDataSource
import com.example.gaugeapp.dataSource.communityLoan.campaign.remote.CampaignComLoanRemoteDataSourceImpl
import com.example.gaugeapp.dataSource.communityLoan.campaign.remote.CampaignComLoanRemoteService
import com.example.gaugeapp.dataSource.communityLoan.creditLine.local.CreditLineComLoanDao
import com.example.gaugeapp.dataSource.communityLoan.creditLine.local.CreditLineComLoanLocalDataSource
import com.example.gaugeapp.dataSource.communityLoan.creditLine.local.CreditLineComLoanLocalDataSourceImpl
import com.example.gaugeapp.dataSource.communityLoan.creditLine.local.CreditLineComLoanLocalMapper
import com.example.gaugeapp.dataSource.communityLoan.creditLine.remote.CreditLineComLoanRemoteDataSource
import com.example.gaugeapp.dataSource.communityLoan.creditLine.remote.CreditLineComLoanRemoteDataSourceImpl
import com.example.gaugeapp.dataSource.communityLoan.creditLine.remote.CreditLineComLoanRemoteService
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
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.IShoppingCreditLocalDataSource
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.ShoppingCreditDao
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.ShoppingCreditLocalDataSourceImpl
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.mappers.ShoppingCreditLineLocalMapper
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.mappers.ShoppingCreditLocalMapper
import com.example.gaugeapp.dataSource.credit.shoppingCredit.remote.IShoppingCreditRemoteDataSource
import com.example.gaugeapp.dataSource.credit.shoppingCredit.remote.ShoppingCreditRemoteDataSourceImpl
import com.example.gaugeapp.dataSource.credit.shoppingCredit.remote.ShoppingCreditService

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
    ) : UserRemoteDataSource = UserRemoteDataSourceImpl(firestore)


    //---------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------SHOPPING CREDIT DATA SOURCE PROVIDING-----------------------------------------
    //---------------------------------------------------------------------------------------------------------------------

    /**
     * Provide shopping credit local data source
     *
     * @param dao
     * @param creditLineMapper
     * @param creditMapper
     * @return ShoppingCreditLocalDataSourceImpl
     * @author tsafix
     */
    @Singleton
    @Provides
    fun provideShoppingCreditLocalDataSource(
        dao: ShoppingCreditDao,
        creditLineMapper: ShoppingCreditLineLocalMapper,
        creditMapper: ShoppingCreditLocalMapper
    ): IShoppingCreditLocalDataSource {
        return ShoppingCreditLocalDataSourceImpl(dao, creditLineMapper, creditMapper)
    }


    /**
     * Provide shopping credit remote data source
     *
     * @param service
     * @return IShoppingCreditRemoteDataSource
     * @author tsafix
     */
    @Singleton
    @Provides
    fun provideShoppingCreditRemoteDataSource(service: ShoppingCreditService): IShoppingCreditRemoteDataSource {
        return ShoppingCreditRemoteDataSourceImpl(service)
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
    //-------------------------------------------COMMUNITY LOAN DATA SOURCE PROVIDING-----------------------------------------
    //---------------------------------------------------------------------------------------------------------------------


    /**
     * Provide credit line com loan local data source
     *
     * @param dao
     * @param mapper
     * @return
     */
    @Singleton
    @Provides
    fun provideCreditLineComLoanLocalDataSource(
        dao: CreditLineComLoanDao,
        mapper: CreditLineComLoanLocalMapper
    ): CreditLineComLoanLocalDataSource {
        return CreditLineComLoanLocalDataSourceImpl(mapper, dao)
    }


    /**
     * Provide credit line com loan remote data source
     *
     * @param service
     * @return
     */
    @Singleton
    @Provides
    fun provideCreditLineComLoanRemoteDataSource(service: CreditLineComLoanRemoteService): CreditLineComLoanRemoteDataSource {
        return CreditLineComLoanRemoteDataSourceImpl(service)
    }




    /**
     * Provide campaign com loan remote data source
     *
     * @param service
     * @return
     */
    @Singleton
    @Provides
    fun provideCampaignComLoanRemoteDataSource(service: CampaignComLoanRemoteService): CampaignComLoanRemoteDataSource {
        return CampaignComLoanRemoteDataSourceImpl(service)
    }




    /**
     * Provide campaign com loan local data source
     *
     * @param dao
     * @param mapper
     * @return
     */
    @Singleton
    @Provides
    fun provideCampaignComLoanLocalDataSource(
        dao: CampaignComLoanDao,
        mapper: CampaignComLoanLocalMapper
    ): CampaignComLoanLocalDataSource {
        return CampaignComLoanLocalDataSourceImpl(mapper, dao)
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