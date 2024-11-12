package com.rkm.tasky.network.model.request

import com.google.gson.annotations.SerializedName

data class TaskRequest(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("time")
    val time: Long,
    @SerializedName("remindAt")
    val remindAt: Long,
    @SerializedName("isDone")
    val isDone: Boolean
)
