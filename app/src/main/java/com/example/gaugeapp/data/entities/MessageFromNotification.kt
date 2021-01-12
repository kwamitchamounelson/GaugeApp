package com.example.gaugeapp.data.entities

data class MessageFromNotification(
    var title: String,
    var body: String,
    var messageType: String
) {

    constructor() : this("", "", "")
}