package com.projectwm.api.models


data class ProductoLocal(
    val id : Int,
    val title : String?,
    val description : String?,
    val cost : Double?,
    val stock : Int,
    val image : String?,
    val company : String?,
    val type : String?
){

}
