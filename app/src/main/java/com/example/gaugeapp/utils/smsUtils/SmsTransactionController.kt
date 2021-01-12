package com.example.gaugeapp.utils.smsUtils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.gaugeapp.data.entities.Cashflow
import com.example.gaugeapp.KolaWhalletApplication
import com.example.gaugeapp.utils.EntitiesUtils.TransactionStateResult
import com.example.gaugeapp.utils.PhoneNumberUtils
import com.kola.smsmodule.entities.CustumSMS
import com.kola.smsmodule.entities.Transaction
import com.kola.smsmodule.enums.ENUM_SERVICE_ANALYSIS
import com.kola.smsmodule.enums.ENUM_SERVICE_OPERATEUR
import com.kola.smsmodule.pref.SmsModuleSharedPref
import java.util.*


object SmsTransactionController {

    private val TAG = "SmsTransactionController"

    /**
     * Chek if transaction make from application or not
     * */
    @SuppressLint("LongLogTag")
    fun checkIfTransactionMakedFromApp(curentCustumSMS: CustumSMS, context: Context): Boolean {
        val smsModuleSharedPref = SmsModuleSharedPref(context)
        val transactionTrace = smsModuleSharedPref.getTransactionTrace()

        if (transactionTrace != null) {
            Log.d(TAG, "***************transactionTrace is not null $transactionTrace ")
            Log.d(TAG, "CustumSMS: ${curentCustumSMS}")

            //si le montant de la transaction est le même
            if (curentCustumSMS.transaction?.montantTransaction == transactionTrace.amount) {
                Log.d(TAG, "*****************transaction amounts are the same ")

                // s'il s'agit du même opérateur
                if (curentCustumSMS.serviceOperateur == transactionTrace.operatorService) {
                    Log.d(TAG, "***********serviceOperateur are the same ")

                    val recipientPhoneNumber = PhoneNumberUtils.remove237ToPhoneNumber(
                        curentCustumSMS.transaction?.recipientPhoneNumber ?: ""
                    )
                    // si le numéro de téléphone du receiver est le même
                    if (recipientPhoneNumber.equals(
                            transactionTrace.receiverPhoneNumber,
                            true
                        )
                    ) {
                        Log.d(TAG, "*************recipientPhoneNumber are the same ")
                        //S'il sagit de la même catégorie de transaction
                        if (curentCustumSMS.typeTransaction == transactionTrace.serviceAnalysis) {
                            Log.d(TAG, "***************** sil sagit de llla même catégorie")
                            //alors la transaction qui est entrain d'être analysée s'est faite via l'application

                            smsModuleSharedPref.resetTransactionTrace()
                            return true
                        }
                    }
                }
            }
        }
        Log.d(TAG, " latesd transaction doesn't maked from application")
        return false
    }


