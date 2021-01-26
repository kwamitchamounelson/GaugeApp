package com.kola.kola_entities_features.entities

import com.kola.kola_entities_features.enums.ENUMEXPENSECATEGORYTYPE

data class ExpenseCategory(val
                           name: Int,
                           val image: Int,
                           val color: Int,
                           val type: ENUMEXPENSECATEGORYTYPE?,
                           val background: Int) {
    constructor():this(0, 0, 0, null, 0)
}