package com.frost.project_wm.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: Int?=0,
    val title: String,
    val description: String,
    val cost: Double,
    val stock: Int,
    var image: String?,
    val company: String,
    val isActive: Boolean,
    val type: String
): Parcelable {}

data class ProductBody(
    val body: Product?
)