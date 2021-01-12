package com.example.gaugeapp.data.entities

data class Release(var apkUrl:String, var name: String?,var senzitivity:Boolean) {

    var shareDescription = ""
    var illustrationUrl =""
    var versionCode =0L
    constructor():this("","",true)
}