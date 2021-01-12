package com.example.gaugeapp.utils.userPrefUtils

import android.content.Context
import android.content.SharedPreferences
import com.example.gaugeapp.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kola.kola_entities_features.entities.WrongTransNotifyer
import com.example.gaugeapp.KolaWhalletApplication
import com.example.gaugeapp.utils.BALANCE_ALL
import com.example.gaugeapp.utils.BOTH_OPERATOR_NAME
import com.example.gaugeapp.utils.PhoneNumberUtils
import com.example.gaugeapp.utils.getCurrentTimeInMiliss
import java.util.*

class UserSharedPref(val context: Context) {

    private val PREFS_FILENAME = "com.kola.kolawallet.userPrefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
    /*private val prefUsersImages: SharedPreferences =
        context.getSharedPreferences("com.kola.kolawallet.userPrefs2", 0)*/

    private val FIRST_OPENNING_KEY = "FIRSTOPENING"
    private val TUTORIAL_KEY = "TUTORIAL"
    private val TUTORIAL_KEY_FROM_SETTINGS = "TUTORIAL_SETTINGS"
    private val HAS_SEEN_Phone_number_dialog  = "HAS_SEEN_Phone_number_dialog"
    private val HAS_COMPLET_INFO_KEY = "HASCOMPLETINFOS"
    private val PRIVACY_STATE = "PRIVACY_STATE"
    private val MERGED_ACCOUNTS = "MERGED_ACCOUNTS"
    private val DISPLAYED_ACCOUNT = "DISPLAYED_ACCOUNT"
    private val SECURITY_PASSWORD = "SECURITY_PASSWORD"
    private val USAGE_RATE = "USAGE_RATE"
    private val SECURITY_PASSWORD_IS_ACTIVALE = "SECURITY_PASSWORD_IS_ACTIVALE"
    private val TOTAL_INCOMES = "INCOMES"
    private val TOTAL_OUTcOMES = "OUTCOMES"
    private val TOTAL_OUTcOMES_orange = "OUTCOMES_Orange"
    private val TOTAL_OUTcOMES_MTN = "OUTCOMES_Mtn"
    private val TOTAL_OUTcOMES_CASH = "OUTCOMES_cash"
    private val TOTAL_INCOMES_Orange = "INCOMESOrange"
    private val TOTAL_INCOMES_MtN = "INCOMES_MTN"
    private val TOTAL_INCOMES_CASH = "INCOMES_CASH"

    private val SECURITY_COUNT = "SECURITY_COUNT"

    private val SECURITY_RESSET_TIME = "SECURITY_RESSET_TIME"


    val REANALYZED_REPORTED_TRANSACTION_VERSION_CODE_KEY =
        "REANALYZED_REPORTED_TRANSACTION_VERSION_CODE_KEY"

    var reanalyseTransactionsVersion: Long
        get() = prefs.getLong(REANALYZED_REPORTED_TRANSACTION_VERSION_CODE_KEY, 1)
        set(value) = prefs.edit().putLong(REANALYZED_REPORTED_TRANSACTION_VERSION_CODE_KEY, value)
            .apply()


    /**
     * promotional code of currnt user
     */
    private val KEY_PROMOTIONAL_CODE = "PROMOTIONAL_CODE"
    var promotionalCode: String
        get() = prefs.getString(KEY_PROMOTIONAL_CODE, "").toString()
        set(value) = prefs.edit().putString(KEY_PROMOTIONAL_CODE, value).apply()



    /**
     * to synch and get the new articles and updated articles from
     * the server
     */
    private val KEY_LATEST_GET_ARTICLE_DATE = "KEY_LATEST_GET_ARTICLE_DATE"
    var latesGestOfAticleDate: Long
        get() = prefs.getLong(KEY_LATEST_GET_ARTICLE_DATE, -1)
        set(value) = prefs.edit().putLong(KEY_LATEST_GET_ARTICLE_DATE, value).apply()




    /**
     * to manage remote chat notification
     */
    private val CHAT_NOTIFICATION = "CHAT_NOTIFICATION"

    fun checkIfNotificationHasClosed(text: String): Boolean {
        return prefs.getBoolean(
            text,
            false
        )
    }

    fun closeChatNotification(text: String) {
        prefs.edit().putBoolean(
            text,
            true
        ).apply()
    }


    /**
     * Pour enregistrer les mises à jours de l'pplication
     */
    private val LATESD_UPDATES_KEY = "LATESD_UPDATES_KEY"

    fun saveLadestUpdates(latesdVersionName: String) {
        prefs.edit().putStringSet(
            LATESD_UPDATES_KEY,
            getLatesdUpdates()
                .apply {
                    add(latesdVersionName)
                    distinct()
                }.toMutableSet()
        ).apply()
    }

