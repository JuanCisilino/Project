package com.projectwm.api.responses

data class PagingResponse<T>(
    val total: Long,
    val items: List<T>
)