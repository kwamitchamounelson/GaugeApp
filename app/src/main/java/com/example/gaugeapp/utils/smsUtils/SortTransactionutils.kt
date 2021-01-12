package com.example.gaugeapp.utils.smsUtils

import com.kola.kola_entities_features.entities.DateSortCondition
import com.example.gaugeapp.utils.extentions.getMonthFromDate
import com.example.gaugeapp.utils.extentions.getYearFromDate
import com.kola.smsmodule.entities.CustumSMS
import com.kola.smsmodule.enums.ENUM_SERVICE_ANALYSIS
import com.kola.smsmodule.enums.ENUM_SERVICE_OPERATEUR

object SortTransactionutils {


    /**
     * Cette fonction regroupe les transactions par operateurs mobileMoney
     * exemple: orange, mtn
     * **/
    fun groupTansactionsListByOperators(custumSmsList: ArrayList<CustumSMS>): Map<ENUM_SERVICE_OPERATEUR, List<CustumSMS>> {
        return custumSmsList.groupBy { it.serviceOperateur!! }
    }

    /**
     * Cette fonction regroupe les transactions par Sevices de l'opérateur
     * exemple:
     * ennvoie d'argent orange
     * envoie d'argent mtn
     * achat de credit orange, mtn,
     * ...
     * **/
    fun groupTansactionsListByTransactionServices(custumSmsList: ArrayList<CustumSMS>): Map<ENUM_SERVICE_ANALYSIS, List<CustumSMS>> {
        return custumSmsList.groupBy { it.transaction?.transactionService!! }
    }

    /**
     * Cette fonction regroupe les transactions par categorie independament de l'operateur
     * exemple:
     * ennvoie d'argent orange
     * envoie d'argent mtn
     * achat de credit orange, mtn,
     * ...
     * **/
    fun groupTansactionsListByCategorieName(custumSmsList: ArrayList<CustumSMS>): Map<String, List<CustumSMS>> {
        return custumSmsList.groupBy { it.transaction?.transactionService!!.curentService.name }
    }

    /**
     * Cette fonction regroupe les transactions par année - mois
     * exemple:
     * Janvier 2020
     * Fevrier 2020
     * mars 2019
     * ...
     * **/
    fun groupTansactionsListByDateCondition(custumSmsList: ArrayList<CustumSMS>): Map<DateSortCondition, List<CustumSMS>> {

        //on regroupe d'abord par mois-années
        val allTransactionsByDate = custumSmsList.groupBy {
            DateSortCondition(
                it.transaction?.date?.getMonthFromDate()!!,
                it.transaction?.date?.getYearFromDate()!!
            )
        }

        // on s'assure que les couples mois-année sont en ordre
        return allTransactionsByDate.toSortedMap(compareByDescending<DateSortCondition> {
                it.year
            }.thenByDescending {
                it.month
            })
    }
}