    /**
     * Pour lire les mises à jours de l'application
     */
    fun getLatesdUpdates(): MutableList<String> {
        return prefs.getStringSet(
            LATESD_UPDATES_KEY,
            mutableSetOf()
        )!!.toMutableList()
            .apply {
                distinct()
            }
    }


    /**
     * To notify when orange wrong transaction are detected
     * */
    private val ORG_WRONG_TRANSACTION_KEY = "WRONG_TRANSACTION_ORANGE"

    fun setOrgWrongTransactionNotifyer(wrongTransNotifyer: WrongTransNotifyer? = null) {
        var stringValue = ""
        if (wrongTransNotifyer != null) {
            stringValue = Gson().toJson(wrongTransNotifyer)
        }
        prefs.edit().putString(ORG_WRONG_TRANSACTION_KEY, stringValue).apply()
    }

    fun getOrgWrongTransactionNotifyer(): WrongTransNotifyer? {
        var stringValue = ""
        return try {
            stringValue = prefs.getString(ORG_WRONG_TRANSACTION_KEY, "").toString()
            Gson().fromJson(stringValue, WrongTransNotifyer::class.java)
        } catch (Ex: Exception) {
            null
        }

    }

    /**
     * To notify when orange wrong transaction are detected
     * */
    private val MTN_WRONG_TRANSACTION_KEY = "WRONG_TRANSACTION_MTN"

    fun setMtnWrongTransactionNotifyer(wrongTransNotifyer: WrongTransNotifyer? = null) {
        var stringValue = ""
        if (wrongTransNotifyer != null) {
            stringValue = Gson().toJson(wrongTransNotifyer)
        }
        prefs.edit().putString(MTN_WRONG_TRANSACTION_KEY, stringValue).apply()
    }

    fun getMtnWrongTransactionNotifyer(): WrongTransNotifyer? {
        var stringValue = ""
        return try {
            stringValue = prefs.getString(MTN_WRONG_TRANSACTION_KEY, "").toString()
            Gson().fromJson(stringValue, WrongTransNotifyer::class.java)
        } catch (Ex: Exception) {
            null
        }

    }

    /**
     * Pour enregistrer les ID des transactions d'ont l'analyse a échoué
     */
    private val ANALYED_WRONGTRANSACTIONS_SKEY = "ANALYED_WRONGTRANSACTIONS_SKEY"

    fun saveAnalysedWorngtransactionId(transactionId: String) {
        prefs.edit().putStringSet(
            ANALYED_WRONGTRANSACTIONS_SKEY,
            getAnalysedWorngtransactionsId()
                .apply {
                    add(transactionId)
                    distinct()
                }.toMutableSet()
        ).apply()
    }

    /**
     * Pour lire les ID des transactions d'ont l'analyse avait déja échoué au préalable
     */
    fun getAnalysedWorngtransactionsId(): MutableList<String> {
        return prefs.getStringSet(
            ANALYED_WRONGTRANSACTIONS_SKEY,
            mutableSetOf()
        )!!.toMutableList()
            .apply {
                distinct()
            }
    }


    /**
     * to register the detected sim cards on the phone
     */
    private val SIM_CARD_OPERATOR_NAME = "SIM_CARD_OPERATOR_NAME"

    fun addDetectedSimCards(simCardOperator: String) {
        prefs.edit().putStringSet(
            SIM_CARD_OPERATOR_NAME,
            getSavedNumberOfCArd()
                .apply {
                    add(simCardOperator)
                    distinct()
                }.toMutableSet()
        ).apply()
    }

    fun clearNumBerOfSimCards() {
        prefs.edit().putStringSet(
            SIM_CARD_OPERATOR_NAME,
            mutableSetOf()
        ).apply()
    }

    /**
     * get list of active sim card on the phone
     */
    fun getSavedNumberOfCArd(): MutableList<String> {
        return prefs.getStringSet(
            SIM_CARD_OPERATOR_NAME,
            mutableSetOf()
        )!!.toMutableList()
            .apply {
                distinct()
            }
    }


    /**
     * to manage favori contacts
     */
    private val RECENT_CONTACTS = "RECENT_CONTACTS"

    fun getRecentContacts(): MutableList<String> {
        return prefs.getStringSet(
            RECENT_CONTACTS,
            mutableSetOf()
        )!!.toMutableList()
            .apply {
                distinct()
            }
    }

    val FLAG_DATE = "FLAG_DATE"
    fun getDateOfLastTransaction(phone: String): Long {
        return prefs.getLong(PhoneNumberUtils.formatPhoneNumber(phone) + "" + FLAG_DATE, -1)
    }

