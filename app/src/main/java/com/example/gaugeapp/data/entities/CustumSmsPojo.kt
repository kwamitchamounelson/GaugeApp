package com.example.gaugeapp.data.entities

import com.kola.smsmodule.entities.CustumSMS
import java.util.*

data class CustumSmsPojo(val custumSms: CustumSMS, val saveAt: Date){
    constructor():this(CustumSMS(), Date())
}