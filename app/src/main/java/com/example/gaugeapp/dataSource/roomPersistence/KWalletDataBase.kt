package com.example.gaugeapp.dataSource.roomPersistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gaugeapp.commonRepositories.roomRep.DateConverter
import com.example.gaugeapp.commonRepositories.roomRep.roomEntities.SmsTable
import com.example.gaugeapp.data.entities.Balance
import com.example.gaugeapp.dataSource.communityLoan.campaign.local.CampaignComLoanDao
import com.example.gaugeapp.dataSource.communityLoan.campaign.local.CampaignComLoanTable
import com.example.gaugeapp.dataSource.communityLoan.creditLine.local.CreditLineComLoanDao
import com.example.gaugeapp.dataSource.communityLoan.creditLine.local.CreditLineComLoanTable
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local.AirtimesCreditLineDao
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local.AirtimesCreditLineLocalEntity
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.ShoppingCreditDao
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.persistanceEntities.ShoppingCreditLineTable
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.persistanceEntities.ShoppingCreditTable
import com.example.gaugeapp.dataSource.user.local.UserCacheEntity
import com.example.gaugeapp.dataSource.user.local.UserDao

@Database(
    entities = [SmsTable::class, Balance::class, UserCacheEntity::class, ShoppingCreditLineTable::class, ShoppingCreditTable::class
        , AirtimesCreditLineLocalEntity::class, CampaignComLoanTable::class, CreditLineComLoanTable::class],
    version = 3
)
@TypeConverters(DateConverter::class)
abstract class KWalletDataBase : RoomDatabase() {

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE financeeducationarticletable(articleUid TEXT NOT NULL, title TEXT NOT NULL, subTitle TEXT NOT NULL, urlIllustrationImg TEXT NOT NULL, contentText TEXT NOT NULL, financialEducator TEXT NOT NULL, publicationDate INTEGER NOT NULL, likesListSerialized TEXT NOT NULL, sharesListSerialized TEXT NOT NULL, keyWordListSerialized TEXT NOT NULL, userIdBookmarks TEXT NOT NULL, commentNumber INTEGER NOT NULL, PRIMARY KEY(articleUid))")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE budgettable ADD isSavings INTEGER NOT NULL DEFAULT(0)")
            }
        }

        val DATA_BASE_NAME = "kola_wallet_database"
        var TEST_MODE = false
    }


    abstract fun userDao(): UserDao
    abstract fun shoppingCreditDao(): ShoppingCreditDao
    abstract fun airtimeCreditLineDao(): AirtimesCreditLineDao
    abstract fun creditLineComLoanDao(): CreditLineComLoanDao
    abstract fun campaignComLoanDao(): CampaignComLoanDao

}