    fun saveRecentContact(phone: String) {
        val formatPhone = PhoneNumberUtils.formatPhoneNumber(phone)
        prefs.edit().putStringSet(
            RECENT_CONTACTS,
            getRecentContacts()
                .apply {
                    add(formatPhone)
                    distinct()
                }.toMutableSet()
        ).apply()
        //save date
        prefs.edit().putLong(
            formatPhone + "" + FLAG_DATE,
            Calendar.getInstance().timeInMillis
        ).apply()
    }

    fun removeRecentContact(phone: String) {
        prefs.edit().putStringSet(
            RECENT_CONTACTS,
            getRecentContacts()
                .apply {
                    remove(PhoneNumberUtils.formatPhoneNumber(phone))
                    distinct()
                }.toMutableSet()
        ).apply()
    }


    /**
     * to manage favori contacts
     */

    private val FAVORIE_CONTACTS = "FAVORIE_CONTACTS"
    fun getFavoriesContacts(): MutableList<String> {
        return prefs.getStringSet(
            FAVORIE_CONTACTS,
            mutableSetOf()
        )!!.toMutableList()
            .apply {
                distinct()
            }
    }

    val FLAG_NAME = "FLAG_NAME"
    fun getNameOfContact(phone: String): String? {
        return prefs.getString(PhoneNumberUtils.formatPhoneNumber(phone) + "" + FLAG_NAME, "")
    }

    fun saveFavorieContact(phone: String, name: String): Boolean {
        val list = getFavoriesContacts()
        return if (list.size < 4) {
            val formatPhone = PhoneNumberUtils.formatPhoneNumber(phone)
            prefs.edit().putStringSet(
                FAVORIE_CONTACTS,
                getFavoriesContacts()
                    .apply {
                        add(formatPhone)
                        distinct()
                    }.toMutableSet()
            ).apply()

            //save date
            prefs.edit().putString(
                formatPhone + "" + FLAG_NAME,
                name
            ).apply()
            true
        } else {
            false
        }
    }

    fun removeContactFavory(phone: String) {
        prefs.edit().putStringSet(
            FAVORIE_CONTACTS,
            getFavoriesContacts()
                .apply {
                    remove(PhoneNumberUtils.formatPhoneNumber(phone))
                    distinct()
                }.toMutableSet()
        ).apply()
    }

    /**
     * to manage favori contacts
     */


    /**
     * to manage name of recent transaction
     */

    private val RECENT_CONTACT_TRANSACTION = "RECENT_CONTACT_TRANSACTION"
    fun getRecentContactTransaction(): MutableList<String> {
        return prefs.getStringSet(
            RECENT_CONTACT_TRANSACTION,
            mutableSetOf()
        )!!.toMutableList()
            .apply {
                distinct()
            }
    }

    val FLAG_NAME_RECENT = "FLAG_NAME_RECENT"
    fun getNameOfRecentContactTransaction(phone: String): String? {
        return prefs.getString(
            PhoneNumberUtils.formatPhoneNumber(phone) + "" + FLAG_NAME_RECENT,
            ""
        )
    }

    fun saveRecentContactTransaction(phone: String, name: String): Boolean {
        val formatPhone = PhoneNumberUtils.formatPhoneNumber(phone)
        prefs.edit().putStringSet(
            RECENT_CONTACT_TRANSACTION,
            getRecentContactTransaction()
                .apply {
                    add(formatPhone)
                    distinct()
                }.toMutableSet()
        ).apply()

        //save name
        prefs.edit().putString(
            formatPhone + "" + FLAG_NAME_RECENT,
            name
        ).apply()
        return true
    }

    /**
     * to manage name of recent transaction
     */


    /**
     * compte momo et om valides
     */

    private val VALIDE_ACOUNTS = "VALIDE_ACOUNTS"

    fun getAllValideAcount(): MutableList<String> {
        return prefs.getStringSet(
            VALIDE_ACOUNTS,
            mutableSetOf()
        )!!.toMutableList()
            .apply {
                distinct()
            }
    }

    fun saveValideAccount(phone: String) {
        if (PhoneNumberUtils.isGreetPhoneNumber(phone)) {
            val list = getAllValideAcount()
            val formatPhone = PhoneNumberUtils.formatPhoneNumber(phone)
            if (!list.contains(formatPhone)) {
                prefs.edit().putStringSet(
                    VALIDE_ACOUNTS,
                    list.apply {
                        add(formatPhone)
                        distinct()
                    }.toMutableSet()
                ).apply()
            }
        }
    }

