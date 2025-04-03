package com.insanoid.whatsapp.model

 data class PhoneAuthUser(
     val userId: String="",
     val phoneNumber: String="",
     val name: String="",
     val status: String="",
     val profileImage: String?=null
 )
