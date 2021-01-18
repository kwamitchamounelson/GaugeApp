package com.example.gaugeapp.commonRepositories.roomRep

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gaugeapp.commonRepositories.roomRep.roomEntities.SmsTable
import com.example.gaugeapp.data.entities.Balance
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local.AirtimesCreditLineLocalEntity
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.persistanceEntities.ShoppingCreditLineTable
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.persistanceEntities.ShoppingCreditTable
import com.example.gaugeapp.dataSource.user.local.UserCacheEntity


@Database(
    entities = [SmsTable::class, Balance::class, UserCacheEntity::class, ShoppingCreditLineTable::class, ShoppingCreditTable::class
        , AirtimesCreditLineLocalEntity::class],
    version = 3
)

@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE financeeducationarticletable(articleUid TEXT NOT NULL, title TEXT NOT NULL, subTitle TEXT NOT NULL, urlIllustrationImg TEXT NOT NULL, contentText TEXT NOT NULL, financialEducator TEXT NOT NULL, publicationDate INTEGER NOT NULL, likesListSerialized TEXT NOT NULL, sharesListSerialized TEXT NOT NULL, keyWordListSerialized TEXT NOT NULL, userIdBookmarks TEXT NOT NULL, commentNumber INTEGER NOT NULL, PRIMARY KEY(articleUid))")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                /*for this migration we aught to:
                    - Add the new tables of bookkeeping and user table
                    - Migrate the data of the eldest tables from the newest
                    - Suppress the eldest tables of bookkeeping
                 */

                TODO("Not yet implemented")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE budgettable ADD isSavings INTEGER NOT NULL DEFAULT(0)")
            }
        }
    }

}