    fun removeValideAcount(phone: String) {
        prefs.edit().putStringSet(
            VALIDE_ACOUNTS,
            getAllValideAcount()
                .apply {
                    remove(PhoneNumberUtils.formatPhoneNumber(phone))
                    distinct()
                }.toMutableSet()
        ).apply()
    }

    /**
     * compte momo et om valides
     */


    /**
     * compte montants favories
     */

    private val FAVORITE_AMOUNT_TRANSFERT = "FAVORITE_AMOUNT_TRANSFERT"
    private val FAVORITE_AMOUNT_AIRTIME = "FAVORITE_AMOUNT_AIRTIME"

    fun getFavotiteAmountList(isTransfer: Boolean): MutableList<String> {
        val preferencenName = if (isTransfer) {
            FAVORITE_AMOUNT_TRANSFERT
        } else {
            FAVORITE_AMOUNT_AIRTIME
        }

        val allAmount = prefs.getStringSet(
            preferencenName,
            mutableSetOf()
        )!!.distinct()

        return if (allAmount.size > 7) {
            allAmount.subList(0, 6).toMutableList()
        } else {
            allAmount.toMutableList()
        }
    }

    fun saveFavotiteAmount(amount: Double, isTransfer: Boolean) {
        val preferencenName = if (isTransfer) {
            FAVORITE_AMOUNT_TRANSFERT
        } else {
            FAVORITE_AMOUNT_AIRTIME
        }

        val list = getFavotiteAmountList(isTransfer)
        val amountStr = amount.toInt().toString()
        prefs.edit().putStringSet(
            preferencenName,
            list.apply {
                add(amountStr)
                distinct()
            }.toMutableSet()
        ).apply()
    }

    /**
     * compte montants favories
     */


    val HAS_A_GOOD_ORANGE_TRANSACTION_KEY = "HAS_A_GOOD_ORANGE_TRANSACTION_KEY"
    var hasGoodOrangeTransaction: Boolean
        get() = prefs.getBoolean(HAS_A_GOOD_ORANGE_TRANSACTION_KEY, false)
        set(value) = prefs.edit().putBoolean(HAS_A_GOOD_ORANGE_TRANSACTION_KEY, value).apply()

    val HAS_A_GOOD_MTN_TRANSACTION_KEY = "HAS_A_GOOD_MTN_TRANSACTION_KEY"
    var hasGoodMtnTransaction: Boolean
        get() = prefs.getBoolean(HAS_A_GOOD_MTN_TRANSACTION_KEY, false)
        set(value) = prefs.edit().putBoolean(HAS_A_GOOD_MTN_TRANSACTION_KEY, value).apply()


    val FIRST_OPENING_FOR_REFRESHBALANCE_KEY = "FIRST_OPENING_FOR_REFRESHBALANCE_KEY"

    var isFirstInstallationForRefreshAmount: Boolean
        get() = prefs.getBoolean(FIRST_OPENING_FOR_REFRESHBALANCE_KEY, true)
        set(value) = prefs.edit().putBoolean(FIRST_OPENING_FOR_REFRESHBALANCE_KEY, value).apply()

    var hasSeeAppIntro: Boolean
        get() = prefs.getBoolean(FIRST_OPENNING_KEY, false)
        set(value) = prefs.edit().putBoolean(FIRST_OPENNING_KEY, value).apply()

    var hasSeenWalkThrough: Boolean
        get() = prefs.getBoolean(TUTORIAL_KEY, false)
        set(value) = prefs.edit().putBoolean(TUTORIAL_KEY, value).apply()

    var isCoomingFromSettings: Boolean
        get() = prefs.getBoolean(TUTORIAL_KEY_FROM_SETTINGS, false)
        set(value) = prefs.edit().putBoolean(TUTORIAL_KEY_FROM_SETTINGS, value).apply()

    var hasSeenPhoneNumberDialog: Boolean
        get() = prefs.getBoolean(HAS_SEEN_Phone_number_dialog, false)
        set(value) = prefs.edit().putBoolean(HAS_SEEN_Phone_number_dialog, value).apply()

    var hasAuthentified: Boolean
        get() = prefs.getBoolean(HAS_COMPLET_INFO_KEY, false)
        set(value) = prefs.edit().putBoolean(HAS_COMPLET_INFO_KEY, value).apply()

    var privateModeDisable: Boolean
        get() = prefs.getBoolean(PRIVACY_STATE, true)
        set(value) = prefs.edit().putBoolean(PRIVACY_STATE, value).apply()

    var mergedAccounts: Boolean
        get() = prefs.getBoolean(MERGED_ACCOUNTS, false)
        set(value) = prefs.edit().putBoolean(MERGED_ACCOUNTS, value).apply()

