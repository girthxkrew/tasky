package com.rkm.tasky.sync.mapper

import com.rkm.tasky.database.model.TaskEntity
import com.rkm.tasky.network.model.request.TaskRequest

fun TaskEntity.asTaskRequest(): TaskRequest {
    return TaskRequest(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = this.remindAt,
        isDone = this.isDone
    )
}