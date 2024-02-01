package com.example.cashtrack.ModelClass

data class AuthModel(
    var name:String,
    var email:String,
    var password:String
){
    constructor() : this("","", "")
}
