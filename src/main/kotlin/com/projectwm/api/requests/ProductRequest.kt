package com.projectwm.api.requests


data class ProductRequest(
    val id: Int,
    val title : String ?= null,
    val description : String ?= null,
    val type : String ?= null,
    val image : String ?= null,
    val company : String ?= null,
    val cost : Double ?= null,
    val stock : Int ?= null,
) {
}