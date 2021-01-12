package com.example.gaugeapp.utils.EntitiesUtils

data class PermissionResult(
    var permissionName: String,
    var state: Boolean?=null,
    var shouldRational: Boolean?=null,
    var descrition: String? = null,
    var limitations: String? = null,
    var image: Int? = null,
    var name :String = ""
) {
    constructor() : this("")
}