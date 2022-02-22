package com.projectwm.api.models

import javax.validation.constraints.Email

data class UsuarioLocal(
    @field:Email
    val email : String,
    val nombre : String?,
    val rol : String?,
    val empresa : String?){
}