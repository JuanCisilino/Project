package com.frost.project_wm.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val cost: Double,
    val stock: Int,
    val image: String,
    val company: String,
    val type: String
): Parcelable
