package com.insanoid.whatsapp.chatScreen
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Reaction(
    val userId: String = "",
    val emoji: String = ""
) {
    constructor() : this("", "") // For Firebase
}