    /**
     * Check if transaction is a good transaction
     * */
    fun controlerEtatTransaction(
        transaction: Transaction,
        originalMsg: String
    ): TransactionStateResult {

        //Vérifie si la catégorie de la transaction est une interogation de seolde MTN ou Orange
        if ((transaction.transactionService == ENUM_SERVICE_ANALYSIS.SOLDE_ORANGE) || (transaction.transactionService == ENUM_SERVICE_ANALYSIS.SOLDE_MTN)) {
            return TransactionStateResult(true, "")
        }

        //Vérifie si les frais de transaction sont supérieures ou égales au montant de la transaction pour orange et MTN
        if (transaction.fraisTransaction >= transaction.montantTransaction) {

            Log.d(
                "REPORTED_TRANs_WORKER",
                "-------------------------- Message original:  $originalMsg"
            )
            Log.d("REPORTED_TRANs_WORKER", "-------------------------- transaction:  $transaction")

            return TransactionStateResult(
                false,
                "les frais de transaction sont supérieures ou égales au montant de la transaction"
            )
        }

        //Vérifie si la commision est supérieure au montant de la transaction ou si la commission est inférieur à 0 pour orange et MTN
        if ((transaction.commission >= transaction.montantTransaction) || (transaction.commission < 0)) {
            return TransactionStateResult(
                false,
                "commision est supérieure au montant de la transaction"
            )
        }

        //Vérifie si les frais de transaction sont supérieures ou  égales à 5_000 fr ou si elle sont inférieures à 0 fr pour orange et MTN
        if ((transaction.fraisTransaction >= 5_000) || (transaction.fraisTransaction < 0)) {
            return TransactionStateResult(
                false,
                "les frais de transaction sont son égale à ${transaction.fraisTransaction} fr"
            )
        }

        //vérifie si le montant de la transaction est inférieure ou égale à 0fr pour orange et MTN
        if ((transaction.montantTransaction <= 0)) {

            // si oui il y'a erreur dans ce ça que si la catégorie de la transaction est different de solde orange et solde MTN
            if ((transaction.transactionService != ENUM_SERVICE_ANALYSIS.SOLDE_MTN) && (transaction.transactionService != ENUM_SERVICE_ANALYSIS.SOLDE_ORANGE)) {
                return TransactionStateResult(
                    false,
                    "le montant de la transaction est inférieure ou égale à 0fr"
                )
            }
        }

        //vérifie si le solde restant de la transaction est inférieure ou égale à 0fr pour orange et MTN
        if (transaction.soldeRestant < 0) {
            return TransactionStateResult(
                false,
                "le solde restant de la transaction est inférieure ou égale à 0fr"
            )
        }


        // vérifie si le numéro de téléphone est non vide et si la taille du numéro est different de 9 pour orange et MTN
        if (transaction.senderPhoneNumber.isNotEmpty() && PhoneNumberUtils.remove237ToPhoneNumber(
                transaction.senderPhoneNumber
            ).length != 9
        ) {
            return TransactionStateResult(
                false,
                "le numéro de téléphone de l'émetteur est non vide et la taille de ce numéro est different de 9"
            )
        }

        // vérifie si le montant de la transaction est supérieur ou égale à  2_000_000 pour orange et MTN
        if (transaction.montantTransaction >= 2_000_000) {
            return TransactionStateResult(
                false,
                "le montant de la transaction est supérieur ou égale à  2_000_000"
            )
        }

        //Pour les dépôts orange et MTN, le nouveau solde doit etre supérieur ou égale au montant de la transaction
        if ((transaction.transactionService == ENUM_SERVICE_ANALYSIS.DEPOT_ORANGE)
            || (transaction.transactionService == ENUM_SERVICE_ANALYSIS.DEPOTS_MTN)
        ) {
            if (transaction.soldeRestant < transaction.montantTransaction) {
                return TransactionStateResult(
                    false,
                    "Il sagit d'un dépôt, Le nouveau solde est inférieur au montant de la transaction"
                )
            }
        }

        //Pour les reception orange et MTN, le nouveau solde doit etre supérieur ou égale au montant de la transaction
        if ((transaction.transactionService == ENUM_SERVICE_ANALYSIS.RECEPTION_ORANGE)
            || (transaction.transactionService == ENUM_SERVICE_ANALYSIS.RECEPTION_MTN)
        ) {
            if (transaction.soldeRestant < transaction.montantTransaction) {
                return TransactionStateResult(
                    false,
                    "Il sagit d'une réception, Le nouveau solde est inférieur au montant de la transaction"
                )
            }
        }

        return TransactionStateResult(true, "")
    }

    fun setUserHaveGoodTransaction(curentCustumSMS:CustumSMS){

        //Notify that user have a good MTN transaction
        if ((curentCustumSMS.serviceOperateur == ENUM_SERVICE_OPERATEUR.SERVICE_MTN_MONEY)
            && !(KolaWhalletApplication.userPref.hasGoodMtnTransaction)
        ) {
            KolaWhalletApplication.userPref.hasGoodMtnTransaction = true
        }

        //Notify that user have a good Orange transaction
        if ((curentCustumSMS.serviceOperateur == ENUM_SERVICE_OPERATEUR.SERVICE_ORANGE_MONEY)
            && !(KolaWhalletApplication.userPref.hasGoodOrangeTransaction)
        ) {
            KolaWhalletApplication.userPref.hasGoodOrangeTransaction = true
        }
    }
}