    var lastDisplayedAccount: String
        get() = prefs.getString(DISPLAYED_ACCOUNT, "")!!
        set(value) = prefs.edit().putString(DISPLAYED_ACCOUNT, value).apply()

    var securityPassword: String
        get() = prefs.getString(SECURITY_PASSWORD, "")!!
        set(value) = prefs.edit().putString(SECURITY_PASSWORD, value).apply()

    /**
     * this variable is there to know if the the user use the app frequently
     * this value is increase when the user perform actions in the app or open some views
     */
    var usageRateOfTheApplication: Int
        get() = prefs.getInt(USAGE_RATE, 0)
        set(value) = prefs.edit().putInt(USAGE_RATE, value).apply()

    /*var securityPasswordIsActivate: Boolean
        get() = prefs.getBoolean(SECURITY_PASSWORD_IS_ACTIVALE, true)
        set(value) = prefs.edit().putBoolean(SECURITY_PASSWORD_IS_ACTIVALE, value).apply()*/

    var securityTentativeCount: Int
        get() = prefs.getInt(SECURITY_COUNT, 0)
        set(value) = prefs.edit().putInt(SECURITY_COUNT, value).apply()

    var securityTimeToRessetCode: Long
        get() = prefs.getLong(SECURITY_RESSET_TIME, 0)
        set(value) = prefs.edit().putLong(SECURITY_RESSET_TIME, value).apply()

    var totalIncomes: Float
        get() = prefs.getFloat(TOTAL_INCOMES, 0F)
        set(value) = prefs.edit().putFloat(TOTAL_INCOMES, value).apply()
    var TotalOutComes: Float
        get() = prefs.getFloat(TOTAL_OUTcOMES, 0F)
        set(value) = prefs.edit().putFloat(TOTAL_OUTcOMES, value).apply()

    var TotalIncomesOrange: Float
        get() = prefs.getFloat(TOTAL_INCOMES_Orange, 0F)
        set(value) = prefs.edit().putFloat(TOTAL_INCOMES_Orange, value).apply()
    var TotalIncomesMtn: Float
        get() = prefs.getFloat(TOTAL_INCOMES_MtN, 0F)
        set(value) = prefs.edit().putFloat(TOTAL_INCOMES_MtN, value).apply()

    var TotalOutcomesOrange: Float
        get() = prefs.getFloat(TOTAL_OUTcOMES_orange, 0F)
        set(value) = prefs.edit().putFloat(TOTAL_OUTcOMES_orange, value).apply()
    var TotalIncomesCash: Float
        get() = prefs.getFloat(TOTAL_INCOMES_CASH, 0F)
        set(value) = prefs.edit().putFloat(TOTAL_INCOMES_CASH, value).apply()
    var TotalOutcomesMtn: Float
        get() = prefs.getFloat(TOTAL_OUTcOMES_MTN, 0F)
        set(value) = prefs.edit().putFloat(TOTAL_OUTcOMES_MTN, value).apply()

    var TotalOutcomesCash: Float
        get() = prefs.getFloat(TOTAL_OUTcOMES_CASH, 0F)
        set(value) = prefs.edit().putFloat(TOTAL_OUTcOMES_CASH, value).apply()


    val RANGE_TRANSACTION = "RANGE_TRANSACTION"
    var rangeToShowTransactions: Int
        get() = prefs.getInt(RANGE_TRANSACTION, 0)
        set(value) = prefs.edit().putInt(RANGE_TRANSACTION, value).apply()


    val CHECK_DETECTED_NAME_DELAI = "CHECK_DETECTED_NAME_DELAI"
    var checDetectedNameDelai: Long
        get() = prefs.getLong(CHECK_DETECTED_NAME_DELAI, 0)
        set(value) = prefs.edit().putLong(CHECK_DETECTED_NAME_DELAI, value).apply()

    /**
     * In the application we ought to know how many budgets a user has.
     * And this variable store it in shared preferences
     * @author Yvana
     */
    private val SIZE_BUDGET = "SIZEBUDGET"
    var numberBudget: Int
        get() = prefs.getInt(SIZE_BUDGET, 0)
        set(value) = prefs.edit().putInt(SIZE_BUDGET, value).apply()

    /**
     * To get all user settings for settings view
     * */
    fun getUserCheckListSetting(): UserSettingPrefObje {
        return UserSettingPrefObje(
            irreversibleMoneyTVal,
            irreversibleMoneyTText,
            irreversibleMoneyAVal,
            irreversibleMoneyAText,
            istheRightPersonVal,
            istheRightPersonText,
            istheRightAmountVal,
            istheRightAmountText,
            showTotalBudgetOnly,
            showSubBudgetDetail,
            showSubBudgetWhithoutDetail,
            useOrangePhoneNumber,
            userMtnPhoneNumber
        )
    }

