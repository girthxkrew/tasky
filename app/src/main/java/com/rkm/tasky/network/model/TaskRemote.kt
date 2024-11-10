package com.rkm.tasky.network.model

data class TaskRemote(
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long,
    val isDone: Boolean
)
