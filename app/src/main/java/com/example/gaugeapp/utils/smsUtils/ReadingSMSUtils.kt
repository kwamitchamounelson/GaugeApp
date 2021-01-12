package com.example.gaugeapp.utils.smsUtils

import android.content.Context
import android.provider.Telephony
import android.util.Log
import com.example.gaugeapp.KolaWhalletApplication
import com.example.gaugeapp.KolaWhalletApplication.Companion.userPref
import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.commonRepositories.smsRep.SMSFirestoreRepository
import com.example.gaugeapp.data.enums.ENUMOPERATEUR
import com.example.gaugeapp.utils.PhoneNumberUtils.remove237ToPhoneNumber
import com.example.gaugeapp.utils.generateSmsId
import com.example.gaugeapp.utils.getDeviceId
import com.example.gaugeapp.utils.getOperatorFromList
import com.kola.smsmodule.constates.FireBaseCons
import com.kola.smsmodule.entities.CustumSMS
import com.kola.smsmodule.entities.CustumSMSObjet
import com.kola.smsmodule.enums.ENUM_LANGUAGES
import com.kola.smsmodule.enums.ENUM_SERVICE_ANALYSIS
import com.kola.smsmodule.enums.ENUM_SERVICE_OPERATEUR
import com.kola.smsmodule.pref.SmsModuleSharedPref
import com.kola.smsmodule.util.RegexConfig
import java.util.*
import kotlin.collections.ArrayList

object ReadingSMSUtils {

    /**
     * ce code a pour but de lire le dernier SMS arrive dans les sms de l'utilisateur
     *
     */
    /**
     * @english
     * this method has as objective to read the last arrived SMS of the user
     */

    private val TAG = "ReadingSMSUtils"
    fun getDeviceSMS(
        context: Context,
        sortOrderAndLimit: String = "",
        selectionCriterion: String? = null
    ): ArrayList<CustumSMSObjet> {

        val custumSmsList = ArrayList<CustumSMSObjet>()

        val c = context.contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            arrayOf(
                Telephony.Sms.Inbox._ID,
                Telephony.Sms.Inbox.ADDRESS,
                Telephony.Sms.Inbox.BODY,
                Telephony.Sms.Inbox.DATE,
                Telephony.Sms.Inbox.DATE_SENT
            ),
            selectionCriterion, null, sortOrderAndLimit
        )
        val totalSMS = c!!.count
        var messageId: String
        var senderPhone: String
        var messageBody: String
        var reciveDate: Date
        var sendingDate: Date

