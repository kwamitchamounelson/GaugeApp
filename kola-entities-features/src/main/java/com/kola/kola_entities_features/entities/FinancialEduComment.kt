package com.kola.kola_entities_features.entities

import java.util.*
import kotlin.collections.ArrayList

data class FinancialEduComment(
    var commentUid: String,
    var articleUid:String,
    val commentText: String,
    val ownerUid: String,
    val date:Date
) {
    var parentCommentuId= ""
    var likes =ArrayList<String>()
    var userName = ""
    var userImage = ""
    var fromDashboad:Boolean = false
    constructor() : this("","", "", "", Date())
}