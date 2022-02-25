package com.frost.project_wm.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val email: String,
    val nombre: String,
    val rol: String,
    val empresa: String
): Parcelable{}

data class UserBody(
    val body: User
)
