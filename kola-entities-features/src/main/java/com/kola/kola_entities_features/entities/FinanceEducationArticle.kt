package com.kola.kola_entities_features.entities

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class FinanceEducationArticle(
    var articleUid: String,
    val title: String,
    val subTitle: String,
    val urlIllustrationImg: String,
    val contentText: String,
    val financialEducator: FinancialEducator,
    val publicationDate: Date,
    var likes: ArrayList<String>, //content ids of likers
    var commentNumber: ArrayList<String>, //contient les ids des gens qui ont commentés ***********
    var sharesNumber: ArrayList<String>, //contient les ids des personnes qui ont partagé
    val keyWordList: ArrayList<String>,//contient la liste des keywords de l'article
    val userIdBookmarks: ArrayList<String>//contient la liste des id des users qui ont bookmark l'article
) : Serializable {
    var commentNumberInt = 0
    var shareNumberInt = 0
    val status =""
    val latestUpdateAt = Date()
    constructor() : this(
        "", "", "", "", "", FinancialEducator(), Date(), ArrayList(),
        ArrayList(),
        ArrayList(),
        ArrayList(),
        ArrayList()
    )


}