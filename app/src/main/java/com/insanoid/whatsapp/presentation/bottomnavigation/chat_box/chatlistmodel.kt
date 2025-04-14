package com.insanoid.whatsapp.presentation.bottomnavigation.chat_box


data class chatlistModel(
    val userId : String?=null,
    val name: String?=null,
    val phoneNumber: String?=null,

    val message: String?=null,
    val time: String?=null,
    val image: Int?=null,
    val profileImage: String?=null

){
    constructor() : this(null,null,null,null,null,null,null)

}