    /**
     * To set all user settings for settings view
     * */
    fun setUserCheckListSetting(userSetting: UserSettingPrefObje) {
        irreversibleMoneyTVal = userSetting.isReversibleVal
        irreversibleMoneyTText = userSetting.isReversibleText

        irreversibleMoneyAVal = userSetting.irreversibleMoneyAVal
        irreversibleMoneyAText = userSetting.irreversibleMoneyAText


        istheRightPersonVal = userSetting.isRightPersonval
        istheRightPersonText = userSetting.isRightPersonText

        istheRightAmountVal = userSetting.isRightAmountval
        istheRightAmountText = userSetting.isRightAmountText

        showTotalBudgetOnly = userSetting.showBudgetOnly
        showSubBudgetDetail = userSetting.showSubBudgetDetail
        showSubBudgetWhithoutDetail = userSetting.showSubBudgetWhithoutDetail

        useOrangePhoneNumber = userSetting.orangeSimCardNumber
        userMtnPhoneNumber = userSetting.mtnSimCardNumber
    }

    /**
     * Phone number settings
     * **/
    private val KEY_MTN_PHONE_NUMBER = "MTN_PHONE_NUMBER"
    var userMtnPhoneNumber: String
        get() = prefs.getString(KEY_MTN_PHONE_NUMBER, "").toString()
        set(value) = prefs.edit().putString(KEY_MTN_PHONE_NUMBER, value).apply()


    private val KEY_ORANGE_PHONE_NUMBER = "ORANGE_PHONE_NUMBER"
    private val KEY_ORANGE_PHONE_NUMBER_VALIDATION = "ORANGE_PHONE_NUMBER_VALIDATION"
    var useOrangePhoneNumber: String
        get() = prefs.getString(KEY_ORANGE_PHONE_NUMBER, "").toString()
        set(value) = prefs.edit().putString(KEY_ORANGE_PHONE_NUMBER, value).apply()

    var orangePhone: Boolean
        get() = prefs.getBoolean(KEY_ORANGE_PHONE_NUMBER_VALIDATION, false)
        set(value) = prefs.edit().putBoolean(KEY_ORANGE_PHONE_NUMBER_VALIDATION, value).apply()


    /**
     * Security checklist settings
     * **/
    private val KEY_IRREVERSIBLE_MONEY_TRANSFERT_VAL = "IRREVERSIBLEMONEYTRANSFERTVAL"
    private val KEY_IRREVERSIBLE_MONEY_TRANSFERT_TEXT = "IRREVERSIBLEMONEYTRANSFERTTEXT"
    var irreversibleMoneyTVal: Boolean
        get() = prefs.getBoolean(KEY_IRREVERSIBLE_MONEY_TRANSFERT_VAL, true)
        set(value) = prefs.edit().putBoolean(KEY_IRREVERSIBLE_MONEY_TRANSFERT_VAL, value).apply()
    private var irreversibleMoneyTText: String
        get() = prefs.getString(
            KEY_IRREVERSIBLE_MONEY_TRANSFERT_TEXT,
            context.getString(R.string.irreversible_money_transfer)
        ).toString()
        set(value) = prefs.edit().putString(KEY_IRREVERSIBLE_MONEY_TRANSFERT_TEXT, value).apply()

    private val KEY_IRREVERSIBLE_MONEY_AIRTIME = "KEY_IRREVERSIBLE_MONEY_AIRTIME"
    private val KEY_IRREVERSIBLE_MONEY_AIRTIME_TEXT = "KEY_IRREVERSIBLE_MONEY_AIRTIME_TEXT"
    var irreversibleMoneyAVal: Boolean
        get() = prefs.getBoolean(KEY_IRREVERSIBLE_MONEY_AIRTIME, true)
        set(value) = prefs.edit().putBoolean(KEY_IRREVERSIBLE_MONEY_AIRTIME, value).apply()
    private var irreversibleMoneyAText: String
        get() = prefs.getString(
            KEY_IRREVERSIBLE_MONEY_AIRTIME_TEXT,
            context.getString(R.string.irreversible_money_airtime)
        ).toString()
        set(value) = prefs.edit().putString(KEY_IRREVERSIBLE_MONEY_AIRTIME_TEXT, value).apply()


