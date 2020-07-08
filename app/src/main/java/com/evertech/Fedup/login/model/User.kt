package com.evertech.Fedup.login.model

data class User(
    val area_code: String,
    val email: String,
    val member_grade: String,
    val name: String,
    val phone: String,
    val token: String,
    val userId: String
)