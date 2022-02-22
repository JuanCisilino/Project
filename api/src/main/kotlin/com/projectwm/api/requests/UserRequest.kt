package com.projectwm.api.requests

import javax.validation.constraints.NotEmpty

data class UserRequest(
    @field:NotEmpty
    val email: String,
    val rol: String
)