    private val KEY_IS_RIGHT_PERSON_VAL = "KEYISRIGHTPERSONVAL"
    private val KEY_IS_RIGHT_PERSON_TEXT = "KEYISRIGHTPERSONTEXT"
    var istheRightPersonVal: Boolean
        get() = prefs.getBoolean(KEY_IS_RIGHT_PERSON_VAL, true)
        set(value) = prefs.edit().putBoolean(KEY_IS_RIGHT_PERSON_VAL, value).apply()
    private var istheRightPersonText: String
        get() = prefs.getString(
            KEY_IS_RIGHT_PERSON_TEXT,
            context.getString(R.string.is_it_the_right_person)
        ).toString()
        set(value) = prefs.edit().putString(KEY_IS_RIGHT_PERSON_TEXT, value).apply()

    private val KEY_IS_RIGHT_AMOUNT_VAL = "KEYISRIGHTAMOUNTVAL"
    private val KEY_IS_RIGHT_AMOUNT_TEXT = "KEYISRIGHTAMOUNTTEXT"
    var istheRightAmountVal: Boolean
        get() = prefs.getBoolean(KEY_IS_RIGHT_AMOUNT_VAL, true)
        set(value) = prefs.edit().putBoolean(KEY_IS_RIGHT_AMOUNT_VAL, value).apply()
    private var istheRightAmountText: String
        get() = prefs.getString(
            KEY_IS_RIGHT_AMOUNT_TEXT,
            context.getString(R.string.is_it_the_right_amount)
        ).toString()
        set(value) = prefs.edit().putString(KEY_IS_RIGHT_AMOUNT_TEXT, value).apply()