        if (c.moveToFirst()) {
            for (i in 0 until totalSMS) {
                messageId = c.getString(0)
                senderPhone = c.getString(1)
                messageBody = c.getString(2)
                reciveDate = Date((c.getString(3)).toLong())
                sendingDate = Date((c.getString(4)).toLong())

                /* on concatène à l'id l'imeil du téléphone car il peut arriver que
                * l'utilisateur perde de téléphone */
                val custumSMsTmp = CustumSMSObjet(
                    "",
                    senderPhone,
                    messageBody,
                    reciveDate,
                    sendingDate
                )
                //messageId += getDeviceId(context, custumSMsTmp)
                messageId += generateSmsId(custumSMsTmp)
                custumSMsTmp.messageId = messageId

                custumSmsList.add(custumSMsTmp)

                c.moveToNext();
            }
        }
        c.close()
        return custumSmsList
    }


    fun extractInformationsFromSMS(
        context: Context,
        custumSmsObjectList: ArrayList<CustumSMSObjet>
    ): ArrayList<CustumSMS>? {

        val custumSmsList = ArrayList<CustumSMS>()
        custumSmsObjectList.forEach { curentCustmSms ->
            curentCustmSms.originalMessage = curentCustmSms.messageBody.toUpperCase()

            Log.d(
                "REPORTED_TRANs_WORKER",
                "-------------------------- curentCustmSms:  $curentCustmSms"
            )


            // curentCustmSms.messageBody = curentCustmSms.messageBody.replace("\\s".toRegex(), "").toLowerCase(Locale.ROOT)

            /**
             * @french
             * on set la valeur du numero utilisateur
             *
             */
            /**
             * @english
             * we set the value of the user's phone number
             */
            FireBaseCons.numbers = remove237ToPhoneNumber(userPref.useOrangePhoneNumber)

            /** on recherche l'expediteur du sms: orange money?, mtn money, wandanet?, ...
             *
             */
            /**
             * @english
             * we look for the sender of the sender sms: orange Money, mtn mobile money, wandanet.
             *
             */
            /**
             * @french
             * et on renvoie null lorsque l'expediteur n'est pas l'un d'entre eu
             *
             */
            /**
             * @english
             * and return null when the sender
             */
            val serviceOperator = getServiceOperator(curentCustmSms)
            if (serviceOperator != null) {
                /**
                 * @french
                 *  On recherche le type de service: envoie d'argent, achat de credit de communication?, achat internet?, retrait d'argent?, envoie de credit,
                 *
                 */
                /**
                 * we look for the type of service: transfer, airtime, data bundle, withdrawal
                 */


                val service = serviceOperator.getService(curentCustmSms, ENUM_LANGUAGES.FRENCH)

                if ((service != ENUM_SERVICE_ANALYSIS.UNCATEGORIZED_SERVICES) && (service != ENUM_SERVICE_ANALYSIS.FAILED_SERVICES)) {

                    //On lit le solde restant précedant
                    val smsModulePref = SmsModuleSharedPref(context)
                    var soldeRestantPrecedant = 0.0
                    try {
                        soldeRestantPrecedant = smsModulePref.remainingBalance.toDouble()
                    } catch (ex: NumberFormatException) {
                        ex.printStackTrace()
                    }

                    // on extrait les informations de la transaction à l'interieur du message
                    val transaction =
                        service.getTransactionDetail(
                            service,
                            curentCustmSms,
                            ENUM_LANGUAGES.FRENCH,
                            soldeRestantPrecedant
                        )

                    transaction.previousBalance = soldeRestantPrecedant

                    //On enregistre le solde restant précedant
                    try {
                        smsModulePref.remainingBalance = transaction.soldeRestant.toFloat()
                    } catch (ex: NumberFormatException) {
                        ex.printStackTrace()
                    }

                    /**
                     * @english
                     * A l'aide de la transaction on crèe le message important que nous devons sauvegarder
                     *
                     */
                    /**
                     * @french
                     * with the help of the transaction we create the important message that we want to save
                     *
                     */
                    val custumSMS =
                        service.getCustumSMS(transaction, curentCustmSms, serviceOperator)
                    custumSMS.userAuthPhoneNumber = FireStoreAuthUtil.getUserPhoneNumber()
                    custumSMS.userid = FireStoreAuthUtil.getUserUID()

                    val simCartId = when (custumSMS.serviceOperateur) {
                        ENUM_SERVICE_OPERATEUR.SERVICE_ORANGE_MONEY -> {
                            getOperatorFromList(
                                KolaWhalletApplication.currentUser.mobileMoneyNumbers,
                                ENUMOPERATEUR.ORANGE_MONEY.name
                            )?.simCartIccId ?: ""
                        }
                        ENUM_SERVICE_OPERATEUR.SERVICE_MTN_MONEY -> {
                            getOperatorFromList(
                                KolaWhalletApplication.currentUser.mobileMoneyNumbers,
                                ENUMOPERATEUR.MTN_MONEY.name
                            )?.simCartIccId ?: ""
                        }
                        else -> {
                            ""
                        }
                    }
                    custumSMS.simcardIccId = simCartId
                    custumSMS.useuDeviceId = getDeviceId(context)

                    //on utilise l'ID de la transaction comme ID du message s'il ne s'agit pas d'une interogation de solde
                    if ((custumSMS.typeTransaction != ENUM_SERVICE_ANALYSIS.SOLDE_MTN)
                        && (custumSMS.typeTransaction != ENUM_SERVICE_ANALYSIS.SOLDE_ORANGE)
                    ) {
                        curentCustmSms.messageId = transaction.transactionId
                    }

                    // Toast.makeText(context, custumSMS.transaction.toString(), Toast.LENGTH_LONG).show()
                    Log.d("smsListner", custumSMS.transaction.toString())
                    custumSmsList.add(custumSMS)
                } else {
                    /**
                     * On ne reconnais pas la catégorie de la transaction, on va donc l'upload dans le serveur comme étant une transaction
                     * de catégorie inconnue
                     * */

                    // On vérifie si la transaction n'avais pas encore été uploader dans le serveur auparavant avant de l'uploader
                    if (!userPref.getAnalysedWorngtransactionsId()
                            .contains(curentCustmSms.messageId)
                    ) {
                        //0n exclus de l'envoie au serveur les deuxièmes SMS d'achat de credit MTN
                        //0n exclus de l'envoie au serveur les notifications informent à l'utilisateur qu'il est sur le point de faire un retrait

                        //TODO add this regex on server extractInformationsFromSMS()
                        val regexKeyToExclude =
                            RegexConfig.VALUE_GET_EXTRACT_INFORMATIONS_FROM_SMS_VALUE_TO_EXCLUD_PATTERN.toRegex()
                        //""""MTNC AIRTIME|VOUS ALLEZ FAIRE UN RETRAIT|YOU WILL DO A CASH OUT|(HTTP://BIT\.LY/CGU_OM)""".toRegex()

                        if (!curentCustmSms.originalMessage.contains(regexKeyToExclude)) {

                            //Si le message est un message de "Failed" on l'enregistre dans la collection des failed messages
                            if (service == ENUM_SERVICE_ANALYSIS.FAILED_SERVICES) {
                                SMSFirestoreRepository.saveFailedTransaction(curentCustmSms)
                            }

                            if (service == ENUM_SERVICE_ANALYSIS.UNCATEGORIZED_SERVICES) {

                                SMSFirestoreRepository.saveUncategorisedTransaction(
                                    curentCustmSms
                                )
                            }
                            // on l'enregistre dans la liste des wrongs transactions pour ne plus avoir à l'uploader dans le serveur prochainement
                            userPref.saveAnalysedWorngtransactionId(curentCustmSms.messageId)
                        }
                    }
                }
            }
        }

        return custumSmsList
    }

    private fun getServiceOperator(custumSMSObjet: CustumSMSObjet): ENUM_SERVICE_OPERATEUR? {
        return when (custumSMSObjet.senderPhone) {
            ENUM_SERVICE_OPERATEUR.SERVICE_ORANGE_MONEY.curentServiceOperator.aireTimeService -> {
                ENUM_SERVICE_OPERATEUR.SERVICE_ORANGE_MONEY
            }
            ENUM_SERVICE_OPERATEUR.SERVICE_MTN_MONEY.curentServiceOperator.aireTimeService -> {
                ENUM_SERVICE_OPERATEUR.SERVICE_MTN_MONEY
            }
            //TODO La liste n'est pas encore complette
            else -> null
        }
    }
}