    /**
     * Theme settings
     * **/
    private val KEY_SHOW_TOTAL_BUDGET_ONLY = "SHOWTOTALBUDGETONLY"
    var showTotalBudgetOnly: Boolean
        get() = prefs.getBoolean(KEY_SHOW_TOTAL_BUDGET_ONLY, false)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_TOTAL_BUDGET_ONLY, value).apply()

    private val ACCOUNT_THEME = "CARDSIMTHEME"
    var accountTheme: String?
        get() = prefs.getString(ACCOUNT_THEME, BOTH_OPERATOR_NAME)
        set(value) = prefs.edit().putString(ACCOUNT_THEME, value).apply()
    private val BALANCE_THEME = "BALANCE_THEME"

    var balanceTheme: String?
        get() = prefs.getString(BALANCE_THEME, BALANCE_ALL)
        set(value) = prefs.edit().putString(BALANCE_THEME, value).apply()

    private val BALANCE_PERCENTAGE = "BALANCE_PERCENTAGE"
    var balancePercentage: Float?
        get() = prefs.getFloat(BALANCE_PERCENTAGE, 30F)
        set(value) = prefs.edit().putFloat(BALANCE_PERCENTAGE, value!!).apply()

    private val BALANCE_FIX_VALUE_ORANGE = "BALANCE_FIX_VALUE_ORANGE"
    var balanceFixValueOrange: Float?
        get() = prefs.getFloat(BALANCE_FIX_VALUE_ORANGE, 2000F)
        set(value) = prefs.edit().putFloat(BALANCE_FIX_VALUE_ORANGE, value!!).apply()

    private val BALANCE_FIX_VALUE_MTN = "BALANCE_FIX_VALUE_MTN"
    var balanceFixValueMTN: Float?
        get() = prefs.getFloat(BALANCE_FIX_VALUE_MTN, 2000F)
        set(value) = prefs.edit().putFloat(BALANCE_FIX_VALUE_MTN, value!!).apply()


    private val KEY_SHOW_SUB_BUDGET_DETAILS = "SHOW_SUB_BUDGET_DETAILS"
    var showSubBudgetDetail: Boolean
        get() = prefs.getBoolean(KEY_SHOW_SUB_BUDGET_DETAILS, true)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_SUB_BUDGET_DETAILS, value).apply()

    private val KEY_SHOW_SUB_BUDGET_WHITHOUT_DETAILS = "SHOW_SUB_BUDGET_WHITHOUT_DETAILS"
    var showSubBudgetWhithoutDetail: Boolean
        get() = prefs.getBoolean(KEY_SHOW_SUB_BUDGET_WHITHOUT_DETAILS, true)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_SUB_BUDGET_WHITHOUT_DETAILS, value).apply()

    data class UserSettingPrefObje(
        var isReversibleVal: Boolean,
        var isReversibleText: String,

        var irreversibleMoneyAVal: Boolean,
        var irreversibleMoneyAText: String,

        var isRightPersonval: Boolean,
        var isRightPersonText: String,

        var isRightAmountval: Boolean,
        var isRightAmountText: String,

        var showBudgetOnly: Boolean,
        var showSubBudgetDetail: Boolean,
        var showSubBudgetWhithoutDetail: Boolean,
        var orangeSimCardNumber: String,
        var mtnSimCardNumber: String
    )


    /**
     * to manage users image local cash
     */
    private val USER_IMAGE_IN_CASH_KEY = "USER_IMAGE_IN_CASH_KEY"
    private val SPLITOR_USER_IMAGE = "&&"
    fun getUserImagesList(): MutableList<String> {
        return prefs.getStringSet(
            USER_IMAGE_IN_CASH_KEY,
            mutableSetOf()
        )!!.toMutableList()
            .apply {
                distinct()
            }
    }

    fun getUserImageUrl(phone: String): String? {
        if (phone.isNotBlank()) {
            val formatPhone = PhoneNumberUtils.formatPhoneNumber(phone)
            val data = getUserImagesList().find { value ->
                value.contains(formatPhone, true)
            }
            return if (data != null) {
                val tab = data.split(SPLITOR_USER_IMAGE)
                if (tab.size == 2) {
                    tab[1]
                } else {
                    ""
                }
            } else {
                ""
            }
        } else {
            return ""
        }
        /*val formatPhone = PhoneNumberUtils.formatPhoneNumber(phone)
        return prefUsersImages.getString(formatPhone, NO_IMAGE_FOUND)*/
    }

    fun saveUserImage(phone: String, imageUrl: String) {
        if (imageUrl.isNotBlank() && phone.isNotBlank()) {
            val formatPhone = PhoneNumberUtils.formatPhoneNumber(phone)
            val holdList = getUserImagesList()
            holdList.add(formatPhone + SPLITOR_USER_IMAGE + imageUrl)
            prefs.edit().putStringSet(USER_IMAGE_IN_CASH_KEY, holdList.distinct().toMutableSet())
                .apply()
        }
        /*val formatPhone = PhoneNumberUtils.formatPhoneNumber(phone)
        prefUsersImages.edit().putString(formatPhone, imageUrl).apply()*/
    }

    fun clearUserImagesCash() {
        val actualTimeMilliss = getCurrentTimeInMiliss()
        if (actualTimeMilliss >= KolaWhalletApplication.userPref.checDetectedNameDelai) {
            prefs.edit().remove(USER_IMAGE_IN_CASH_KEY).apply()
        }
    }

    /**
     * to manage users image local cash
     */


    /**
     * to manage users image local cash
     */
    private val DETECTED_NAME_KEY = "DETECTED_NAME_KEY"
    private val SPLITOR_DETECTED_NAME = "&&"
    fun getDetectedNamesList(): MutableList<String> {
        return prefs.getStringSet(
            DETECTED_NAME_KEY,
            mutableSetOf()
        )!!.toMutableList()
            .apply {
                distinct()
            }
    }

    fun getUserName(phone: String): String? {
        if (phone.isNotBlank()) {
            val formatPhone = PhoneNumberUtils.remove237ToPhoneNumber(phone)
            val data = getDetectedNamesList().find { value ->
                value.contains(formatPhone, true)
            }
            return if (data != null) {
                val tab = data.split(SPLITOR_DETECTED_NAME)
                if (tab.size == 2) {
                    tab[1]
                } else {
                    ""
                }
            } else {
                ""
            }
        } else {
            return ""
        }
    }

    fun saveUserName(phone: String, userName: String) {
        if (userName.isNotBlank() && phone.isNotBlank()) {
            val formatPhone = PhoneNumberUtils.remove237ToPhoneNumber(phone)
            val holdList = getDetectedNamesList()
            holdList.add(formatPhone + SPLITOR_DETECTED_NAME + userName)
            prefs.edit().putStringSet(DETECTED_NAME_KEY, holdList.distinct().toMutableSet()).apply()
        }
    }


    private val TRANSACTION_BUDGETIZED = "TRANSACTION_BUDGETIZED"

    fun resetTransactionBudgetized() {
        prefs.edit().putString(TRANSACTION_BUDGETIZED, "").apply()
    }

    /**
     * Budget_selected
     *
     * this is the budget id selected by the user to display the budget that the user has left
     */
    private val BUDGET_SELECTED = "BUDGET_SELECTED"

    var budgetSelectedId: String?
        get() = prefs.getString(BUDGET_SELECTED, "")
        set(value) = prefs.edit().putString(BUDGET_SELECTED, value).apply()


    private val PERSONAL_BUDGET = "PERSONAL_BUDGET"
    var personalBudgetId: String?
        get() = prefs.getString(PERSONAL_BUDGET, "")
        set(value) = prefs.edit().putString(PERSONAL_BUDGET, value).apply()
}